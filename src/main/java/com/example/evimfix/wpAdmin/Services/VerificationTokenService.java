package com.example.evimfix.wpAdmin.Services;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import com.example.evimfix.wpAdmin.Models.VerificationToken;
import com.example.evimfix.wpAdmin.Repositories.VerificationTokenRepository;

@Service
public class VerificationTokenService {

    private static BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(12);

    @Value("2800")
    private int tokenValidityInSeconds;

    @Autowired
    private VerificationTokenRepository secureTokenRepository;

    public VerificationToken createToken() {
        //String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()));
        byte[] tokenBytes = DEFAULT_TOKEN_GENERATOR.generateKey();
        String tokenValue = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
        //String tokenValue = new String(DEFAULT_TOKEN_GENERATOR.generateKey(), StandardCharsets.UTF_8);   
        VerificationToken secureToken = new VerificationToken();
        secureToken.setToken(tokenValue);

        System.out.println(tokenValue.length());
        System.out.println(tokenValue);
        
        Date now = new Date();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        
        calendar.add(Calendar.SECOND, tokenValidityInSeconds);
        
        Date updatedTime = calendar.getTime();

        secureToken.setExpiredAt(updatedTime);
        //this.addSecureToken(secureToken);
        return secureToken;
    }

    public String addSecureToken(VerificationToken secureToken) {
        return secureTokenRepository.addToken(secureToken);
    }

    public VerificationToken findByToken(String token) {
        return secureTokenRepository.findByToken(token).get() == null 
        ? null 
        : secureTokenRepository.findByToken(token).get();
    }

    public String removeToken(VerificationToken token) {
        return secureTokenRepository.deleteToken(token);
    }
}
