package nl.agility.commons.web.filters;

import nl.agility.commons.web.TestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

class CustomMdcFilterTest {

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new TestController())
            .addFilters(new CustomMdcFilter())
            .build();
    }

    @Test
    void addingAndRemovingPropertiesToTheMdcAreBothAlwaysCalledExactlyOnce() throws Exception {
        var request = mock(ServletRequest.class);
        var response = mock(ServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var customMdcFilter = spy(new CustomMdcFilter());

        customMdcFilter.doFilter(request, response, filterChain);

        verify(customMdcFilter, times(1)).addCustomPropertiesToMdc(request);
        verify(customMdcFilter, times(1)).removeCustomPropertiesFromMdc();
    }

    @Test
    void aRandomCorrelationIdIsAddedToTheMdcWhenACorrelationIdHeaderIsNotPresent() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/test"))
            .andExpect(status().isOk())
            .andExpect(content().string(is(notNullValue())));
    }

    @Test
    void theCorrelationIdFromTheCorrelationIdHeaderIsAddedToTheMdcWhenACorrelationIdHeaderIsPresent() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/test").header(CORRELATION_ID, "48485a3953bb6124"))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("48485a3953bb6124")));
    }

}
