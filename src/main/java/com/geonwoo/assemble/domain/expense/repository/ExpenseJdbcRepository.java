package com.geonwoo.assemble.domain.expense.repository;

import com.geonwoo.assemble.domain.expense.model.Expense;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ExpenseJdbcRepository {

    private final NamedParameterJdbcTemplate template;

    private final SimpleJdbcInsert insert;

    public ExpenseJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("expense")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Expense expense) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(expense);
        Number key = insert.executeAndReturnKey(param);
        return key.longValue();
    }
}
