package com.tanhua.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.tanhua.sso.mapper")
@SpringBootApplication
public class MyTanhuaSsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyTanhuaSsoApplication.class, args);
    }

}
