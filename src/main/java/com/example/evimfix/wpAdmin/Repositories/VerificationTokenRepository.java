package com.example.evimfix.wpAdmin.Repositories;

import java.util.Optional;

import com.example.evimfix.wpAdmin.Models.VerificationToken;

public interface VerificationTokenRepository {
    Optional<VerificationToken> findByToken(String token);
    String deleteToken(VerificationToken token);
    String addToken(VerificationToken token);
}
