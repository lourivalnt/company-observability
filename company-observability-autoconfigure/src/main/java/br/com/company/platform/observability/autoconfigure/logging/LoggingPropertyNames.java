package br.com.company.platform.observability.autoconfigure.logging;

final class LoggingPropertyNames {

    static final String OBSERVABILITY_ENABLED = "company.observability.enabled";

    static final String LOGGING_ENABLED = "company.observability.logging.enabled";

    static final String FORMAT = "company.observability.logging.format";

    static final String ENVIRONMENT = "company.observability.logging.environment";

    static final String FILE_ENABLED = "company.observability.logging.file-enabled";

    static final String FILE_NAME = "company.observability.logging.file-name";

    static final String STACKTRACE_MAX_LENGTH = "company.observability.logging.stacktrace-max-length";

    static final String STACKTRACE_MAX_THROWABLE_DEPTH = "company.observability.logging.stacktrace-max-throwable-depth";

    private LoggingPropertyNames() {
    }
}