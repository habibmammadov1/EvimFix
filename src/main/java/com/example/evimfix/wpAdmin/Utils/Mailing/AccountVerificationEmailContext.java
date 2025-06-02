package com.example.evimfix.wpAdmin.Utils.Mailing;

import org.springframework.web.util.UriComponentsBuilder;

import com.example.evimfix.wpAdmin.Models.Auth;

import lombok.Getter;

@Getter
public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;

    @Override
    public <T> void init(T context) {
        Auth user = (Auth) context;

        put("firstName", user.getFirstName());
        setTemplateLocation("mailing/email-verification");
        setSubject("Complete Your Registration");
        setFrom("habibmammadov0@gmail.com");
        setTo(user.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token) {
        final String url1 = UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/wp-admin/verify").queryParam("token", token).toUriString();

        final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
        .path("/wp-admin/verify").toUriString() + "?token=" + token;
        put("verificationURL", url);
    }

}
