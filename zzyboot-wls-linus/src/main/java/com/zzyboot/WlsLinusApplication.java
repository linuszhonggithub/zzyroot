package com.zzyboot;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.zzyboot.common.util.DES;

@SpringBootApplication
public class WlsLinusApplication {

	public static void main(String[] args) {
		SpringApplication.run(WlsLinusApplication.class, args);

	}
	 @Autowired
	 private Environment env;
	    @Bean("defaultDataSource")  
	    public DataSource defaultDataSource() {
   	    	String drivername = env.getProperty("spring.datasource.driver-class-name");
	    	String url = env.getProperty("spring.datasource.url");
	    	String username = env.getProperty("spring.datasource.username");
	    	String password = env.getProperty("spring.datasource.password");
	    	
	    	try {
				password=DES.decryption(password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	DataSource ds = DataSourceBuilder.create()
	    			.driverClassName(drivername)
	    			.url(url)
	    			.username(username)
	    			.password(password)
	    			.build(); 
	        return  ds;
	    }  

}
