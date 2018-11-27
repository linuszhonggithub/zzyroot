package com.zzyboot.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "zzyusercenter", fallback = ZzyUserCenterServiceHystric.class)
public interface ZzyUserCenterService {
	@PostMapping(value = "/usercheck")
	String usercheck(@RequestBody String param);
	@PostMapping(value = "/changepassword")
	String changepassword(@RequestBody String param);
	@PostMapping(value = "/gettabledata")
	String gettabledata(@RequestBody String param);
	@PostMapping(value = "/save")
	String save(@RequestBody String param);
}
