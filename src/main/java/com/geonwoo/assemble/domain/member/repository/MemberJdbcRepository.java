package com.geonwoo.assemble.domain.member.repository;

import com.geonwoo.assemble.domain.member.model.Member;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class MemberJdbcRepository {

    private final NamedParameterJdbcTemplate template;

    private final SimpleJdbcInsert insert;

    public MemberJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Member member){
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = insert.executeAndReturnKey(param);
        return key.longValue();
    }

}
