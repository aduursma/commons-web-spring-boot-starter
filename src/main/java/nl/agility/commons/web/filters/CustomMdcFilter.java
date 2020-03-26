package nl.agility.commons.web.filters;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class CustomMdcFilter implements Filter {

    public static final String CORRELATION_ID = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        addCustomPropertiesToMdc(request);
        try {
            log.info("Request Started");
            filterChain.doFilter(request, response);
            log.info("Request Finished");
        } finally {
            removeCustomPropertiesFromMdc();
        }
    }

    protected void addCustomPropertiesToMdc(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String correlationId = httpRequest.getHeader(CORRELATION_ID);
            MDC.put(CORRELATION_ID, correlationId == null ? UUID.randomUUID().toString() : correlationId);
        }
    }

    protected void removeCustomPropertiesFromMdc() {
        MDC.remove(CORRELATION_ID);
    }
}
