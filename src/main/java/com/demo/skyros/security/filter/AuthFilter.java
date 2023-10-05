package com.demo.skyros.security.filter;

import com.demo.skyros.security.exception.UnauthorizedUserException;
import com.demo.skyros.security.service.AuthService;
import com.demo.skyros.security.service.JwtTokenUtil;
import com.demo.skyros.vo.AppUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtTokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if (jwtTokenHeader != null && securityContext.getAuthentication() == null) {
            String jwtToken = jwtTokenHeader.substring("Bearer ".length());
            if (tokenUtil.validateToken(jwtToken, request)) {
                String userName = tokenUtil.getUserNameFromToken(jwtToken);
                if (userName != null) {
                    AppUserDetails userDetails = (AppUserDetails) authService.loadUserByUsername(userName);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new UnauthorizedUserException("invalid username");
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    protected boolean isSwaggerUrl(String url) {
        if (url.contains("swagger")
                || url.contains("api-docs")
                || url.contains("configuration/ui")
                || url.contains("webjars/")
                || url.contains("swagger-resources")
                || url.contains("configuration/security")
                || url.contains("actuator")) {
            return true;
        } else {
            return false;
        }
    }
}
