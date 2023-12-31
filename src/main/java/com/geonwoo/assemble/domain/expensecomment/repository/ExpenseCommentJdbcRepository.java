package com.geonwoo.assemble.domain.expensecomment.repository;

import com.geonwoo.assemble.domain.expensecomment.model.ExpenseComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class ExpenseCommentJdbcRepository {

    private final NamedParameterJdbcTemplate template;

    private final SimpleJdbcInsert insert;

    public ExpenseCommentJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("expense_comment")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(ExpenseComment expenseComment) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(expenseComment);
        Number key = insert.executeAndReturnKey(param);
        return key.longValue();
    }

    public List<ExpenseComment> findAllByExpenseId(Long expenseId) {

        String sql = "select * from expense_comment where expense_id=:expenseId order by group_no, comment_order";

        try {
            Map<String, Long> param = Map.of("expenseId", expenseId);
            List<ExpenseComment> list = template.query(sql, param, expenseCommentBeanPropertyRowMapper());
            log.info("{}", expenseId);
            return list;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExpenseComment> findById(Long id) {

        String sql = "select * from expense_comment where id =:id";

        try {
            Map<String, Long> param = Map.of("id", id);
            ExpenseComment expenseComment = template.queryForObject(sql, param, expenseCommentBeanPropertyRowMapper());
            return Optional.of(expenseComment);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    public Long findMaxGroupNoByExpenseId(Long expenseId) {
        String sql = "select IFNULL(MAX(group_no),0) from expense_comment where expense_id =:expenseId";

        try {
            Map<String, Long> param = Map.of("expenseId", expenseId);
            Long maxGroupNo = template.queryForObject(sql, param, Long.class);
            return maxGroupNo;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Long findMaxCommentOrderById(Long parentId) {

        String sql = "select IFNULL(MAX(comment_order),0) from expense_comment where parent_id=:parentId";

        try {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("parentId", parentId);

            Long maxCommentOrder = template.queryForObject(sql, param, Long.class);
            return maxCommentOrder;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateCommentOrder(Long expenseId, Long groupNo, Long commentOrder) {
        String sql = "update expense_comment set comment_order = comment_order + 1  where expense_id =:expenseId and group_no =:groupNo and comment_order>:commentOrder";

        try {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("expenseId", expenseId)
                    .addValue("groupNo", groupNo)
                    .addValue("commentOrder", commentOrder);
            template.update(sql, param);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private BeanPropertyRowMapper<ExpenseComment> expenseCommentBeanPropertyRowMapper() {
        return BeanPropertyRowMapper.newInstance(ExpenseComment.class);
    }
}
