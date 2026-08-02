package br.com.company.platform.observability.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LogContextTest {

    @AfterEach
    void cleanup() {
        LogContext.clear();
    }

    @Test
    void shouldPutAndGetValue() {

        LogContext.put("loanId", 123L);

        assertThat(LogContext.get("loanId")).isEqualTo("123");
    }

    @Test
    void shouldIgnoreNullValue() {

        LogContext.put("loanId", null);

        assertThat(LogContext.get("loanId")).isNull();
    }

    @Test
    void shouldIgnoreBlankKey() {

        LogContext.put(" ", "value");

        assertThat(LogContext.copy()).isEmpty();
    }

    @Test
    void shouldPutMultipleValues() {

        LogContext.putAll(
                Map.of(
                        "loanId", 10L,
                        "delegate", "CreditAnalysisDelegate"
                )
        );

        assertThat(LogContext.copy())
                .containsEntry("loanId", "10")
                .containsEntry(
                        "delegate",
                        "CreditAnalysisDelegate"
                );
    }

    @Test
    void shouldRemoveValue() {
        LogContext.put("loanId", "123");
        LogContext.remove("loanId");
        assertThat(LogContext.get("loanId")).isNull();
    }

    @Test
    void shouldCopyContext() {

        LogContext.put(
                LogContext.CORRELATION_ID,
                "correlation-001"
        );

        Map<String, String> context = LogContext.copy();

        assertThat(context)
                .containsEntry(
                        LogContext.CORRELATION_ID,
                        "correlation-001"
                );
    }

    @Test
    void shouldRestoreContext() {

        LogContext.put("oldKey", "oldValue");

        Map<String, String> context = LogContext.copy();

        LogContext.clear();

        LogContext.put("newKey", "newValue");

        LogContext.restore(context);

        assertThat(LogContext.get("oldKey")).isEqualTo("oldValue");

        assertThat(LogContext.get("newKey")).isNull();
    }
}