package com.geonwoo.assemble.domain.expense.repository;

import com.geonwoo.assemble.domain.expense.model.Expense;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Optional<Expense> findById(Long id) {
        String sql = "select * from expense where id=:id";

        try {
            Map<String, Long> param = Map.of("id", id);
            Expense expense = template.queryForObject(sql, param, expenseRowMapper());
            return Optional.of(expense);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Expense> findAllByPartyId(Long partyId) {
        String sql = "select * from expense where party_id =:partyId";

        try {
            Map<String, Long> param = Map.of("partyId", partyId);
            List<Expense> list = template.query(sql, param, expenseRowMapper());
            return list;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private RowMapper<Expense> expenseRowMapper() {
        return BeanPropertyRowMapper.newInstance(Expense.class);
    }
}
