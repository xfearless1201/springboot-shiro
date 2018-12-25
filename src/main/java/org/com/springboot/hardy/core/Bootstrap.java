package org.com.springboot.hardy.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import(net.sf.oval.Validator.class)
@ComponentScan(basePackages="org.com.springboot.hardy.core")
@PropertySource(value={"classpath:conf/jdbc.properties","classpath:conf/redis.properties","classpath:conf/conf.properties"},ignoreResourceNotFound=true)
public class Bootstrap {
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }
}
