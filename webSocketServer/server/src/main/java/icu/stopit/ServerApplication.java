package icu.stopit;


import icu.stopit.Controller.RedirectController;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@ComponentScan("icu.stopit.**")
@MapperScan("icu.stopit.**")
public class ServerApplication {

    public static void main(String... args) {
        ConfigurableApplicationContext run = SpringApplication.run(ServerApplication.class, args);
        ConfigurableEnvironment environment = run.getEnvironment();
        String port = environment.getProperty("server.port");
        boolean flag = true;
        for (String arg : args) {
            System.out.println("参数：" + arg);
            if (arg.startsWith("--name=")) {
                String name = arg.substring("--name=".length());
                RedirectController.setName(name);
                System.out.printf("端口%s映射为%s\n", port, name);
                flag = false;
            }
        }
        if (flag) {
            System.err.println(new String("未配置name属性，启动失败！".getBytes(), StandardCharsets.UTF_8));
            System.exit(1);
        }
    }

}
