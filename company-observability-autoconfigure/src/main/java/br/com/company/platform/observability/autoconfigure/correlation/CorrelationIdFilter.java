package br.com.company.platform.observability.autoconfigure.correlation;

import br.com.company.platform.observability.autoconfigure.ObservabilityProperties;
import br.com.company.platform.observability.correlation.CorrelationIdGenerator;
import br.com.company.platform.observability.logging.LogContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class CorrelationIdFilter extends OncePerRequestFilter {

    private final ObservabilityProperties properties;
    private final CorrelationIdGenerator generator;

    public CorrelationIdFilter(
            ObservabilityProperties properties,
            CorrelationIdGenerator generator) {

        this.properties = properties;
        this.generator = generator;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Map<String, String> previousContext =
                LogContext.copy();

        String headerName = properties
                .getCorrelation()
                .getHeaderName();

        String correlationId =
                resolveCorrelationId(
                        request,
                        headerName
                );

        try {
            if (correlationId != null) {
                LogContext.put(
                        LogContext.CORRELATION_ID,
                        correlationId
                );

                if (properties
                        .getCorrelation()
                        .isIncludeInResponse()) {

                    response.setHeader(
                            headerName,
                            correlationId
                    );
                }
            }

            filterChain.doFilter(
                    request,
                    response
            );

        } finally {
            LogContext.restore(previousContext);
        }
    }

    private String resolveCorrelationId(
            HttpServletRequest request,
            String headerName) {

        String correlationId =
                request.getHeader(headerName);

        if (!isBlank(correlationId)) {
            return correlationId;
        }

        if (!properties
                .getCorrelation()
                .isGenerateWhenMissing()) {

            return null;
        }

        return generator.generate();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}