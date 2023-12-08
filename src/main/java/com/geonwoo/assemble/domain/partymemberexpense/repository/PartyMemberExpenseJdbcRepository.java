package com.geonwoo.assemble.domain.partymemberexpense.repository;

import com.geonwoo.assemble.domain.partymemberexpense.model.PartyMemberExpense;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PartyMemberExpenseJdbcRepository {

    private final NamedParameterJdbcTemplate template;

    private final SimpleJdbcInsert insert;

    public PartyMemberExpenseJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("party_member_expense")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(PartyMemberExpense partyMemberPrice) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(partyMemberPrice);
        Number key = insert.executeAndReturnKey(param);
        return key.longValue();
    }

}
