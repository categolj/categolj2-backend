package am.ik.categolj2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class App {

    public static void main(String[] args) {
        if (System.getProperty("h2.implicitRelativePath") == null) {
            // keep compatibility with H2 1.3
            // prevent http://www.h2database.com/javadoc/org/h2/api/ErrorCode.html#c90011
            System.setProperty("h2.implicitRelativePath", "true");
        }
        SpringApplication.run(App.class, args);
    }
}
