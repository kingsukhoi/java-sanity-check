package ca.farsos.sanitycheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class StartUpThing implements ApplicationListener<ApplicationReadyEvent> {

    Logger logger = LoggerFactory.getLogger(StartUpThing.class);

    @Value("${TERMINATE_AFTER_SECONDS:-1}")
    private String terminateAfterString;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        var killAfter = Long.parseLong(terminateAfterString);

        if (killAfter > 0) {
            logger.info(String.format("Dying after %s seconds", terminateAfterString));
            var r = new Runnable() {
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(killAfter);
                        logger.error("Times up. Good by cruel world!!!!");
                        System.exit(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };
            r.run();
        } else {
            logger.info("TERMINATE_AFTER_SECONDS not set. Won't be dying any time soon.");
        }


    }
}