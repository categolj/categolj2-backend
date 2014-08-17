package am.ik.categolj2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class App {
    public static final String API_VERSION = "v1";

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
