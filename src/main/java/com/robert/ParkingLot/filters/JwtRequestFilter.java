package com.robert.ParkingLot.filters;

import com.robert.ParkingLot.authentication.AuthenticationService;
import com.robert.ParkingLot.authentication.UserDetails;
import com.robert.ParkingLot.structures.User;
import com.robert.ParkingLot.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Request-ul de preflight are un request method de tip "OPTIONS". Vrem ca acesta sa nu fie interceptat de filtru
        if(Objects.equals(request.getRequestURI(), "/login") || Objects.equals(request.getMethod(), "OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        String jwt = null;
        String authorizationHeader = request.getHeader("Authorization");
        final String authorizationHeaderPrefix = "Bearer ";

        if(authorizationHeader != null && authorizationHeader.startsWith(authorizationHeaderPrefix)) {
            jwt = authorizationHeader.substring(authorizationHeaderPrefix.length());
            username = jwtUtil.getUsernameFromToken(jwt);

            User user = authenticationService.getUserByUsername(username);
            if(jwtUtil.validateToken(jwt, user)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        response.sendError(HttpServletResponse.SC_CONFLICT, "Unauthorized! (Blocked by JWT filter)");
    }
}
