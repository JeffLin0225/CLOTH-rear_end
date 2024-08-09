//package com.cloth.config;
//
//import com.cloth.filter.JwtRequestFilter;
//import com.cloth.service.EmployeeService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final JwtRequestFilter jwtRequestFilter;
//    private final EmployeeService employeeService;
//
//    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter, EmployeeService employeeService) {
//        this.jwtRequestFilter = jwtRequestFilter;
//        this.employeeService = employeeService;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(employeeService).passwordEncoder(passwordEncoder());
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//                .antMatchers("/authenticate", "/public/**").permitAll() // 不需要认证的路径
//                .antMatchers("/admin/**").hasRole("ADMIN") // 需要ADMIN角色
//                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") // 需要USER或ADMIN角色
//                .anyRequest().authenticated() // 其他所有请求需要认证
//            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}
//
//
