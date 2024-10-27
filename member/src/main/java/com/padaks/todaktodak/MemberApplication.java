package com.padaks.todaktodak;

import com.padaks.todaktodak.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
public class MemberApplication {

	private static final Logger log = LoggerFactory.getLogger(MemberApplication.class);

	@Autowired
	private ChatService chatService; // ChatService 타입의 chatService를 주입
	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}

	@PostConstruct
	public void init() {
		log.info("ChatService 빈이 주입되었습니다: {}", chatService != null);
		// chatService는 실제로 주입된 객체를 확인할 수 있도록 주입된 서비스로 변경
	}
}
