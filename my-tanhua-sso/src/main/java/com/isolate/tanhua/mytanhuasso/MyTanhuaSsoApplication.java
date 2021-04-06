package com.isolate.tanhua.mytanhuasso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.isolate.tanhua.mytanhuasso.mapper")
@SpringBootApplication
public class MyTanhuaSsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyTanhuaSsoApplication.class, args);
    }

}
