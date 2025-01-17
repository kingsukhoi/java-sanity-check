package ca.farsos.sanitycheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;

@RestController
public class Routes {
  Logger logger
          = LoggerFactory.getLogger(Routes.class);

  @GetMapping("/")
  public String health(@RequestHeader Map<String, String> headers) {
    for (String key : headers.keySet()) {
      logger.info("{}: {}", key, headers.get(key));
    }
    return "Running";
  }

  @GetMapping("/hello")
  public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
    return String.format("Hello %s", HtmlUtils.htmlEscape(name, "UTF-8"));
  }

  @GetMapping("/headerinfo")
  public Map<String, String> dumpHeaders(@RequestHeader Map<String, String> headers) {
    for (String key : headers.keySet()) {
      var curr = headers.get(key);
      curr = HtmlUtils.htmlEscape(curr, "UTF-8");
      headers.put(key, curr);
    }
    return headers;
  }

  @GetMapping("/die")
  public String die() {
    logger.error("Received kill signal. Good by cruel world!!!!");
    System.exit(1);
    return "If you are seeing me, I'm not dead";
  }

}