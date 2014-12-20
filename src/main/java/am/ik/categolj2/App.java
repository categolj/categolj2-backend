package am.ik.categolj2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Stream;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        argsToSyetemProperty(args, "log.verbose", "log.sql", "log.sql.result");
        keepCompatibilityH2_13();
        SpringApplication.run(App.class, args);
    }

    static void argsToSyetemProperty(String[] args, String... keys) {
        Stream.of(keys).forEach(key -> {
            Stream.of(args)
                    .filter(x -> x.startsWith("--" + key + "="))
                    .map(x -> x.substring(("--" + key + "=").length()))
                    .map(String::trim)
                    .findFirst()
                    .map(x -> {
                        System.out.println("match " + key + " ->" + x);
                        return x;
                    })
                    .ifPresent(x -> System.setProperty(key, x));
        });
    }

    static void keepCompatibilityH2_13() {
        if (System.getProperty("h2.implicitRelativePath") == null) {
            // keep compatibility with H2 1.3
            // prevent http://www.h2database.com/javadoc/org/h2/api/ErrorCode.html#c90011
            System.setProperty("h2.implicitRelativePath", "true");
        }
    }
}
