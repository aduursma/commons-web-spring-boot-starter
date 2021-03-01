package nl.agility.commons.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static nl.agility.commons.web.filters.CustomMdcFilter.CORRELATION_ID;

@Slf4j
@RestController
public class TestController {

    public static final String TEST_LOGGER_NAME = "UNITTEST";
    public static final String TEST_MESSAGE = "Test messsage";

//    private static final Logger LOGGER = LoggerFactory.getLogger(TEST_LOGGER_NAME);

    @GetMapping("/test")
    public String test() {
//        LOGGER.info(TEST_MESSAGE);
        log.info(TEST_MESSAGE);
        return MDC.get(CORRELATION_ID);
    }

}
