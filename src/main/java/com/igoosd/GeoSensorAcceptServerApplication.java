package com.igoosd;

import com.igoosd.server.AcceptServerBootstrap;
import com.igoosd.util.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeoSensorAcceptServerApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(GeoSensorAcceptServerApplication.class, args);
		System.out.println("地磁接入服务已启动.....");
		SpringContextHolder.getBean(AcceptServerBootstrap.class).run();
	}
}
