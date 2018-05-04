package com.mgp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.mgp.*.dao")
public class BaseItem {
    public static void main(String[] args) {
        SpringApplication.run(BaseItem.class, args);
    }
}
