package com.zzyboot;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.service.ZzyUserCenterService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {
	@Value("${zzyprime}")
	private int zzyprime;
	@Value("${zzydhbase}")
	private int zzydhbase;
	@Autowired
	ZzyUserCenterService zzyUserCenterService;
	@Autowired
	private TestRestTemplate restTemplate;
	
	private String url = "http://localhost:8085";
	
	@Test
	public void getSessionkey(){
		MultiValueMap<String, Integer> map= new LinkedMultiValueMap<String, Integer>();
		map.add("param", 14559535); 
		//HttpHeaders headers = new HttpHeaders(); 
		HttpEntity<MultiValueMap<String, Integer>> request = new HttpEntity<MultiValueMap<String, Integer>>(map);//, headers);
		
		
		String rtn = restTemplate.postForObject(url + "/getsessionkey", request,String.class);
		System.out.println("rtn is " + rtn);
		assertTrue(rtn.indexOf(ZzyCommon.ZZYSUCCESS) == 0);
		
	}
}
