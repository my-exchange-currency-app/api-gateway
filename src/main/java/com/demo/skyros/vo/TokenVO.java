package com.demo.skyros.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenVO {

    private String userName;
    private String accessToken;
    private String refreshToken;

    public TokenVO(String accessToken) {
        this.accessToken = accessToken;
    }
}
