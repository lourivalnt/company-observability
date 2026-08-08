package br.com.company.platform.observability.autoconfigure.logging;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class StructuredLoggingEnvironmentPostProcessorTest {

    private final StructuredLoggingEnvironmentPostProcessor processor =
            new StructuredLoggingEnvironmentPostProcessor();

    @Test
    void shouldConfigureStructuredConsoleLoggingByDefault() {

        MockEnvironment environment =
                new MockEnvironment()
                        .withProperty(
                                "spring.application.name",
                                "test-service"
                        );

        processor.postProcessEnvironment(environment, new SpringApplication(Object.class));

        assertThat(
                environment.getProperty(
                        "logging.structured.format.console"
                )
        ).isEqualTo("logstash");

        assertThat(
                environment.getProperty(
                        "logging.structured.json.add.application"
                )
        ).isEqualTo("test-service");

        assertThat(
                environment.getProperty(
                        "logging.structured.json.add.environment"
                )
        ).isEqualTo("local");
    }

    @Test
    void shouldAllowApplicationToOverrideFormat() {

        MockEnvironment environment =
                new MockEnvironment()
                        .withProperty(
                                "company.observability.logging.format",
                                "ecs"
                        )
                        .withProperty(
                                "logging.structured.format.console",
                                "gelf"
                        );

        processor.postProcessEnvironment(
                environment,
                new SpringApplication(Object.class)
        );

        /*
         * A propriedade original tem precedência
         * sobre os defaults do Starter.
         */
        assertThat(
                environment.getProperty(
                        "logging.structured.format.console"
                )
        ).isEqualTo("gelf");
    }

    @Test
    void shouldConfigureFileWhenEnabled() {

        MockEnvironment environment =
                new MockEnvironment()
                        .withProperty(
                                "company.observability.logging.file-enabled",
                                "true"
                        )
                        .withProperty(
                                "company.observability.logging.file-name",
                                "logs/test.json"
                        );

        processor.postProcessEnvironment(
                environment,
                new SpringApplication(Object.class)
        );

        assertThat(
                environment.getProperty(
                        "logging.file.name"
                )
        ).isEqualTo("logs/test.json");

        assertThat(
                environment.getProperty(
                        "logging.structured.format.file"
                )
        ).isEqualTo("logstash");
    }

    @Test
    void shouldNotConfigureLoggingWhenDisabled() {

        MockEnvironment environment =
                new MockEnvironment()
                        .withProperty(
                                "company.observability.logging.enabled",
                                "false"
                        );

        processor.postProcessEnvironment(
                environment,
                new SpringApplication(Object.class)
        );

        assertThat(
                environment.getProperty(
                        "logging.structured.format.console"
                )
        ).isNull();
    }
}