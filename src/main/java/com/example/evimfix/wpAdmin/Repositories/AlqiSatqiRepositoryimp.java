package com.example.evimfix.wpAdmin.Repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.evimfix.wpAdmin.Models.AlqiSatqi;
import com.example.evimfix.wpAdmin.Utils.Mappers.AlqiSatqiRowMapper;

@Repository
public class AlqiSatqiRepositoryimp implements AlqiSatqiRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AlqiSatqiRepositoryimp(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<AlqiSatqi> getAllAlqiSatqi() {
        String query = """
                    select t.id,
                    t.bashliq,
                    t.qiymet,
                    t.haqqinda,
                    t.menzil_nov,
                    t.otaq_sayi,
                    t.sahe,
                    t.mertebe,
                    t.mertebe_sayi,
                    t.temir,
                    t.chixarish,
                    t.sheher,
                    t.unvan,
                    t.metro,
                    t.sahibi,
                    t.telefon,
                    t.status,
                    t.kiraye_satish,
                    t.add_upd_tarix,
                    t.tikili_nov_id from HM_TIKILILER_ORD t where t.status = 1 order by t.id
            """;

        return (List<AlqiSatqi>) jdbcTemplate.query(query, new AlqiSatqiRowMapper());
    }

    @Override
    public String deleteAlqiSatqi(int id) {
        String functionCall = "{ ? = call DELETE_EV_ORD(?) }";

        return jdbcTemplate.execute(functionCall, (CallableStatementCallback<String>) cs -> {
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cs.setInt(2, id);

            cs.execute();
            
            return cs.getString(1);
        });
    }

    @Override
    public Optional<AlqiSatqi> getAlqiSatqiById(int id) {
        String query = """
                        select t.id,
                               t.bashliq,
                               t.qiymet,
                               t.haqqinda,
                               t.menzil_nov,
                               t.otaq_sayi,
                               t.sahe,
                               t.mertebe,
                               t.mertebe_sayi,
                               t.temir,
                               t.chixarish,
                               t.sheher,
                               t.unvan,
                               t.metro,
                               t.sahibi,
                               t.telefon,
                               t.status,
                               t.kiraye_satish,
                               t.add_upd_tarix,
                               t.tikili_nov_id from HM_TIKILILER_ORD t where t.id = :id and t.status = 1
                    """;
               

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("id", id);

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(query, namedParameters, new AlqiSatqiRowMapper()));
    }

    @Override
    public String[] getAlqiSatqiPhotos(int alqiSatqiId) {
        String sql = "select t.photopath from HM_TIKILILER_PHOTOS t where t.tikili_id = :tikili_id";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("tikili_id", alqiSatqiId);

        List<String> photos = namedParameterJdbcTemplate.queryForList(sql, namedParameters, String.class);
        
        return photos.toArray(new String[0]);
    }

    @Override
    public HashMap<Integer, String> getEmlakNovleri() {
        String sql = "select t.id, t.emlak_novu from HM_MENZIL_NOVLERI t";

        HashMap<Integer, String> emlakNovleri = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> map : rows) {
            emlakNovleri.put(Integer.valueOf(String.valueOf(map.get("id"))), (String) map.get("emlak_novu")) ;
        }

        return emlakNovleri;
    }

    @Override
    public HashMap<Integer, String> getAlishKiraye() {
        String sql = "select t.id, t.alish_kiraye from HM_ALISH_KIRAYE t where t.status = 1";

        HashMap<Integer, String> alishKirayeNovler = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> map : rows) {
            alishKirayeNovler.put(Integer.valueOf(String.valueOf(map.get("id"))), (String) map.get("alish_kiraye")) ;
        }

        return alishKirayeNovler;
    }

    @Override
    public int updateAlqiSatqi(AlqiSatqi alqiSatqi) {
        String query = """
                update HM_TIKILILER_ORD SET
                  bashliq = :bashliq,
                  qiymet = :qiymet,
                  haqqinda = :haqqinda,
                  menzil_nov = :menzilNov,
                  otaq_sayi = :otaqSayi,
                  sahe = :sahe,
                  mertebe = :mertebe,
                  mertebe_sayi = :mertebeSayi,
                  temir = :temir,
                  chixarish = :chixarish,
                  sheher = :sheher,
                  unvan = :unvan,
                  metro = :metro,
                  sahibi = :sahibi,
                  telefon = :telefon,
                  kiraye_satish = :kirayeSatish,
                  add_upd_tarix = :addUpdTarix
                WHERE id = :id
            """;

        Date customUtilDate = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            customUtilDate = dateFormatter.parse(new Date().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }    
        
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("bashliq", alqiSatqi.getBashliq())
            .addValue("qiymet", alqiSatqi.getQiymet())
            .addValue("haqqinda", alqiSatqi.getHaqqinda())
            .addValue("menzilNov", alqiSatqi.getMenzilNov())
            .addValue("otaqSayi", alqiSatqi.getOtaqSayi())
            .addValue("sahe", alqiSatqi.getSahe())
            .addValue("mertebe", alqiSatqi.getMertebe())
            .addValue("mertebeSayi", alqiSatqi.getMertebeSayi())
            .addValue("temir", alqiSatqi.getTemir())
            .addValue("chixarish", alqiSatqi.getChixarish())
            .addValue("sheher", alqiSatqi.getSheher())
            .addValue("unvan", alqiSatqi.getUnvan())
            .addValue("metro", alqiSatqi.getMetro())
            .addValue("sahibi", alqiSatqi.getSahibi())
            .addValue("telefon", alqiSatqi.getTelefon())
            .addValue("kirayeSatish", alqiSatqi.getKirayeSatish())
            .addValue("addUpdTarix", customUtilDate)
            .addValue("id", alqiSatqi.getId());
        
        return namedParameterJdbcTemplate.update(query, params);
    }

    @Override
    public String addAlqiSatqi(AlqiSatqi alqiSatqi) {
        String functionCall = "{ ? = call ADD_EV_ORD(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        return jdbcTemplate.execute(functionCall, (CallableStatementCallback<String>) cs -> {
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cs.setString(2, alqiSatqi.getBashliq());
            cs.setDouble(3, alqiSatqi.getQiymet());
            cs.setString(4, alqiSatqi.getHaqqinda());
            cs.setInt(5, alqiSatqi.getMenzilNov());
            cs.setInt(6, alqiSatqi.getOtaqSayi());
            cs.setDouble(7, alqiSatqi.getSahe());
            cs.setInt(8, alqiSatqi.getMertebe() == null ? 0 : alqiSatqi.getMertebe());
            cs.setInt(9, alqiSatqi.getMertebeSayi() == null ? 0 : alqiSatqi.getMertebeSayi());
            cs.setInt(10, alqiSatqi.getTemir());
            cs.setInt(11, alqiSatqi.getChixarish());
            cs.setInt(12, alqiSatqi.getSheher());
            cs.setString(13, alqiSatqi.getUnvan());
            cs.setInt(14, alqiSatqi.getMetro());
            cs.setString(15, alqiSatqi.getSahibi());
            cs.setString(16, alqiSatqi.getTelefon());
            cs.setInt(17, alqiSatqi.getKirayeSatish());

            cs.execute();
            
            return cs.getString(1);
        });
    }

    @Override
    public String addPhoto(String photoPath, int alqiSatqiId) {
        String functionCall = "{ ? = call ADD_PHOTO(?, ?) }";

        return jdbcTemplate.execute(functionCall, (CallableStatementCallback<String>) cs -> {
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cs.setString(2, photoPath);
            cs.setInt(3, alqiSatqiId);

            cs.execute();
            
            return cs.getString(1);
        });
    }    
}
