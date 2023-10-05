package com.demo.skyros.security.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
@Log4j2
public class UnauthorizedResponse implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = 2848589597094595376L;

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException, ServletException {
        log.error(ex);
        final String expired = (String) req.getAttribute("expired");
        log.info("expired:" + expired);
        if (expired != null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, expired);
        } else {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You would need to provide the Jwt token to access this resource");
        }
    }

}
