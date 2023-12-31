package com.geonwoo.assemble.domain.partymember.repository;

import com.geonwoo.assemble.domain.partymember.dto.PartyMemberDTO;
import com.geonwoo.assemble.domain.partymember.model.PartyMember;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
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

    public Optional<PartyMember> findById(Long id) {
        String sql = "select * from party_member where id=:id";
        try {
            Map<String, Long> param = Map.of("id", id);
            PartyMember partyMember = template.queryForObject(sql, param, partyMemberRowMapper());
            return Optional.of(partyMember);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<PartyMemberDTO> findAllByPartyId(Long partyId) {

        String sql = "select pm.*, m.nickname from party_member pm" +
                " join member m on pm.member_id = m.id" +
                " where party_id=:partyId";

        try {
            Map<String, Long> param = Map.of("partyId", partyId);
            List<PartyMemberDTO> list = template.query(sql, param, partyMemberDTORowMapper());
            return list;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<PartyMember> findByMemberIdAndPartyId(Long memberId, Long partyId) {
        String sql = "select * from party_member where member_id=:memberId and party_id=:partyId";

        try {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("memberId", memberId)
                    .addValue("partyId", partyId);
            PartyMember partyMember = template.queryForObject(sql, param, partyMemberRowMapper());
            return Optional.of(partyMember);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }


    public void delete(Long id) {
        String sql = "delete from party_member where id = :id";

        try {
            Map<String, Long> param = Map.of("id", id);
            template.update(sql, param);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private RowMapper<PartyMember> partyMemberRowMapper() {
        return new BeanPropertyRowMapper<>(PartyMember.class);
    }

    private RowMapper<PartyMemberDTO> partyMemberDTORowMapper() {
        return new BeanPropertyRowMapper<>(PartyMemberDTO.class);
    }

}
