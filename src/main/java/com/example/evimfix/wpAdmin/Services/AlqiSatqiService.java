package com.example.evimfix.wpAdmin.Services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.example.evimfix.wpAdmin.Repositories.Helper.HelperRepository;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class AlqiSatqiService {
    @Autowired
    private AlqiSatqiRepository alqiSatqiRepository;

    @Autowired
    private HelperRepository helperRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String addPhotos(MultipartFile file, int alqiSatqiId) {
        try {
            Path uploadPath = Paths.get(uploadDir);
        
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            byte[] compressedImage = compressImage(file.getBytes());
            
            // Generate unique filename
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path targetLocation = uploadPath.resolve(uniqueFileName);
            //Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Files.write(targetLocation, compressedImage);
            
            return alqiSatqiRepository.addPhoto(uniqueFileName, alqiSatqiId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public String deleteAlqiSatqi(int id) {
        return alqiSatqiRepository.deleteAlqiSatqi(id);
    }

    public List<AlqiSatqi> getAllAlqiSatqi() {
        List<AlqiSatqi> alqiSatqiList = alqiSatqiRepository.getAllAlqiSatqi();
        
        return alqiSatqiList;
    }

    public Optional<AlqiSatqi> getAlqiSatqiById(int id) {
        Optional<AlqiSatqi> alqiSatqi = alqiSatqiRepository.getAlqiSatqiById(id);

        if (alqiSatqi.isPresent()) {
            // AlqiSatqi obyektinin fotosunu al
            String[] photos = helperRepository.getAlqiSatqiPhotos(id);
            alqiSatqi.get().setPhotoPaths(photos);
            return alqiSatqi;            
        }

        return alqiSatqi;
    }    

    public String deleteAlqiSatqiPhotos(int id) {
        return alqiSatqiRepository.deleteAlqiSatqiPhotos(id);
    }

    public String updateAlqiSatqi(AlqiSatqi alqiSatqi) throws ParseException {
        //System.out.println(alqiSatqi.getPhotos()[0].getSize());
        if (alqiSatqi.getPhotos()[0].getSize() > 0 && alqiSatqi.getPhotos().length > 0) {
            int alqiSatqiId = alqiSatqi.getId();

            // Kohne fotolari yer tutmasin deye folderden silirik
            String[] existingPhotos = alqiSatqiRepository.getAlqiSatqiPhotos(alqiSatqiId);
            for (String photo : existingPhotos) {
                try {
                    deleteFile(photo);
                } catch (FileNotFoundException e) {
                    // Foto tapilmadi, davam et
                    System.out.println("Foto tapilmadi: " + photo);
                } catch (IOException e) {
                    // Foto silinende problem yarandi
                    return "Foto silinende problem yarandi: " + e.getMessage();
                }
            }

            deleteAlqiSatqiPhotos(alqiSatqiId); // Db den fotoları silirik            

            for (MultipartFile path : alqiSatqi.getPhotos()) {
                if (!path.isEmpty()) {
                    String result = addPhotos(path, alqiSatqiId);
                    if (!result.equals("OK")) {
                        return result; // Əgər foto əlavə edilmədisə, xətanı qaytar
                    }
                }
            }
        }        
        
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
    
    // DBden ehtiyacim olan melumatlar
    public HashMap<Integer, String> getEmlakNovleri() {
        return alqiSatqiRepository.getEmlakNovleri();
    }

    public HashMap<Integer, String> getAlishKirayeNovler() {
        return alqiSatqiRepository.getAlishKiraye();
    }

    public HashMap<Integer, String> getSheherler() {
        return alqiSatqiRepository.getSheherler();
    }

    public HashMap<Integer, String> getRayonlar(){
        return alqiSatqiRepository.getRayonlar();
    }

    public HashMap<Integer, String> getMetrolar() {
        return alqiSatqiRepository.getMetrolar();
    }

    public List<AlqiSatqi> getAllAlqiSatqiByCategories(Integer sheher, Integer rayon, Integer metro, Double minQiymet, Double maxQiymet, Integer otaqSayi, Integer emlakNovu, Integer alishKiraye) {
        return alqiSatqiRepository.getAllAlqiSatqiByCategories(sheher, rayon, metro, minQiymet, maxQiymet, otaqSayi, emlakNovu, alishKiraye);
    }

    // Helper metodlar
    public byte[] compressImage(byte[] imageBytes) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(new ByteArrayInputStream(imageBytes))
            .size(1024, 1024) // Set max width/height
            .outputFormat("jpg") // Convert to JPEG which is smaller than PNG
            .outputQuality(0.7) // Quality ratio (0.0-1.0)
            .toOutputStream(outputStream);
        return outputStream.toByteArray();
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
        
        try {
            Files.delete(filePath);
        } catch (NoSuchFileException e) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }
}
