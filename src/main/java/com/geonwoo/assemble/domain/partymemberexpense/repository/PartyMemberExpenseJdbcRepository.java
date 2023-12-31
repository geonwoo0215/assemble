package com.geonwoo.assemble.domain.partymemberexpense.repository;

import com.geonwoo.assemble.domain.partymemberexpense.dto.PartyMemberExpenseDTO;
import com.geonwoo.assemble.domain.partymemberexpense.model.PartyMemberExpense;
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

    public List<PartyMemberExpenseDTO> findByExpenseId(Long expenseId) {

        String sql = "select pme.payer, m.nickname from party_member_expense pme" +
                " join party_member pm on pm.id = pme.party_member_id" +
                " join member m on m.id=pm.member_id" +
                " where pme.expense_id=:expenseId";

        try {
            Map<String, Long> param = Map.of("expenseId", expenseId);
            List<PartyMemberExpenseDTO> list = template.query(sql, param, memberNameRowMapper());
            return list;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private RowMapper<PartyMemberExpense> partyMemberExpenseRowMapper() {
        return BeanPropertyRowMapper.newInstance(PartyMemberExpense.class);
    }

    private RowMapper<PartyMemberExpenseDTO> memberNameRowMapper() {
        return BeanPropertyRowMapper.newInstance(PartyMemberExpenseDTO.class);
    }

}
