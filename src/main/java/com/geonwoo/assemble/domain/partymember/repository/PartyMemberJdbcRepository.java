package com.geonwoo.assemble.domain.partymember.repository;

import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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


}
