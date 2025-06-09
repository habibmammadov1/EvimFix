package com.example.evimfix.wpAdmin.Repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
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
    
    private AlqiSatqiRowMapper alqiSatqiRowMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AlqiSatqiRepositoryimp(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, AlqiSatqiRowMapper alqiSatqiRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.alqiSatqiRowMapper = alqiSatqiRowMapper;
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
                    s.city_name as sheher_adi,
                    t.unvan,
                    t.metro,
                    m.metro_name as metro_adi,
                    t.rayon,
                    r.name as rayon_adi,
                    t.sahibi,
                    t.telefon,
                    t.status,
                    t.kiraye_satish,
                    t.add_upd_tarix,
                    t.tikili_nov_id from HM_TIKILILER_ORD t 
                    LEFT JOIN HM_SHEHERLER s ON t.sheher = s.id
                    LEFT JOIN HM_RAYONLAR r ON t.rayon = r.id
                    LEFT JOIN HM_METROLAR m ON t.metro = m.id
                    where t.status = 1 order by t.id
            """;

        return (List<AlqiSatqi>) jdbcTemplate.query(query, alqiSatqiRowMapper);
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
    public String deleteAlqiSatqiPhotos(int id) {
        String functionCall = "{ ? = call DELETE_EV_PHOTO(?) }";

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
                               s.city_name as sheher_adi,
                               t.unvan,
                               t.metro,
                               m.metro_name as metro_adi,
                               t.rayon,
                               r.name as rayon_adi,
                               t.sahibi,
                               t.telefon,
                               t.status,
                               t.kiraye_satish,
                               t.add_upd_tarix,
                               t.tikili_nov_id from HM_TIKILILER_ORD t 
                               LEFT JOIN HM_SHEHERLER s ON t.sheher = s.id
                               LEFT JOIN HM_RAYONLAR r ON t.rayon = r.id
                               LEFT JOIN HM_METROLAR m ON t.metro = m.id
                               where t.id = :id and t.status = 1
                    """;
               

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("id", id);

        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(query, namedParameters, alqiSatqiRowMapper));
    }

    @Override
    public HashMap<Integer, String> getEmlakNovleri() {
        String sql = "select t.id, t.emlak_novu from HM_MENZIL_NOVLERI2 t";

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
                  add_upd_tarix = :addUpdTarix,
                  rayon = :rayon
                WHERE id = :id
            """;

        Date customUtilDate = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            customUtilDate = dateFormatter.parse(dateFormatter.format(customUtilDate));
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
            .addValue("rayon", alqiSatqi.getRayon())
            .addValue("id", alqiSatqi.getId()
            );
        
        return namedParameterJdbcTemplate.update(query, params);
    }

    @Override
    public String addAlqiSatqi(AlqiSatqi alqiSatqi) {
        String functionCall = "{ ? = call ADD_EV_ORD(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        return jdbcTemplate.execute(functionCall, (CallableStatementCallback<String>) cs -> {
            cs.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cs.setString(2, alqiSatqi.getBashliq());
            cs.setDouble(3, alqiSatqi.getQiymet());
            cs.setString(4, Jsoup.clean(alqiSatqi.getHaqqinda(), Safelist.none()));
            cs.setInt(5, alqiSatqi.getMenzilNov());
            cs.setInt(6, alqiSatqi.getOtaqSayi());
            cs.setDouble(7, alqiSatqi.getSahe());
            
            if (alqiSatqi.getMertebe() == null) {
                cs.setNull(8, java.sql.Types.INTEGER);
            } else {
                cs.setInt(8, alqiSatqi.getMertebe());
            }
            
            if (alqiSatqi.getMertebeSayi() == null) {
                cs.setNull(9, java.sql.Types.INTEGER);
            } else {
                cs.setInt(9, alqiSatqi.getMertebeSayi());
            }

            cs.setInt(10, alqiSatqi.getTemir());
            cs.setInt(11, alqiSatqi.getChixarish());
            cs.setInt(12, alqiSatqi.getSheher());
            cs.setString(13, alqiSatqi.getUnvan());

            if (alqiSatqi.getMetro() == null) {
                cs.setNull(14, java.sql.Types.INTEGER);
            } else {
                cs.setInt(14, alqiSatqi.getMetro());
            }

            cs.setString(15, alqiSatqi.getSahibi());
            cs.setString(16, alqiSatqi.getTelefon());
            cs.setInt(17, alqiSatqi.getKirayeSatish());
            
            if (alqiSatqi.getRayon() == null) {
                cs.setNull(18, java.sql.Types.INTEGER);
            } else {
                cs.setInt(18, alqiSatqi.getRayon());
            }

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

    @Override
    public HashMap<Integer, String> getSheherler() {
        String sql = "select t.id, t.city_name from HM_SHEHERLER t";

        HashMap<Integer, String> sheherler = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> map : rows) {
            sheherler.put(Integer.valueOf(String.valueOf(map.get("id"))), (String) map.get("city_name")) ;
        }

        return sheherler;
    }

    @Override
    public HashMap<Integer, String> getRayonlar() {
        String sql = "select t.id, t.name from HM_RAYONLAR t";

        HashMap<Integer, String> rayonlar = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> map : rows) {
            rayonlar.put(Integer.valueOf(String.valueOf(map.get("id"))), (String) map.get("name")) ;
        }

        return rayonlar;
    }

    @Override
    public HashMap<Integer, String> getMetrolar() {
        String sql = "select t.id, t.metro_name from HM_METROLAR t";

        HashMap<Integer, String> metrolar = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> map : rows) {
            metrolar.put(Integer.valueOf(String.valueOf(map.get("id"))), (String) map.get("metro_name")) ;
        }

        return metrolar;
    }

    @Override
    public List<AlqiSatqi> getAllAlqiSatqiByCategories(Integer sheher, Integer rayon, Integer metro, Double minQiymet,
            Double maxQiymet, Integer otaqSayi, Integer emlakNovu, Integer alishKiraye) {
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
                    s.city_name as sheher_adi,
                    t.unvan,
                    t.metro,
                    m.metro_name as metro_adi,
                    t.rayon,
                    r.name as rayon_adi,
                    t.sahibi,
                    t.telefon,
                    t.status,
                    t.kiraye_satish,
                    t.add_upd_tarix,
                    t.tikili_nov_id from HM_TIKILILER_ORD t 
                    LEFT JOIN HM_SHEHERLER s ON t.sheher = s.id
                    LEFT JOIN HM_RAYONLAR r ON t.rayon = r.id
                    LEFT JOIN HM_METROLAR m ON t.metro = m.id
                    where t.status = 1
            """;

        if (sheher != null && sheher != 0) {
            query += " and t.sheher = :sheher";
        }

        if (rayon != null && rayon != 0 && sheher != null && sheher == 8) {
            query += " and t.rayon = :rayon";
        }

        if (metro != null && metro != 0 && sheher != null && sheher == 8) {
            query += " and t.metro = :metro";
        }

        if (minQiymet != null && minQiymet > 0) {
            query += " and t.qiymet >= :minQiymet";
        }

        if (maxQiymet != null && maxQiymet > 0) {
            query += " and t.qiymet <= :maxQiymet";
        }

        if (otaqSayi != null && otaqSayi != 0) {
            query += " and t.otaq_sayi = :otaqSayi";
        }

        if (emlakNovu != null && emlakNovu != 0) {
            query += " and t.menzil_nov = :emlakNovu";
        }

        if (alishKiraye != null && alishKiraye != 0) {
            query += " and t.kiraye_satish = :alishKiraye";
        }

        query += " order by t.id";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("sheher", sheher)
                                                .addValue("rayon", rayon)
                                                .addValue("metro", metro)
                                                .addValue("minQiymet", minQiymet)
                                                .addValue("maxQiymet", maxQiymet)
                                                .addValue("otaqSayi", otaqSayi)
                                                .addValue("emlakNovu", emlakNovu)
                                                .addValue("alishKiraye", alishKiraye);

        return namedParameterJdbcTemplate.query(query, namedParameters, alqiSatqiRowMapper);
    }

    @Override
    public String[] getAlqiSatqiPhotos(int alqiSatqiId) {
        String query = "select t.photopath from HM_TIKILILER_PHOTOS t where t.tikili_id = :tikili_id";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                .addValue("tikili_id", alqiSatqiId);

        List<String> photos = namedParameterJdbcTemplate.queryForList(query, namedParameters, String.class);

        return photos.toArray(new String[0]);
    }
}
