package com.app.itsm.security.jwt;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.itsm.security.UserDetailsImpl;
import com.app.itsm.security.jwt.dto.UserCredentialsDTO;
import com.app.itsm.security.jwt.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super();

        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserCredentialsDTO uc = new ObjectMapper().readValue(request.getInputStream(), UserCredentialsDTO.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    uc.getEmail(), uc.getPassword(), new ArrayList<>());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        response.setHeader("access-control-expose-headers", "Authorization");
        response.setHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        Integer status = 401;

        response.setStatus(status);
        response.setContentType("application/json");

        JSONObject obj = new JSONObject();
        obj.put("status", status);
        obj.put("error", "Unauthorized");
        obj.put("message", "Unauthorized");
        obj.put("path", "/auth/login");

        response.getWriter().append(obj.toString());
    }
}
