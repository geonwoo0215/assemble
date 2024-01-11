package com.geonwoo.assemble.global.auth.token.repository;

import com.geonwoo.assemble.global.auth.token.model.RefreshToken;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RefreshTokenJdbcRepository {

    private NamedParameterJdbcTemplate template;

    private SimpleJdbcInsert insert;

    public RefreshTokenJdbcRepository(DataSource dataSource) {

        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("refresh_token")
                .usingGeneratedKeyColumns("id");
    }

    public void save(RefreshToken refreshToken) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(refreshToken);
        insert.execute(param);
    }

    public Optional<RefreshToken> findByUserId(Long memberId) {
        String sql = "select * from refresh_token where member_id =:memberId";

        try {
            Map<String, Long> param = Map.of("memberId", memberId);
            RefreshToken refreshToken = template.queryForObject(sql, param, refreshTokenRowMapper());
            return Optional.of(refreshToken);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Long id, String refreshToken) {
        String sql = "update refresh_token set refresh_token=:refreshToken where id=:id";

        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("refreshToken", refreshToken)
                    .addValue("id", id);
            template.update(sql, param);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        String sql = "select * from refresh_token where refresh_token =:refreshToken";

        try {
            Map<String, String> param = Map.of("refreshToken", refreshToken);
            RefreshToken token = template.queryForObject(sql, param, refreshTokenRowMapper());
            return Optional.of(token);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<RefreshToken> refreshTokenRowMapper() {
        return BeanPropertyRowMapper.newInstance(RefreshToken.class);
    }
}
