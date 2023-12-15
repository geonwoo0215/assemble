package com.geonwoo.assemble.domain.invitation.repository;

import com.geonwoo.assemble.domain.invitation.model.Invitation;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class InvitationJdbcRepository {

    private final SimpleJdbcInsert insert;

    public InvitationJdbcRepository(DataSource dataSource) {
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("invitation")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Invitation invitation) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(invitation);
        Number key = insert.executeAndReturnKey(param);
        return key.longValue();
    }
}
