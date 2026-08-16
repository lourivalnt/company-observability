package br.com.company.platform.observability.autoconfigure.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class ActuatorEnvironmentPostProcessorTest {

    private final ActuatorEnvironmentPostProcessor processor =
            new ActuatorEnvironmentPostProcessor();

    @Test
    void shouldConfigureActuatorDefaults() {

        MockEnvironment environment =
                new MockEnvironment();

        processor.postProcessEnvironment(
                environment,
                new SpringApplication(Object.class)
        );

        assertThat(
                environment.getProperty(
                        "management.endpoints.web.base-path"
                )
        ).isEqualTo("/actuator");

        assertThat(
                environment.getProperty(
                        "management.endpoints.web.exposure.include"
                )
        ).isEqualTo(
                "health,info,metrics"
        );

        assertThat(
                environment.getProperty(
                        "management.endpoint.health.show-details"
                )
        ).isEqualTo("never");

        assertThat(
                environment.getProperty(
                        "management.endpoint.health.probes.enabled",
                        Boolean.class
                )
        ).isTrue();
    }

    @Test
    void shouldAllowCustomExposure() {

        MockEnvironment environment =
                new MockEnvironment()
                        .withProperty(
                                "company.observability.actuator.exposure",
                                "health,info"
                        );

        processor.postProcessEnvironment(
                environment,
                new SpringApplication(Object.class)
        );

        assertThat(
                environment.getProperty(
                        "management.endpoints.web.exposure.include"
                )
        ).isEqualTo(
                "health,info"
        );
    }

    @Test
    void shouldAllowApplicationOverride() {

        MockEnvironment environment =
                new MockEnvironment()
                        .withProperty(
                                "management.endpoint.health.show-details",
                                "always"
                        );

        processor.postProcessEnvironment(
                environment,
                new SpringApplication(Object.class)
        );

        assertThat(
                environment.getProperty(
                        "management.endpoint.health.show-details"
                )
        ).isEqualTo("always");
    }

    @Test
    void shouldDisableActuatorDefaults() {

        MockEnvironment environment =
                new MockEnvironment()
                        .withProperty(
                                "company.observability.actuator.enabled",
                                "false"
                        );

        processor.postProcessEnvironment(
                environment,
                new SpringApplication(Object.class)
        );

        assertThat(
                environment.getProperty(
                        "management.endpoints.web.base-path"
                )
        ).isNull();
    }
}