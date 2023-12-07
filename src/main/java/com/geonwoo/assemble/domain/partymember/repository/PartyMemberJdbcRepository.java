package com.geonwoo.assemble.domain.partymember.repository;

import com.geonwoo.assemble.domain.partymember.model.PartyMember;
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
public class PartyMemberJdbcRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public PartyMemberJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("party_member")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(PartyMember partyMember) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(partyMember);
        Number key = insert.executeAndReturnKey(param);
        return key.longValue();
    }

    public Optional<PartyMember> findById(Long id) {
        String sql = "select * from party_member where id=:id";
        try {
            Map<String, Long> param = Map.of("id", id);
            PartyMember partyMember = template.queryForObject(sql, param, partyMemberRowMapper());
            return Optional.of(partyMember);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void delete(Long id) {
        String sql = "delete from party_member where id = :id";

        try {
            Map<String, Long> param = Map.of("id", id);
            template.update(sql, param);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private RowMapper<PartyMember> partyMemberRowMapper() {
        return new BeanPropertyRowMapper<>(PartyMember.class);
    }


}
