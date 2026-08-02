package br.com.company.platform.observability.logging;

import org.slf4j.MDC;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class LogContext {

    public static final String CORRELATION_ID = "correlationId";
    public static final String TRACE_ID = "traceId";
    public static final String SPAN_ID = "spanId";

    private LogContext() {
    }

    public static void put(
            String key,
            Object value) {

        if (isBlank(key) || value == null) {
            return;
        }

        MDC.put(
                key,
                String.valueOf(value)
        );
    }

    public static void putAll(
            Map<String, ?> values) {

        if (values == null || values.isEmpty()) {
            return;
        }

        values.forEach(LogContext::put);
    }

    public static String get(String key) {
        if (isBlank(key)) {
            return null;
        }

        return MDC.get(key);
    }

    public static boolean contains(String key) {
        return get(key) != null;
    }

    public static void remove(String key) {
        if (isBlank(key)) {
            return;
        }

        MDC.remove(key);
    }

    public static void removeAll(
            String... keys) {

        if (keys == null) {
            return;
        }

        for (String key : keys) {
            remove(key);
        }
    }

    public static Map<String, String> copy() {
        Map<String, String> currentContext =
                MDC.getCopyOfContextMap();

        if (currentContext == null) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(
                new HashMap<>(currentContext)
        );
    }

    public static void restore(
            Map<String, String> context) {

        MDC.clear();

        if (context == null || context.isEmpty()) {
            return;
        }

        MDC.setContextMap(
                new HashMap<>(context)
        );
    }

    public static void clear() {
        MDC.clear();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}