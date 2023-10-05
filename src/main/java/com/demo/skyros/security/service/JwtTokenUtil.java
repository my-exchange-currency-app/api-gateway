package com.demo.skyros.security.service;

import com.demo.skyros.vo.AppUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Log4j2
@Component
public class JwtTokenUtil {

    private static String TOKEN_SECRET_KEY;
    @Value("${secret.issuer}")
    private static String issuer;
    private static Long ACCESS_TOKEN_VALIDITY;
    private static Long REFRESH_TOKEN_VALIDITY;

    public JwtTokenUtil(@Value("${secret.key}") String secretKey,
                        @Value("${token.expiration.access}") Long accessValidity,
                        @Value("${token.expiration.refresh}") Long refreshValidity) {

        Assert.notNull(accessValidity, "Validity must not be null");
        Assert.hasText(secretKey, "Validity must not be null or empty");

        TOKEN_SECRET_KEY = secretKey;
        ACCESS_TOKEN_VALIDITY = accessValidity;
        REFRESH_TOKEN_VALIDITY = refreshValidity;
    }

    public static String generateToken(final String username, final String tokenId, boolean isRefresh) {
        return Jwts.builder()
                .setId(tokenId)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setExpiration(getTokenExpirationDate(isRefresh))
                .claim("created", Calendar.getInstance().getTime())
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET_KEY).compact();
    }


    private static Date getTokenExpirationDate(boolean isRefresh) {
        return new Date(System.currentTimeMillis() + (isRefresh ? REFRESH_TOKEN_VALIDITY : ACCESS_TOKEN_VALIDITY) * 1000);
    }

    public String getUserNameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getTokenIdFromToken(String token) {
        return getClaims(token).getId();
    }

    public boolean isTokenValid(String token, AppUserDetails userDetails) {
        boolean tokenExpired = isTokenExpired(token);
        log.info("isTokenExpired : " + tokenExpired);
        String username = getUserNameFromToken(token);
        log.info("username from token : " + username);
        log.info("userDetails username : " + userDetails.getUsername());
        Boolean isUserNameEqual = username.equalsIgnoreCase(userDetails.getUsername());
        boolean isTokenValid = isUserNameEqual && !tokenExpired;
        log.info("isTokenValid : " + isTokenValid);
        return isTokenValid;
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, HttpServletRequest httpServletRequest) {
        try {
            Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.info("Invalid JWT Signature");
            //throw new SecurityException("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            log.info("Invalid JWT token");
            //httpServletRequest.setAttribute("expired", ex.getMessage());
            throw new SecurityException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.info("Expired JWT token");
            //httpServletRequest.setAttribute("expired", ex.getMessage());
            //throw new SecurityException("security.token_expired");
        } catch (UnsupportedJwtException ex) {
            log.info("Unsupported JWT exception");
            //throw new SecurityException("Unsupported JWT exception");
        } catch (IllegalArgumentException ex) {
            log.info("Jwt claims string is empty");
            //throw new SecurityException("Jwt claims string is empty");
        }
        return false;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.info("Invalid JWT Signature");
            //throw new SecurityException("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            log.info("Invalid JWT token");
            throw new SecurityException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.info("Expired JWT token");
            //throw new SecurityException("security.token_expired");
        } catch (UnsupportedJwtException ex) {
            log.info("Unsupported JWT exception");
            //throw new SecurityException("Unsupported JWT exception");
        } catch (IllegalArgumentException ex) {
            log.info("Jwt claims string is empty");
            //throw new SecurityException("Jwt claims string is empty");
        }
        return false;
    }
}
