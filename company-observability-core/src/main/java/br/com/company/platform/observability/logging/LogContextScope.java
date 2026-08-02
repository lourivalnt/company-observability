package br.com.company.platform.observability.logging;

import java.util.Map;

public final class LogContextScope implements AutoCloseable {

    private final Map<String, String> previousContext;

    private boolean closed;

    private LogContextScope(
            Map<String, ?> additionalContext) {

        this.previousContext = LogContext.copy();

        LogContext.putAll(additionalContext);
    }

    public static LogContextScope open(
            Map<String, ?> context) {

        return new LogContextScope(context);
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }

        LogContext.restore(previousContext);
        closed = true;
    }
}