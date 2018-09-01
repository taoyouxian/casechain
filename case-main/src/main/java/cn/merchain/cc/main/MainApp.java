package cn.merchain.cc.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @version V1.0
 * @Package: cn.merchain.cc.main
 * @ClassName: MainApp
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-25 21:48
 **/
@Configuration
@SpringBootApplication
@EnableScheduling
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

}

