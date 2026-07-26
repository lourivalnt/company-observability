package br.com.company.platform.observability.logging;

import org.slf4j.MDC;

import java.util.Collections;
import java.util.Map;

public final class LogContext {

    public static final String CORRELATION_ID =
            "correlationId";

    public static final String TRACE_ID =
            "traceId";

    public static final String SPAN_ID =
            "spanId";

    private LogContext() {
    }

    public static void put(String key, Object value) {
        if (key == null || key.isBlank() || value == null) {
            return;
        }

        MDC.put(key, String.valueOf(value));
    }

    public static String get(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }

        return MDC.get(key);
    }

    public static void remove(String key) {
        if (key == null || key.isBlank()) {
            return;
        }

        MDC.remove(key);
    }

    public static void clear() {
        MDC.clear();
    }

    public static Map<String, String> copy() {
        Map<String, String> context =
                MDC.getCopyOfContextMap();

        if (context == null) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(context);
    }

    public static void restore(
            Map<String, String> context) {

        MDC.clear();

        if (context != null && !context.isEmpty()) {
            MDC.setContextMap(context);
        }
    }
}