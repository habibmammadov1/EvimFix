package com.example.evimfix.wpAdmin.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.evimfix.wpAdmin.Models.AlqiSatqi;
import com.example.evimfix.wpAdmin.Repositories.AlqiSatqiRepository;

@Service
public class AlqiSatqiService {
    @Autowired
    private AlqiSatqiRepository alqiSatqiRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String addPhotos(MultipartFile file, int alqiSatqiId) {
        try {
            Path uploadPath = Paths.get(uploadDir);
        
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path targetLocation = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return alqiSatqiRepository.addPhoto(uniqueFileName, alqiSatqiId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public String deleteAlqiSatqi(int id) {
        return alqiSatqiRepository.deleteAlqiSatqi(id);
    }

    public List<AlqiSatqi> getAllAlqiSatqi() {
        return alqiSatqiRepository.getAllAlqiSatqi();
    }

    public Optional<AlqiSatqi> getAlqiSatqiById(int id) {
        Optional<AlqiSatqi> alqiSatqi = alqiSatqiRepository.getAlqiSatqiById(id);

        if (alqiSatqi.isPresent()) {
            // AlqiSatqi obyektinin fotosunu al
            String[] photos = alqiSatqiRepository.getAlqiSatqiPhotos(id);
            alqiSatqi.get().setPhotoPaths(photos);
            return alqiSatqi;
            
        }

        return alqiSatqi;
    }

    // DBden ehtiyacim olan melumatlar
    public HashMap<Integer, String> getEmlakNovleri() {
        return alqiSatqiRepository.getEmlakNovleri();
    }

    public HashMap<Integer, String> getAlishKirayeNovler() {
        return alqiSatqiRepository.getAlishKiraye();
    }

    public String updateAlqiSatqi(AlqiSatqi alqiSatqi) throws ParseException {
        return alqiSatqiRepository.updateAlqiSatqi(alqiSatqi) == 1 ? "OK" : "Yeniləmə uğursuz oldu";
    }

    public String addAlqiSatqi(AlqiSatqi alqiSatqi) {
        String id_result = alqiSatqiRepository.addAlqiSatqi(alqiSatqi);

        for (MultipartFile path : alqiSatqi.getPhotos()) {
            if (!path.isEmpty()) {
                String result = addPhotos(path, Integer.parseInt(id_result));
                if (!result.equals("OK")) {
                    return result; // Əgər foto əlavə edilmədisə, xətanı qaytar
                }
            }
            
        }
        return !id_result.contains("ORA") ? "OK" : "Problem baş verdi, yenidən yoxlayın";
    }
}
