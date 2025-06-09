package com.example.evimfix.wpAdmin.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.evimfix.wpAdmin.Models.Auth;
import com.example.evimfix.wpAdmin.Models.Role;
import com.example.evimfix.wpAdmin.Models.VerificationToken;
import com.example.evimfix.wpAdmin.Repositories.AuthRepository;
import com.example.evimfix.wpAdmin.Utils.Exceptions.InvalidTokenException;
import com.example.evimfix.wpAdmin.Utils.Mailing.AccountVerificationEmailContext;
import com.example.evimfix.wpAdmin.Utils.Mailing.EmailService;

import org.apache.commons.lang3.StringUtils;

import jakarta.mail.MessagingException;

@Service
public class AuthService implements UserDetailsService {

    @Value("${site.base.url.https}")
    private String baseUrl;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Auth> userOptional = authRepository.getUser(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        Auth user = userOptional.get();
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(user.getRole()));
        return new User(
            user.getUsername(), 
            user.getPassword(), 
            user.isEnabled(),
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            grantedAuthority
        );
    }

    public List<Role> getRollar(){
        return authRepository.getRollar();
    }

    public String addUser(Auth auth){
        auth.setPassword(passwordEncoder.encode(auth.getPassword()));
        
        String result = authRepository.addUser(auth);
        sendRegistrationConfirmationEmail(auth);
        return result.equals("1") ? "OK" : result;
    }

    public Optional<Auth> getUserByUsername(String username){
        return authRepository.getUser(username);
    }

    public Optional<Auth> getUserById(int id){
        return authRepository.getUser(id);
    }

    public String updateProfile(Auth auth){
        if (auth.getPassword() != null && auth.getPassword() != "") {
            auth.setPassword(passwordEncoder.encode(auth.getPassword()));
        }
        
        return authRepository.updateProfile(auth);
    }

    public String updateUser(Auth auth){
        if (auth.getPassword() != null && auth.getPassword() != "") {
            auth.setPassword(passwordEncoder.encode(auth.getPassword()));
        }
        
        return authRepository.updateUser(auth);
    }

    public List<Auth> getUsers(){
        return authRepository.getUsers();
    }

    public String deleteUser(String username){
        return authRepository.deleteUser(username);
    }

    public void sendRegistrationConfirmationEmail(Auth user) {
        VerificationToken secureToken = verificationTokenService.createToken();
        secureToken.setUsername(user.getUsername());
        verificationTokenService.addSecureToken(secureToken);

        AccountVerificationEmailContext context = new AccountVerificationEmailContext();
        context.init(user);
        context.setToken(secureToken.getToken());
        context.buildVerificationUrl(baseUrl, secureToken.getToken());

        try {
            emailService.sendMail(context);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyUser(String token) throws InvalidTokenException {
        VerificationToken secureToken = verificationTokenService.findByToken(token);
        if (Objects.isNull(token) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()) {
            throw new InvalidTokenException("Token is not valid");
        }

        Optional<Auth> user = authRepository.getUser(secureToken.getUserId());
        if (Objects.isNull(user.get())) {
            return false;
        }

        user.get().setEnabled(true);
        authRepository.updateUser(user.get());

        verificationTokenService.removeToken(secureToken);
        return true;
    }

    public HashMap<Integer, String> getUserTest() {
        return authRepository.getUserTest();
    }
}