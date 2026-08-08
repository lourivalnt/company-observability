package br.com.company.platform.observability.autoconfigure.logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

public class StructuredLoggingEnvironmentPostProcessor
        implements EnvironmentPostProcessor, Ordered {

    private static final String PROPERTY_SOURCE_NAME = "companyObservabilityLoggingDefaults";

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment,
            SpringApplication application) {

        if (!isEnabled(
                environment,
                LoggingPropertyNames.OBSERVABILITY_ENABLED,
                true
        )) {
            return;
        }

        if (!isEnabled(
                environment,
                LoggingPropertyNames.LOGGING_ENABLED,
                true
        )) {
            return;
        }

        Map<String, Object> defaults =
                createDefaults(environment);

        /*
         * addLast garante baixa precedência.
         *
         * application.yml, variáveis de ambiente e
         * parâmetros da JVM podem sobrescrever os defaults.
         */
        environment.getPropertySources().addLast(
                new MapPropertySource(
                        PROPERTY_SOURCE_NAME,
                        defaults
                )
        );
    }

    private Map<String, Object> createDefaults(
            ConfigurableEnvironment environment) {

        Map<String, Object> properties =
                new LinkedHashMap<>();

        String format = environment.getProperty(
                LoggingPropertyNames.FORMAT,
                "logstash"
        );

        String applicationName = environment.getProperty(
                "spring.application.name",
                "application"
        );

        String environmentName = environment.getProperty(
                LoggingPropertyNames.ENVIRONMENT,
                environment.getProperty(
                        "APP_ENVIRONMENT",
                        "local"
                )
        );

        int stacktraceMaxLength = environment.getProperty(
                LoggingPropertyNames.STACKTRACE_MAX_LENGTH,
                Integer.class,
                4096
        );

        int stacktraceMaxDepth = environment.getProperty(
                LoggingPropertyNames.STACKTRACE_MAX_THROWABLE_DEPTH,
                Integer.class,
                30
        );

        /*
         * Console JSON habilitado por padrão.
         */
        properties.put("logging.structured.format.console", format);

        /*
         * Campos corporativos adicionados a todos os logs.
         */
        properties.put("logging.structured.json.add.application", applicationName);
        properties.put("logging.structured.json.add.environment", environmentName);

        /*
         * Protege o sistema de ingestão contra
         * stack traces excessivamente grandes.
         */
        properties.put("logging.structured.json.stacktrace.max-length", stacktraceMaxLength);
        properties.put("logging.structured.json.stacktrace.max-throwable-depth", stacktraceMaxDepth);

        configureFileLogging(environment, properties, format);

        return properties;
    }

    private void configureFileLogging(
            ConfigurableEnvironment environment,
            Map<String, Object> properties,
            String format) {

        boolean fileEnabled = isEnabled(
                environment,
                LoggingPropertyNames.FILE_ENABLED,
                false
        );

        if (!fileEnabled) {
            return;
        }

        String fileName = environment.getProperty(
                LoggingPropertyNames.FILE_NAME,
                "logs/application.json"
        );

        properties.put(
                "logging.file.name",
                fileName
        );

        properties.put(
                "logging.structured.format.file",
                format
        );

        properties.put(
                "logging.logback.rollingpolicy.max-file-size",
                "100MB"
        );

        properties.put(
                "logging.logback.rollingpolicy.max-history",
                "30"
        );

        properties.put(
                "logging.logback.rollingpolicy.total-size-cap",
                "5GB"
        );
    }

    private boolean isEnabled(
            ConfigurableEnvironment environment,
            String property,
            boolean defaultValue) {

        return environment.getProperty(
                property,
                Boolean.class,
                defaultValue
        );
    }

    @Override
    public int getOrder() {
        /*
         * Executa com baixa precedência para que
         * as configurações da aplicação prevaleçam.
         */
        return Ordered.LOWEST_PRECEDENCE;
    }
}