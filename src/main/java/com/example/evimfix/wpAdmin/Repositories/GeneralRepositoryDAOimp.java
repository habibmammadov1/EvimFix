package com.example.evimfix.wpAdmin.Repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GeneralRepositoryDAOimp implements GeneralRepositoryDAO{
    private final JdbcTemplate jdbcTemplate;

    public GeneralRepositoryDAOimp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HashMap<Integer, String> getModullar() {
        String sql = "select t.id, t.modul_name from MODULLAR t where t.status = 1";

        HashMap<Integer, String> modullar = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> map : rows) {
            modullar.put(Integer.valueOf(String.valueOf(map.get("id"))), (String) map.get("MODUL_NAME")) ;
        }

        return modullar;
    }

    // @Override
    // public HashMap<Integer, String> getAllCourseCategories() {
    //     String sql = "select t.category_id, t.category_name, t.status from COURSE_CATEGORIES t where t.status = 0";

    //     HashMap<Integer, String> categories = new HashMap<>();

    //     List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

    //     for (Map<String, Object> map : rows) {
    //         categories.put(Integer.valueOf(String.valueOf(map.get("category_id"))), (String) map.get("category_name"));
    //     }

    //     return categories;
    // }
}
