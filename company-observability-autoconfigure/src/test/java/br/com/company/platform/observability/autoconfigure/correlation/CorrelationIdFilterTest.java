package br.com.company.platform.observability.autoconfigure.correlation;

import br.com.company.platform.observability.autoconfigure.ObservabilityProperties;
import br.com.company.platform.observability.logging.LogContext;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CorrelationIdFilterTest {

    @AfterEach
    void cleanup() {
        LogContext.clear();
    }

    @Test
    void shouldUseCorrelationIdFromRequest()
            throws Exception {

        ObservabilityProperties properties =
                new ObservabilityProperties();

        CorrelationIdFilter filter =
                new CorrelationIdFilter(
                        properties,
                        () -> "generated-id"
                );

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.addHeader(
                "X-Correlation-Id",
                "received-id"
        );

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(
                request,
                response,
                chain
        );

        assertThat(
                response.getHeader(
                        "X-Correlation-Id"
                )
        ).isEqualTo("received-id");

        assertThat(
                LogContext.get(
                        LogContext.CORRELATION_ID
                )
        ).isNull();

        verify(chain).doFilter(
                request,
                response
        );
    }

    @Test
    void shouldGenerateIdWhenHeaderIsMissing()
            throws Exception {

        ObservabilityProperties properties =
                new ObservabilityProperties();

        CorrelationIdFilter filter =
                new CorrelationIdFilter(
                        properties,
                        () -> "generated-id"
                );

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(
                request,
                response,
                chain
        );

        assertThat(
                response.getHeader(
                        "X-Correlation-Id"
                )
        ).isEqualTo("generated-id");
    }
}