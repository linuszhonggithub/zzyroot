package com.zzyboot.service;

import org.springframework.stereotype.Component;

import com.zzyboot.common.util.ZzyCommon;
@Component
public class ZzyMailServiceHystric implements ZzyMailService {

	@Override
	public String mail(String param) {
		// TODO Auto-generated method stub
		return ZzyCommon.ZZYFAIL_CANOTACCESS + " Mail Server";
	}

}
