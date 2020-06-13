package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {



        System.out.println(new BCryptPasswordEncoder().encode("CWX"));
        System.out.println(new BCryptPasswordEncoder().encode("RW"));
        SpringApplication.run(DemoApplication.class, args);
    }

}
