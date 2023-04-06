package com.demo.skyros.proxy;

import com.demo.skyros.vo.AppResponse;
import com.demo.skyros.vo.TokenVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "credential-service", url = "http://127.0.0.1:7777/auth")
public interface AuthProxy {

    @PostMapping("validate-token")
    AppResponse validateToken(@RequestBody TokenVO tokenVO);

}
