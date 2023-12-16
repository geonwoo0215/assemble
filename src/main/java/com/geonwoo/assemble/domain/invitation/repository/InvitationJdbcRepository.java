package com.geonwoo.assemble.domain.invitation.repository;

import com.geonwoo.assemble.domain.invitation.model.Invitation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Repository
public class InvitationJdbcRepository {

    private final SimpleJdbcInsert insert;

    private final NamedParameterJdbcTemplate template;

    public InvitationJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("invitation")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Invitation invitation) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(invitation);
        Number key = insert.executeAndReturnKey(param);
        return key.longValue();
    }

    public Optional<Invitation> findByInviteCode(String inviteCode) {
        String sql = "select * from invitation where invite_code =:inviteCode";

        try {
            Map<String, String> param = Map.of("inviteCode", inviteCode);
            Invitation invitation = template.queryForObject(sql, param, invitationRowMapper());
            return Optional.of(invitation);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Invitation> invitationRowMapper() {
        return BeanPropertyRowMapper.newInstance(Invitation.class);
    }
}
