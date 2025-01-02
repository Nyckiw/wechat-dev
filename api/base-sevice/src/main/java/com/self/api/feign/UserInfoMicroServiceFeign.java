package com.self.api.feign;

import com.self.grace.result.GraceJSONResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jcy
 * @version 1.0
 * @data 2025/1/2
 */
@FeignClient(value = "main-service")//提供接口的服务
public interface UserInfoMicroServiceFeign {
    @PostMapping("/userinfo/updateFace")//全路径
    public GraceJSONResult updateFace(@RequestParam String userId, @RequestParam String face);
}
