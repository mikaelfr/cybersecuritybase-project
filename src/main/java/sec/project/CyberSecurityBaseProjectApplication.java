package sec.project;

import java.util.Map;
import java.util.TreeMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CyberSecurityBaseProjectApplication {

    public static void main(String[] args) throws Throwable {
        SpringApplication app = new SpringApplication(CyberSecurityBaseProjectApplication.class);
        Map<String, Object> map = new TreeMap<>();
        map.put("server.servlet.session.timeout", 600); // 10min timeout for admin login
        map.put("server.error.include-exception", true);
        map.put("server.error.include-stacktrace", "always");
        app.setDefaultProperties(map);
        app.run(args);
    }
}
