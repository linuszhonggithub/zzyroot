package com.zzyboot.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "zzymail", fallback = ZzyMailServiceHystric.class)
public interface ZzyMailService {
	@PostMapping(value = "/mail")
	String mail(@RequestBody String param);
}
