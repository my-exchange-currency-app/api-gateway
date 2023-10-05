package com.demo.skyros.controller;

import com.demo.skyros.security.service.AuthService;
import com.demo.skyros.vo.AppResponse;
import com.demo.skyros.vo.LoginRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public AppResponse exchangeCurrency(@RequestBody LoginRequestVO vo) {
        return authService.login(vo);
    }

}
