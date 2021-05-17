package com.supportportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

import static com.supportportal.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class SupportPortalBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupportPortalBackEndApplication.class, args);
        new File(USER_FOLDER).mkdirs();
    }

    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
