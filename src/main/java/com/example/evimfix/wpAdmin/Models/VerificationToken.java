package com.example.evimfix.wpAdmin.Models;

import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VerificationToken {
    private int id;
    
    private String token;

    private Timestamp timestamp;

    private Date expiredAt;
  
    private int userId;

    private String username;

    public boolean isExpired() {
        return getExpiredAt().before(new Date());
    }
}
