package com.cloth.Util;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	 private final JsonWebTokenUtility jwtUtility;
	    private final UserDetailsService userDetailsService;

	    public JwtAuthorizationFilter(JsonWebTokenUtility jwtUtility, UserDetailsService userDetailsService) {
	        this.jwtUtility = jwtUtility;
	        this.userDetailsService = userDetailsService;
	    }

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
	        String authHeader = request.getHeader("Authorization");
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            String[] claims = jwtUtility.validateToken(token);
	            if (claims != null) {
	                String username = claims[0];
	                String authority = claims[1];
	                try {
	                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	                    Collection<? extends GrantedAuthority> authorities = 
	                    		Collections.singletonList(new SimpleGrantedAuthority(authority));

	                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
	                            userDetails, 
	                            null, 
	                            authorities
//	                            Collections.singletonList(new SimpleGrantedAuthority(authority))
	                    );
	                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	                    System.out.println("Authentication successful for user: " + username);
	                    System.out.println("Authentication successful for authority: " + authority);
	                    System.out.println("Authentication successful for authenticationToken: " + authenticationToken);
	                    System.out.println("Authentication successful for authorities: " + authorities);

	                } catch (UsernameNotFoundException e) {
	                   System.out.println("找不到使用者喔!");
	                    e.printStackTrace();
	                }
	            }
	        }
	        chain.doFilter(request, response);
	    }
}
