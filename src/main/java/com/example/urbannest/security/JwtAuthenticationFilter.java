package com.example.urbannest.security;

import com.example.urbannest.model.User;
import com.example.urbannest.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extract the Authorization header from the incoming request
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // 2. Guard Clause: Check if the token format matches standard Bearer schemas
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the clean token string (Everything after "Bearer ")
        jwtToken = authHeader.substring(7);
        userEmail = jwtUtils.extractEmail(jwtToken);

        // 4. If an email was found and the user isn't already logged in for this request
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Look up our database record to attach valid role permissions
            userService.getUserByEmail(userEmail).ifPresent(userEntity -> {

                // If token is validated successfully against database info
                if (jwtUtils.validateToken(jwtToken, userEntity.getEmail())) {

                    // Wrap our core domain user into the security user implementation
                    SecurityUser securityUser = new SecurityUser(userEntity);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            securityUser,
                            null,
                            securityUser.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Inject this verified token packet into the global Security Engine
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            });
        }

        // 5. Forward the execution onto the next filter pipeline chain segment
        filterChain.doFilter(request, response);
    }
}