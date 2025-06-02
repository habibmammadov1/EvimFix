package com.example.evimfix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.evimfix.config.Helpers.CustomLoginSuccessHandler;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    private static final String[] WHITELIST = {
        "/**",
        "/login", 
        "/resources/**",
        "/css/**",
        "/fonts/**",
        "/img/**",
        "/js/**"
    };

    private static final String[] AUTHLIST = {
        "/wp-admin/**"
    };

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
            .headers(header -> header.frameOptions(header1 -> header1.sameOrigin()))
             // Disable X-Frame-Options header
            .authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests
                    .requestMatchers(WHITELIST).permitAll()
                    .requestMatchers(AUTHLIST).authenticated()
                    .requestMatchers("/new-user").hasRole("ADMIN")
                    .requestMatchers("/update-user").hasRole("ADMIN")
                    .anyRequest()
            )

            .formLogin(formLogin ->
                formLogin
                    .loginPage("/wp-admin/login")
                    .successHandler(customLoginSuccessHandler)
                    .loginProcessingUrl("/login") // Correct: Specifies the URL to submit the login form.
                    .usernameParameter("username") // Correct: Customizes the username parameter, assuming you're using 'email' instead of the default 'username'.
                    .passwordParameter("password") // Correct: Specifies the form field for the password.
                    .defaultSuccessUrl("/wp-admin/index", true) // Correct: Redirects to a specific page after successful login.
                    .failureUrl("/wp-admin/login?error") // Correct: Specifies where to redirect after login failure.
                    .permitAll()
            )

            .logout(logout ->
                logout
                .logoutUrl("/wp-admin/logout") // Specifies the URL for logout
                .logoutSuccessUrl("/wp-admin/login") // Redirects to this URL after successful logout
                .deleteCookies("JSESSIONID") // Delete the session cookie
                .invalidateHttpSession(true) // Invalidate the HTTP session      
                .permitAll()
            )  

            .rememberMe(
                rememberMe -> rememberMe.rememberMeParameter("remember-me")
            )

            .csrf(csrf -> csrf.disable())
            .headers(header -> header.frameOptions(header1 -> header1.disable()));

            //http.sessionManagement(httpSecuritySessionManagementConfigurer -> 
            //    httpSecuritySessionManagementConfigurer
            //    .expiredUrl("/")
            //    .invalidSessionUrl("/invalidSession")
            //);
            
        return http.build();
    }

    //@Bean
    //public HttpSessionEventPublisher httpSessionEventPublisher() {
    //    return new HttpSessionEventPublisher();
    //}   
}
