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

    private RowMapper<Party> partyRowMapper() {
        return BeanPropertyRowMapper.newInstance(Party.class);
    }
}
