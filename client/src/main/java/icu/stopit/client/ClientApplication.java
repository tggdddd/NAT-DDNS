package icu.stopit.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("icu.stopit.client.**")
public class ClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(ClientApplication.class, args);
        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        System.out.println("配置地址：http://localhost:" + port);
    }
}
