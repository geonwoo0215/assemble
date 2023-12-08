package com.geonwoo.assemble.global.config;

import com.geonwoo.assemble.global.auth.PrincipalDetails;
import com.geonwoo.assemble.global.auth.jwt.JwtFilter;
import com.geonwoo.assemble.global.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/partys/**").authenticated()
                        .requestMatchers("/partyMembers/**").authenticated()
                        .requestMatchers("/expense/**").authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form
                        .loginPage("/member/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .failureForwardUrl("/member/login")
                        .successHandler((request, response, authentication) -> {
                            PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
                            String token = jwtTokenProvider.createToken(userDetails.getUserId(), userDetails.getMemberRole());
                            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                        }))
                .logout(logout -> logout
                        .permitAll()
                        .logoutUrl("/member/logout"))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
