package com.example.evimfix.wpAdmin.Repositories;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.evimfix.wpAdmin.Models.AlqiSatqi;

public interface AlqiSatqiRepository {
    List<AlqiSatqi> getAllAlqiSatqi();
    List<AlqiSatqi> getAllAlqiSatqiByCategories(Integer sheher, Integer rayon, Integer metro, Double minQiymet, Double maxQiymet, Integer otaqSayi, Integer emlakNovu, Integer alishKiraye);

    Optional<AlqiSatqi> getAlqiSatqiById(int id);
    
    String addAlqiSatqi(AlqiSatqi alqiSatqi);
    int updateAlqiSatqi(AlqiSatqi alqiSatqi) throws ParseException;
    String deleteAlqiSatqi(int id);
    String deleteAlqiSatqiPhotos(int id);
    String addPhoto(String photoPath, int alqiSatqiId);
    String[] getAlqiSatqiPhotos(int alqiSatqiId);

    // DBden ehtiyacim olan melumatlar
    HashMap<Integer, String> getEmlakNovleri();
    HashMap<Integer, String> getAlishKiraye();
    HashMap<Integer, String> getSheherler();
    HashMap<Integer, String> getRayonlar(); // Baki uzre
    HashMap<Integer, String> getMetrolar(); // Baki uzre
}
