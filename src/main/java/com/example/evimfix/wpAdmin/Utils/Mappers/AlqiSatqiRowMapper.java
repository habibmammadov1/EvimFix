package com.example.evimfix.wpAdmin.Utils.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.evimfix.wpAdmin.Models.AlqiSatqi;
import com.example.evimfix.wpAdmin.Repositories.Helper.HelperRepository;

@Component
public class AlqiSatqiRowMapper implements RowMapper<AlqiSatqi> {

    private HelperRepository helperRepository;

    public AlqiSatqiRowMapper(HelperRepository helperRepository) {
        this.helperRepository = helperRepository;
    }

    @Override
    public AlqiSatqi mapRow(ResultSet rs, int rowNum) throws SQLException {
        AlqiSatqi alqiSatqi = new AlqiSatqi();
        
        alqiSatqi.setId(rs.getInt("id"));
        alqiSatqi.setBashliq(rs.getString("bashliq"));
        alqiSatqi.setQiymet(rs.getDouble("qiymet"));
        alqiSatqi.setHaqqinda(rs.getString("haqqinda").replace("&nbsp;", ""));
        alqiSatqi.setMenzilNov(rs.getInt("menzil_nov"));
        alqiSatqi.setMenzilNovAdi(getMenzilNovAdi(rs.getInt("menzil_nov")));
        alqiSatqi.setOtaqSayi(rs.getObject("otaq_sayi", Integer.class));
        alqiSatqi.setSahe(rs.getDouble("sahe"));
        alqiSatqi.setMertebe(rs.getObject("mertebe", Integer.class));
        alqiSatqi.setMertebeSayi(rs.getObject("mertebe_sayi", Integer.class));
        alqiSatqi.setTemir(rs.getObject("temir", Integer.class));
        alqiSatqi.setTemirAdi(rs.getObject("temir", Integer.class) == 1 ? "Təmirli" : "Təmirsiz");
        alqiSatqi.setChixarish(rs.getObject("chixarish", Integer.class));
        alqiSatqi.setChixarishAdi(rs.getObject("chixarish", Integer.class)== 1 ? "Çıxarışlı" : "Çıxarışsız");
        alqiSatqi.setSheher(rs.getInt("sheher"));
        alqiSatqi.setSheherAdi(rs.getString("sheher_adi"));
        alqiSatqi.setUnvan(rs.getString("unvan"));
        alqiSatqi.setMetro(rs.getInt("metro"));
        alqiSatqi.setMetroAdi(rs.getString("metro_adi"));
        alqiSatqi.setRayon(rs.getInt("rayon"));
        alqiSatqi.setRayonAdi(rs.getString("rayon_adi"));
        alqiSatqi.setSahibi(rs.getString("sahibi"));
        alqiSatqi.setTelefon(rs.getString("telefon"));
        alqiSatqi.setStatus(rs.getInt("status"));
        alqiSatqi.setKirayeSatish(rs.getInt("kiraye_satish"));
        alqiSatqi.setKirayeSatishAdi(rs.getInt("kiraye_satish") == 1 ? "Satış" : "Kirayə");
        alqiSatqi.setAddUpdTarix(rs.getDate("add_upd_tarix"));
        alqiSatqi.setTikiliNovId(rs.getInt("tikili_nov_id"));
        alqiSatqi.setTikiliNovAdi("Adi tikili");
        alqiSatqi.setPhotoPaths(helperRepository.getAlqiSatqiPhotos(rs.getInt("id")));
        try {
            alqiSatqi.setPhotoPath(helperRepository.getAlqiSatqiPhotos(rs.getInt("id"))[0]);    
        } catch (IndexOutOfBoundsException  e) {
            alqiSatqi.setPhotoPath(null);
            e.printStackTrace();
        }
        

        return alqiSatqi;
    }

    // Helper methods
    private String getMenzilNovAdi(int menzilNov) {
        switch (menzilNov) {
            case 1: return "Mənzil";
            case 2: return "Həyət evi (Villa)";
            case 3: return "Obyekt";
            case 4: return "Ofis";
            default: return null;
        }
    }
    
}
