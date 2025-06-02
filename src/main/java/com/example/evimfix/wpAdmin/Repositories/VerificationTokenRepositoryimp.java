package com.example.evimfix.wpAdmin.Repositories;

import java.sql.Date;
import java.util.Optional;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.evimfix.wpAdmin.Models.VerificationToken;
import com.example.evimfix.wpAdmin.Utils.Mappers.TokenRowMapper;

@Repository
public class VerificationTokenRepositoryimp implements VerificationTokenRepository{
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public VerificationTokenRepositoryimp(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        String query = "select t.id,\r\n" + //
                        "       t.token,\r\n" + //
                        //"       t.time_stampp,\r\n" + //
                        "       t.expired_at,\r\n" + //
                        "       t.user_id,\r\n" + //
                        "       t.status from verification_tokens t where t.token = :token and t.status = 1";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("token", token);

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(query, namedParameters, new TokenRowMapper()));
    }

    @Override
    public String deleteToken(VerificationToken token) {
        String functionCall = "{ ? = call DELETE_VERIFICATION_TOKEN(?) }";

        return jdbcTemplate.execute(functionCall, (CallableStatementCallback<String>) cs -> {
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cs.setLong(2, token.getId());

            cs.execute();
            
            return cs.getString(1);
        });
    }

    @Override
    public String addToken(VerificationToken token) {
        String functionCall = "{ ? = call ADD_VERIFICATION_TOKEN(?, ?, ?) }";

        return jdbcTemplate.execute(functionCall, (CallableStatementCallback<String>) cs -> {
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cs.setString(2, token.getToken());
            //cs.setTimestamp(3, token.getTimestamp());
            cs.setDate(3, token.getExpiredAt() == null ? null : new Date(token.getExpiredAt().getTime()));
            cs.setString(4, token.getUsername());
            cs.execute();

            return cs.getString(1);
        }); 
    }
    
}
