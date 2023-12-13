package com.geonwoo.assemble.domain.party.repository;

import com.geonwoo.assemble.domain.party.dto.PartyUpdateDTO;
import com.geonwoo.assemble.domain.party.model.Party;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PartyJdbcRepository {

    private final NamedParameterJdbcTemplate template;

    private final SimpleJdbcInsert insert;

    public PartyJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("party")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Party party) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(party);
        Number key = insert.executeAndReturnKey(param);
        return key.longValue();
    }

    public Optional<Party> findById(Long id) {
        String sql = "select * from party where id =:id";

        try {
            Map<String, Long> param = Map.of("id", id);
            Party party = template.queryForObject(sql, param, partyRowMapper());
            return Optional.of(party);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Party> findAllByMemberId(Long memberId, int size, int offset) {
        String sql = "select p.* from party p" +
                " join party_member pm on p.id = pm.party_id" +
                " join member m on pm.member_id = m.id" +
                " where m.id =:memberId" +
                " order by p.id desc" +
                " limit :size" +
                " offset :offset";

        try {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("memberId", memberId)
                    .addValue("offset", offset)
                    .addValue("size", size);
            List<Party> list = template.query(sql, param, partyRowMapper());
            return list;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(Long id, PartyUpdateDTO partyUpdateDTO) {
        String sql = "update party set name=:name, content=:content, start_date=:startDate where id=:id";

        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("name", partyUpdateDTO.getName())
                    .addValue("content", partyUpdateDTO.getContent())
                    .addValue("startDate", partyUpdateDTO.getStartDate())
                    .addValue("id", id);
            template.update(sql, param);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        String sql = "delete from party where id =:id";
        try {
            Map<String, Long> param = Map.of("id", id);
            template.update(sql, param);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private RowMapper<Party> partyRowMapper() {
        return BeanPropertyRowMapper.newInstance(Party.class);
    }
}
