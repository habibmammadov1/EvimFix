package com.example.evimfix.wpAdmin.Repositories.Helper;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class HelperRepositoryimp implements HelperRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public HelperRepositoryimp(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public String[] getAlqiSatqiPhotos(int alqiSatqiId) {
        String sql = "select t.photopath from HM_TIKILILER_PHOTOS t where t.tikili_id = :tikili_id";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("tikili_id", alqiSatqiId);

        List<String> photos = namedParameterJdbcTemplate.queryForList(sql, namedParameters, String.class);
        
        return photos.toArray(new String[0]);
    }
    
}
