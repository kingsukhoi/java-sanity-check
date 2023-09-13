package ca.farsos.sanitycheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Routes {
    Logger logger
            = LoggerFactory.getLogger(Routes.class);

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s", name);
    }

    @GetMapping("/die")
    public String die() {
        logger.error("Received kill signal. Good by cruel world!!!!");
        System.exit(1);
        return "If you are seeing me, I'm not dead";
    }

}
