package com.zzyboot.controller;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zzyboot.api.service.ZzyServiceImpl;
import com.zzyboot.api.util.ZzyUtil;
import com.zzyboot.common.util.DES;
import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.entity.Zzyuser;
import com.zzyboot.pojo.ZzyParam;
import com.zzyboot.pojo.ZzyResult;
import com.zzyboot.service.ZzyMailService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.zzyboot.entity.ZzyEntityParent;

@RestController
public class UserController {
	@Autowired
	ZzyServiceImpl zzyService;
	 @Autowired
	 private Environment env;
	 @Autowired
	ZzyMailService zzyMailService;
	 @Autowired
	 private StringRedisTemplate redisTemplate;
		
	 private void zzycron5(String hostname){
		 zzyService.removeLog(hostname,"Zzyuser");
	 }
	 @PostMapping("/zzycron")
		public void zzycron(@RequestBody int param,HttpRequest httpRequest){
			if(param == 5){
				String hostname = httpRequest.getURI().getHost()+":"+ httpRequest.getURI().getPort();
				zzycron5(hostname);
			}
		}

	@GetMapping("/userinfo")
	public String getUserInfo(){
		StringBuilder sb = new StringBuilder();
		List<ZzyEntityParent> users =zzyService.findAll(new Zzyuser()); 
		for(ZzyEntityParent z: users){
			System.out.println(z.toString());
			sb.append(z.toStringZzy() + ZzyCommon.STRSEPLINE);
		}
		return sb.toString();
	}
	@PostMapping("/save")
	public ZzyResult Save(@RequestBody String param){
		
		ZzyParam zp = ZzyCommon.getZzyParam(param);
		

		String param0 = zp.getData();
		int index = param0.indexOf(ZzyCommon.STRSEPBLOCK);
		String tablename = param0.substring(0,index);
		param0 = param0.substring(index + ZzyCommon.STRSEPBLOCK.length());
		String tablenamelow = tablename.toLowerCase();
		String[] objA = param0.split(ZzyCommon.STRSEPITEM);
		String[] cols = null;
		String[] values = null;
		String password = "";
		if(objA[0].length() < 1){ //add
			if(tablenamelow.equals("zzyuser")){
				cols = new String[]{"password"};
				
				
				String newuser = objA[2];
				password = ZzyCommon.bytesToHex(newuser.getBytes());
				
				password = ZzyUtil.sha256(password);
				password = password.substring(0,8);
				String newpassword = ZzyUtil.sha256(password);
				if(objA.length == 6){
					values = new String[]{null,newpassword};	
				}else if(objA.length == 7){
					values = new String[]{newpassword};
				}
				
			}
		}
		String result = "";
		result = zzyService.save(param,cols, values);
		if(objA[0].length() < 1 && password.length() > 0 && result.indexOf(ZzyCommon.ZZYSUCCESS) >= 0){
			//email to user;
			if(tablenamelow.equals("zzyuser")){
				String newuser = objA[2];
				String to = objA[3];
				String url = objA[5];
				StringBuilder parammail= new StringBuilder();
				String from = "",cc="",bcc="";
				String subject = "hi " + newuser + ", notice from ZCloud";
				String content = "Your login is " + newuser + ", password is " + password+", please change password after you login to our website " + url + ". ";
				String file = "";
				parammail.append(from + ZzyCommon.STRSEPLINE); // from is null
				parammail.append(to + ZzyCommon.STRSEPLINE); // to
				parammail.append(cc + ZzyCommon.STRSEPLINE); // cc
				parammail.append(bcc + ZzyCommon.STRSEPLINE); // bcc
				parammail.append(subject + ZzyCommon.STRSEPLINE); // subject
				parammail.append(content + ZzyCommon.STRSEPLINE); // content
				parammail.append(file + ZzyCommon.STRSEPLINE); // content
				parammail.append(zp.getUsername() + ZzyCommon.STRSEPITEM); // content
				parammail.append(zp.getToken()); // content
				zzyMailService.mail(parammail.toString());
			}
			
		}
		return getZzyResult(param,result);
		//return result;
	}

	@PostMapping("/add")
	public ZzyResult Add(@RequestBody String param){
		Zzyuser u = new Zzyuser();
		
		
		ZzyParam zp = ZzyCommon.getZzyParam(param);
		
		
		
		u.setValues(zp.getData());
		String result =  zzyService.add(u,zp.getUsername());
		return getZzyResult(param,result);
	}
	
	@PostMapping("/changepassword")
	public ZzyResult Changepassword(@RequestBody String param){
		ZzyParam zp = ZzyCommon.getZzyParam(param);
		String[] paramArray = zp.getData().split(ZzyCommon.STRSEPITEM);
		int index = 0;
		String password = paramArray[index++];
		index++;
		String passwordnew = paramArray[index++];
		index++;
		String username = zp.getUsername();
		
		Zzyuser u = (Zzyuser)zzyService.findOne(new Zzyuser(),"username" + ZzyCommon.STRSEPITEM + "password",username + ZzyCommon.STRSEPITEM +password);
		if(u == null){
			System.out.println("user, password not exists");
			return ZzyCommon.getZzyResultFalse(ZzyCommon.ZZYFAIL_USERINVALID);
		}
		u.setPassword(passwordnew);
			
		String result =  zzyService.update(u, u.getId() + ZzyCommon.STRSEPLINE + "password" + ZzyCommon.STRSEPITEM + passwordnew,username);
		if(result.indexOf(ZzyCommon.ZZYSUCCESS) == 0){
			System.out.println("password updated");
			return ZzyCommon.getZzyResultTrue(ZzyCommon.ZZYSUCCESSChangePassword);
		}
		return ZzyCommon.getZzyResultFalse(result);
	}
	@PostMapping("/delete")
	public ZzyResult Delete(@RequestBody String param){
		Zzyuser u = new Zzyuser();
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		//check if session is valid
		String sessioninfo = paramA[paramA.length - 1];
		System.out.println("sessioninfo is " + sessioninfo);
		String[] sessioninfoA = sessioninfo.split(ZzyCommon.STRSEPITEM);
		System.out.println("sessioninfoA length is " + sessioninfoA.length);
		String username = sessioninfoA[sessioninfoA.length - 2];
		
		String result =  zzyService.delete(u, param,username);
		//System.out.println("delete result is " + result);
		ZzyResult zr = getZzyResult(param,result);
		zr.setResultmsg(result);
		return zr;
	}
	@PostMapping("/update")
	public String Update(@RequestBody String param){
		/*try {
			param = URLDecoder.decode(param,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String[] paramA = param.split(ZzyCommon.STRSEPLINE);
		//check if session is valid
		String sessioninfo = paramA[paramA.length - 1];
		System.out.println("sessioninfo is " + sessioninfo);
		String[] sessioninfoA = sessioninfo.split(ZzyCommon.STRSEPITEM);
		System.out.println("sessioninfoA length is " + sessioninfoA.length);
		String username = sessioninfoA[sessioninfoA.length - 2];
		//System.out.println("add param is " + param);
		Zzyuser u = new Zzyuser();
		String result =  zzyService.update(u, param,username);
		//System.out.println("update result is " + result);
		return result;
	}
	
	@PostMapping(path="/gettabledata",produces=MediaType.APPLICATION_JSON_VALUE)
	public ZzyResult gettabledata(@RequestBody String param){
		 return getZzyResult(param,"");
		/*String supers = env.getProperty("super");
		ZzyParam zp = ZzyCommon.getZzyParam(param);
		ZzyResult zr=  ZzyUtil.gettabledata(zp,redisTemplate,zzyService,supers);
		
		return zr;*/
		
	}
	public ZzyResult getZzyResult(String param, String result){
		String supers = env.getProperty("super");
		ZzyParam zp = ZzyCommon.getZzyParam(param);
		ZzyResult zr=  ZzyUtil.gettabledata(zp,zzyService,supers);
		zr.setResultmsg(result);
		return zr;
	}

	@PostMapping(path="/usercheck",produces=MediaType.APPLICATION_JSON_VALUE)
	public ZzyResult UserCheck(@RequestBody String param){
		/*try {
			param = URLDecoder.decode(param,"UTF-8");
			System.out.println("usercheck param is " + param);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		ZzyParam zp = ZzyCommon.getZzyParam(param);
		//String[] paramArray = param.split(ZzyCommon.STRSEPITEM);
		
		String username = zp.getUsername();
		String token = zp.getToken();
		param = username + ZzyCommon.STRSEPITEM + token;
		ZzyResult zr = new ZzyResult();
		/*if(ZzyCommon.tokenvalid(zp,redisTemplate)){
			zr =  ZzyCommon.tokenvalidstr(zr,zp,redisTemplate);
			return zr;
		}*/
		Zzyuser u = null;
		String realUserName = null;
		u = (Zzyuser)zzyService.findOne(new Zzyuser(),"email",username);
		if(u != null){
			//System.out.println(u.getPassword()+" ==? " + token);
			if(u.getPassword().equals(token)){
				realUserName = u.getUsername();
			}
		}
		if(realUserName == null){
			u = (Zzyuser)zzyService.findOne(new Zzyuser(),"cell",username);
			
			if(u != null){
				if(u.getPassword().equals(token)){
					realUserName = u.getUsername();
				}
			}
		}
		if(realUserName == null){
			u = (Zzyuser)zzyService.findOne(new Zzyuser(),"username" + ZzyCommon.STRSEPITEM + "password",username + ZzyCommon.STRSEPITEM +token);
			if(u != null){
				realUserName = username;
			}
			
		}
		if(realUserName == null){
			String supers = env.getProperty("super");
			String supersu = env.getProperty("superu");
			try {
				supers=DES.decryption(supers);
				//supersu=DES.decryption(supersu);
				if(param.equals(supers)){
					u = new Zzyuser();
					u.setUsername(username);
					u.setUrl(supersu);
					realUserName = u.getUsername();
					
				}else{
					zr.setFlag(ZzyCommon.ZZYFAIL);
					zr.setResultmsg(ZzyCommon.ZZYFAIL_USERINVALID);
					return zr;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		//generate token
		/*Random random = new Random();
		int itoken = random.nextInt(100000);*/
		String url = u.getUrl();
		if(realUserName == null){
			realUserName = "";
		}
		
		String sToken = Jwts.builder().setSubject(realUserName).setIssuedAt(new Date())
	            .signWith(SignatureAlgorithm.HS256, "secretkey").compact();
		
		zr = ZzyCommon.tokenkeep(realUserName, sToken, url,redisTemplate);
		System.out.println("user check result is " + zr);
		return zr;
	}
}
