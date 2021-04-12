package com.tanhua.homepage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.tanhua,homepage.mapper")
@SpringBootApplication
public class MyTanhuaHomepageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyTanhuaHomepageApplication.class, args);
    }

}
