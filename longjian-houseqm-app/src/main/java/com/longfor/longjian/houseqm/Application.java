package com.longfor.longjian.houseqm;

import com.longfor.gaia.gfs.web.feign.EnableLFFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lipeishuai
 * @date 2018-11-10 10:45
 */
@SpringBootApplication(scanBasePackages = "com.longfor")
@EnableLFFeignClients(basePackages = "com.longfor")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}