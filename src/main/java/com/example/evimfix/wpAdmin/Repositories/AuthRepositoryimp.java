package com.example.evimfix.wpAdmin.Repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.evimfix.wpAdmin.Models.Auth;
import com.example.evimfix.wpAdmin.Models.Role;
import com.example.evimfix.wpAdmin.Utils.Mappers.AuthRowMapper;

@Repository
public class AuthRepositoryimp implements AuthRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public AuthRepositoryimp(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Auth> getUser(String username) {     
        String query = "select t.id,\r\n" + //
                        "       t.username,\r\n" + //
                        "       t.first_name,\r\n" + //
                        "       t.last_name,\r\n" + //
                        "       t.email,\r\n" + //
                        "       t.password,\r\n" + //
                        "       t.enabled,\r\n" + //
                        "       r.name role_name, \r\n" + //
                        "       t.role_id \r\n" +
                        "       from HM_USERS t left join ROLES r on r.id = t.role_id \r\n"+
                        "       where (t.email = :email or t.username = :username) and t.status = 1";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("email", username)
                                                .addValue("username", username);

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(query, namedParameters, new AuthRowMapper()));
    }

    @Override
    public Optional<Auth> getUser(int id) {
        String query = "select t.id,\r\n" + //
                        "       t.username,\r\n" + //
                        "       t.first_name,\r\n" + //
                        "       t.last_name,\r\n" + //
                        "       t.email,\r\n" + //
                        "       t.password,\r\n" + //
                        "       r.name role_name, \r\n" + //
                        "       t.role_id, \r\n" +
                        "       t.enabled \r\n" +
                        "       from HM_USERS t left join ROLES r on r.id = t.role_id \r\n"+
                        "       where t.id = :id and t.status = 1";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("id", id);

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(query, namedParameters, new AuthRowMapper()));
    }  

    @Override
    public List<Role> getRollar() {
        String sql = "select t.id, t.name from ROLES t where t.status = 1";

        List<Role> rollar = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> map : rows) {
            rollar.add(new Role((String) map.get("id"), 
                                (String) map.get("name")
                        ));
        }

        return rollar;
    }

    @Override
    public String addUser(Auth auth) {
        String functionCall = "{ ? = call ADD_USER(?, ?, ?, ?, ?, ?) }";

        return jdbcTemplate.execute(functionCall, (CallableStatementCallback<String>) cs -> {
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cs.setString(2, auth.getUsername());
            cs.setString(3, auth.getFirstName());
            cs.setString(4, auth.getLastName());
            cs.setString(5, auth.getEmail());
            cs.setString(6, auth.getPassword());
            cs.setString(7, auth.getRole());

            cs.execute();
            
            return cs.getString(1);
        });
    }

    @Override
    public String updateProfile(Auth auth) {
        String query = "update HM_USERS t set t.first_name = ?, t.last_name = ?, t.email = ? where t.id = ? and t.status = 1";
        
        int result = 0;

        if ((!Objects.equals(auth.getPassword(), null)) && (!Objects.equals(auth.getPassword(), ""))) {            
            query = "update HM_USERS t set t.first_name = ?, t.last_name = ?, t.email = ?, t.password = ? where t.id = ?";
            result = jdbcTemplate.update(query, auth.getFirstName(), auth.getLastName(), auth.getEmail(), auth.getPassword(), auth.getId());
        }
        else{
            result = jdbcTemplate.update(query, auth.getFirstName(), auth.getLastName(), auth.getEmail(), auth.getId());
        }
        
        if (result > 0) {
            return "OK";
        }

        return "NO";
    }

    @Override
    public List<Auth> getUsers() {
        String query = "select t.id,\r\n" + //
                        "       t.username,\r\n" + //
                        "       t.first_name,\r\n" + //
                        "       t.last_name,\r\n" + //
                        "       t.email,\r\n" + //
                        "       t.password,\r\n" + //
                        "       t.role_id,\r\n" + //
                        "       t.enabled,\r\n" + //
                        "       r.name role_name,\r\n" + //
                        "       t.status from HM_USERS t left join ROLES r on r.id = t.role_id " +
                        "       where t.status = 1 order by r.id";

        return (List<Auth>) jdbcTemplate.query(query, new AuthRowMapper());
    }

    @Override
    public String updateUser(Auth auth) {
        String functionCall = "{ ? = call UPDATE_USER(?, ?, ?, ?, ?, ?, ?) }";

        return jdbcTemplate.execute(functionCall, (CallableStatementCallback<String>) cs -> {
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cs.setInt(2, auth.getId());
            cs.setString(3, auth.getFirstName());
            cs.setString(4, auth.getLastName());
            cs.setString(5, auth.getEmail());
            cs.setString(6, auth.getPassword());
            cs.setString(7, auth.getRole());
            cs.setInt(8, auth.isEnabled() ? 1 : 0);

            cs.execute();
            
            return cs.getString(1);
        });
    }

    @Override
    public String deleteUser(String username) {
        String functionCall = "{ ? = call DELETE_USER(?) }";

        return jdbcTemplate.execute(functionCall, (CallableStatementCallback<String>) cs -> {
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cs.setString(2, username);

            cs.execute();
            
            return cs.getString(1);
        });
    }

    @Override
    public HashMap<Integer, String> getUserTest() {
        String query = "select t.id, t.name from HM_USERS t";

        HashMap<Integer, String> emlakNovleri = new HashMap<>();
        
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        
        for (Map<String, Object> map : rows) {
            emlakNovleri.put(Integer.valueOf(String.valueOf(map.get("id"))), (String) map.get("name")) ;
        }

        return emlakNovleri;
    }
}
