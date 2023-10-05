package com.demo.skyros.security.service;


import com.demo.skyros.proxy.UserProxy;
import com.demo.skyros.security.exception.ForceChangePasswordException;
import com.demo.skyros.security.exception.InvalidCredentialsException;
import com.demo.skyros.security.exception.TokenExpiredException;
import com.demo.skyros.security.exception.UserNotFoundException;
import com.demo.skyros.vo.*;
import com.demo.skyros.vo.enums.LoginStatusEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
@Setter
@Getter
public class AuthService implements UserDetailsService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserProxy userProxy;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppResponse appResponse = getUserProxy().findUserEmail(username);
        AppUserVO userVO = (AppUserVO) appResponse.getData();
        AppUserDetails appUserDetails = new AppUserDetails(userVO);
        return appUserDetails;
    }

    public AppResponse validateToken(TokenVO tokenVO) {
        try {
            String accessToken = tokenVO.getAccessToken();
            boolean validateToken = getJwtTokenUtil().validateToken(accessToken);
            return prepareAppResponse(validateToken, null);
        } catch (Exception ex) {
            return prepareAppResponse(false, null);
        }
    }

    public AppResponse login(LoginRequestVO requestVO) {
        String userName = requestVO.getUserName();
        Authentication authentication = authenticateUser(requestVO);
        AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
        LoginStatusEnum loginStatusEnum = appUserDetails.getLoginStatusEnum();
        TokenVO responseVO = new TokenVO();
        switch (loginStatusEnum) {
            case ACTIVE:
                SecurityContextHolder.getContext().setAuthentication(authentication);
                responseVO = generateUserTokens(userName, appUserDetails.getId());
                break;
            case FORCE_CHANGE_PASSWORD:
                throw new ForceChangePasswordException("must change your password");
        }
        return prepareAppResponse(responseVO, null);
    }

    private Authentication authenticateUser(LoginRequestVO requestVO) {
        try {
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(requestVO.getUserName(), requestVO.getPassword()));
        } catch (Exception ex) {
            log.error(ex);
            throw new InvalidCredentialsException("invalid username or password");
        }
    }

    private AppResponse prepareAppResponse(Object data, String message) {
        AppResponse appResponse = new AppResponse(message);
        appResponse.setData(data);
        appResponse.setResponseDate(new Date());
        appResponse.setHttpStatus(HttpStatus.OK);
        return appResponse;
    }

    public TokenVO generateUserTokens(String userName, Long userId) {
        String accessTokenId = UUID.randomUUID().toString();
        String accessToken = getJwtTokenUtil().generateToken(userName, accessTokenId, false);
        String refreshTokenId = UUID.randomUUID().toString();
        String refreshToken = getJwtTokenUtil().generateToken(userName, refreshTokenId, true);
        TokenVO responseVO = new TokenVO();
        responseVO.setAccessToken(accessToken);
        responseVO.setRefreshToken(refreshToken);
        responseVO.setUserName(userName);
        return responseVO;
    }

    public TokenVO refreshAccessToken(TokenVO tokenVO) {
        String refreshToken = tokenVO.getRefreshToken();
        if (getJwtTokenUtil().isTokenExpired(refreshToken)) {
            throw new TokenExpiredException("refresh");
        }
        String userName = getJwtTokenUtil().getUserNameFromToken(refreshToken);
        String accessToken = getJwtTokenUtil().generateToken(userName, UUID.randomUUID().toString(), false);
        TokenVO responseVO = new TokenVO(userName, accessToken, refreshToken);
        return responseVO;

    }

}

