package com.example.evimfix.wpAdmin.Models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlqiSatqi {
    @Id
    private int id;

    @NotBlank(message = "Başlıq boş ola bilməz.")
    @Size(min = 3, max = 100, message = "Başlıq min. 3 simvoldan ibarət olmalıdır.")
    private String bashliq;

    @Min(value = 0, message = "Qiymət 0-dan kiçik ola bilməz.")
    private double qiymet;

    @NotBlank(message = "Haqqında bölməsi boş ola bilməz.")
    @Size(min = 3, max = 4000, message = "Haqqında bölməsi min. 10 simvoldan ibarət olmalıdır.")
    private String haqqinda;

    @NotNull(message = "Mənzil növü boş ola bilməz.")
    @Min(value = 1, message = "Mənzil növü boş ola bilməz.")
    @Max(value = 5, message = "Mənzil növü 1-5 arasında olmalıdır.")
    private int menzilNov;

    private String menzilNovAdi; // Buna ui tesir etmir deye validation yoxdu

    @Min(value = 1, message = "Otaq sayı 1-dən kiçik ola bilməz.")
    @Max(value = 20, message = "Otaq sayı 20-dan çox ola bilməz.")
    private Integer otaqSayi;

    @Min(value = 1, message = "Sahə 1-dən kiçik ola bilməz.")
    private double sahe;

    @Min(value = 1, message = "Mərtəbə 1-dən kiçik ola bilməz.")
    private Integer mertebe = 0;

    @Min(value = 1, message = "Mərtəbə sayı 1-dən kiçik ola bilməz.")
    private Integer mertebeSayi = 0;

    @NotBlank(message = "Təmiri boş ola bilməz.")
    private int temir;
    
    private String temirAdi;

    @NotBlank(message = "Çıxarış boş ola bilməz.")
    private int chixarish;

    private String chixarishAdi;

    @NotBlank(message = "Şəhər boş ola bilməz.")
    private int sheher;
    
    private String sheherAdi;

    @NotBlank(message = "Ünvan boş ola bilməz.")
    private String unvan;

    @Min(value = 1, message = "Mərtəbə sayı 1-dən kiçik ola bilməz.")
    private Integer metro;

    @NotBlank(message = "Metro boş ola bilməz.")
    private String metroAdi; // Buna ui tesir etmir deye validation yoxdu

    @NotBlank(message = "Sahibi boş ola bilməz.")
    private String sahibi;

    @NotBlank(message = "Telefon boş ola bilməz.")
    @Size(min = 13, max = 13, message = "Telefon nömrəsi 13 simvoldan ibarət olmalıdır.")
    private String telefon;

    private Integer status;

    private int kirayeSatish;
    private String kirayeSatishAdi;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date addUpdTarix;

    private Integer tikiliNovId;
    private String tikiliNovAdi;

    private MultipartFile[] photos;

    private String[] photoPaths; // Foto yolları üçün əlavə sahə
}
