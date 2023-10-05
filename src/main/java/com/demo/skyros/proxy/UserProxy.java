package com.demo.skyros.proxy;

import com.demo.skyros.vo.AppResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/user")
public interface UserProxy {

    @GetMapping("email/{email}")
    AppResponse findUserEmail(@PathVariable String email);

}
