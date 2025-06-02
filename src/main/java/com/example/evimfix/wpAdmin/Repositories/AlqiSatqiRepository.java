package com.example.evimfix.wpAdmin.Repositories;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.evimfix.wpAdmin.Models.AlqiSatqi;

public interface AlqiSatqiRepository {
    List<AlqiSatqi> getAllAlqiSatqi();
    Optional<AlqiSatqi> getAlqiSatqiById(int id);
    String[] getAlqiSatqiPhotos(int alqiSatqiId);
    String addAlqiSatqi(AlqiSatqi alqiSatqi);
    int updateAlqiSatqi(AlqiSatqi alqiSatqi) throws ParseException;
    String deleteAlqiSatqi(int id);
    String addPhoto(String photoPath, int alqiSatqiId);

    // DBden ehtiyacim olan melumatlar
    HashMap<Integer, String> getEmlakNovleri();
    HashMap<Integer, String> getAlishKiraye();
}
