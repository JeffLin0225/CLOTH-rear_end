package com.cloth.Util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JsonWebTokenUtility jwtUtility;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JsonWebTokenUtility jwtUtility, UserDetailsService userDetailsService) {
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtility);
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtility, userDetailsService);

        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
            		                

                .requestMatchers("/backstage/secure/login").permitAll()
                .requestMatchers("/backstage/activity/delete/**").hasAuthority("Manager")                
                .requestMatchers("/backstage/coupon/delete/**").hasAuthority("Manager")                
                .requestMatchers("/backstage/product/delete/**").hasAuthority("Manager")                

                .requestMatchers("/backstage/**").permitAll()
                .requestMatchers("/ws/chat/**").permitAll()
                .anyRequest().authenticated()	
            )
            .addFilter(jwtAuthenticationFilter)
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/**")
    //                 .allowedOrigins("http://localhost:5173","http://localhost:5174") // 前端域名
    //                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //                 .allowCredentials(true);
    //         }
    //     };
    // }
        
    
}
