package nl.agility.commons.web.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static nl.agility.commons.web.filters.CustomMdcFilter.CORRELATION_ID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomMdcFilterTest {

    private MockMvc mockMvc;
    private CustomMdcFilter customMdcFilter;

    @BeforeEach
    public void setUp() {
        MDC.clear();

        mockMvc = MockMvcBuilders
            .standaloneSetup(new TestController())
            .addFilters(new CustomMdcFilter())
            .build();

        customMdcFilter = spy(new CustomMdcFilter());
    }

    @Test
    public void addingAndRemovingPropertiesToTheMdcAreBothAlwaysCalledExactlyOnce() throws Exception {
        var request = mock(ServletRequest.class);
        var response = mock(ServletResponse.class);
        var filterChain = mock(FilterChain.class);

        customMdcFilter.doFilter(request, response, filterChain);

        verify(customMdcFilter, times(1)).addCustomPropertiesToMdc(request);
        verify(customMdcFilter, times(1)).removeCustomPropertiesFromMdc();
    }

    @Test
    public void aRandomCorrelationIdIsAddedToTheMdcWhenACorrelationIdHeaderIsNotPresent() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/test"))
            .andExpect(status().isOk())
            .andExpect(content().string(is(notNullValue())));
    }

    @Test
    public void theCorrelationIdFromTheCorrelationIdHeaderIsAddedToTheMdcWhenACorrelationIdHeaderIsPresent() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/test").header(CORRELATION_ID, "48485a3953bb6124"))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("48485a3953bb6124")));
    }

    @RestController
    private class TestController {

        @GetMapping("/test")
        public String test() {
            return MDC.get(CORRELATION_ID);
        }

    }

}
