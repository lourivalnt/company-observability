package br.com.company.platform.observability.autoconfigure.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class MetricsAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(
                                    MetricsAutoConfiguration.class,
                                    SimpleMetricsExportAutoConfiguration.class,
                                    br.com.company.platform.observability.autoconfigure.metrics
                                            .MetricsAutoConfiguration.class
                            )
                    )
                    .withPropertyValues(
                            "spring.application.name=test-service"
                    );

    @Test
    void shouldAddApplicationAndEnvironmentTags() {

        contextRunner.run(context -> {

            assertThat(context)
                    .hasSingleBean(MeterRegistry.class);

            MeterRegistry registry =
                    context.getBean(
                            MeterRegistry.class
                    );

            registry.counter(
                    "test.metric"
            ).increment();

            var counter =
                    registry.get(
                            "test.metric"
                    ).counter();

            assertThat(
                    counter.getId()
                            .getTag("application")
            ).isEqualTo("test-service");

            assertThat(
                    counter.getId()
                            .getTag("environment")
            ).isEqualTo("local");
        });
    }

    @Test
    void shouldAllowCustomEnvironment() {

        contextRunner
                .withPropertyValues(
                        "company.observability.metrics.environment=dev"
                )
                .run(context -> {

                    MeterRegistry registry =
                            context.getBean(
                                    MeterRegistry.class
                            );

                    registry.counter(
                            "test.metric"
                    ).increment();

                    var counter =
                            registry.get(
                                    "test.metric"
                            ).counter();

                    assertThat(
                            counter.getId()
                                    .getTag("environment")
                    ).isEqualTo("dev");
                });
    }

    @Test
    void shouldDisableMetricsConfiguration() {

        contextRunner
                .withPropertyValues(
                        "company.observability.metrics.enabled=false"
                )
                .run(context -> {

                    assertThat(context)
                            .doesNotHaveBean(
                                    "companyMeterRegistryCustomizer"
                            );
                });
    }
}