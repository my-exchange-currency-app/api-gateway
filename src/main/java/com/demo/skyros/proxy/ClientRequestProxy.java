package com.demo.skyros.proxy;

import com.demo.skyros.vo.RequestVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "client-requests-service")
public interface ClientRequestProxy {

    @PostMapping("save-client-request")
    void saveClientRequest(@RequestBody RequestVO vo);

}
