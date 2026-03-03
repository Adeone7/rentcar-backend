package com.example.sharebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration // 스프링 설정 파일임을 알려주는 어노테이션
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path.of(System.getProperty("user.home"), "share", "car-images");
        String uploadDirPath = Paths.get(System.getProperty("user.home"), "share", "car-images")
                .toAbsolutePath() // 절대 경로로 변환 (중요!)
                .toString();

        registry.addResourceHandler("/car-images/**")
                .addResourceLocations("file:" + uploadDirPath + "/"); // 중요한 점: 경로 끝에 '/'를 꼭 붙여야 해!
    }
}
