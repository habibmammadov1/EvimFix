package com.example.evimfix.wpAdmin.Utils.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.example.evimfix.wpAdmin.Models.VerificationToken;

public class TokenRowMapper implements RowMapper<VerificationToken> {
    @Override
    @Nullable
    public VerificationToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        VerificationToken token = new VerificationToken();

        token.setId(rs.getInt("id"));
        token.setToken(rs.getString("token"));
        //token.setTimestamp(rs.getTimestamp("time_stampp"));
        token.setExpiredAt(rs.getDate("expired_at"));
        token.setUserId(rs.getInt("user_id"));

        return token;
    }
}
