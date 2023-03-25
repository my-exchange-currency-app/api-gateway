package com.demo.skyros.proxy;

import com.demo.skyros.vo.CurrencyVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "client-requests-service")
public interface ClientRequestProxy {

    @PostMapping("save-client-request/{requestId}")
    void saveClientRequest(@PathVariable String requestId, @RequestBody CurrencyVO vo);

}
