package com.geonwoo.assemble.domain.imageurl.repository;

import com.geonwoo.assemble.domain.imageurl.model.ImageUrl;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class ImageUrlJdbcRepository {

    private final NamedParameterJdbcTemplate template;

    private final SimpleJdbcInsert insert;

    public ImageUrlJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("image_url")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(ImageUrl imageUrl) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(imageUrl);
        Number key = insert.executeAndReturnKey(param);
        return key.longValue();
    }

    public List<String> findByExpenseId(Long expenseId) {
        String sql = "select img_url from image_url where expense_id =:expenseId";

        try {
            Map<String, Long> param = Map.of("expenseId", expenseId);
            List<String> list = template.queryForList(sql, param, String.class);
            return list;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> findByPartyId(Long partyId) {
        String sql = "select img_url from image_url where party_id =:partyId";

        try {
            Map<String, Long> param = Map.of("partyId", partyId);
            List<String> list = template.queryForList(sql, param, String.class);
            return list;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
