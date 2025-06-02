package com.example.evimfix.wpAdmin.Utils.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.example.evimfix.wpAdmin.Models.Auth;

public class AuthRowMapper implements RowMapper<Auth> {

    @Override
    @Nullable
    public Auth mapRow(ResultSet rs, int rowNum) throws SQLException {
        Auth user = new Auth();

        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role_id"));
        user.setRoleName(rs.getString("role_name"));
        user.setEnabled(rs.getInt("enabled") == 1 ? true : false);

        return user;
    }
    
}
