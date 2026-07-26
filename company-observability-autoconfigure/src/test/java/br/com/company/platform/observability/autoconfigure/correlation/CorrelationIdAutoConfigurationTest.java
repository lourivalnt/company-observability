package br.com.company.platform.observability.autoconfigure.correlation;

import br.com.company.platform.observability.correlation.CorrelationIdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(
                                    CorrelationIdAutoConfiguration.class
                            )
                    );

    @Test
    void shouldCreateDefaultGenerator() {

        contextRunner.run(context -> {

            assertThat(context)
                    .hasSingleBean(
                            CorrelationIdGenerator.class
                    );

            CorrelationIdGenerator generator =
                    context.getBean(
                            CorrelationIdGenerator.class
                    );

            assertThat(generator.generate())
                    .isNotBlank();
        });
    }

    @Test
    void shouldAllowCustomGenerator() {

        contextRunner
                .withBean(
                        CorrelationIdGenerator.class,
                        () -> () -> "fixed-correlation-id"
                )
                .run(context -> {

                    assertThat(context)
                            .hasSingleBean(
                                    CorrelationIdGenerator.class
                            );

                    CorrelationIdGenerator generator =
                            context.getBean(
                                    CorrelationIdGenerator.class
                            );

                    assertThat(generator.generate())
                            .isEqualTo(
                                    "fixed-correlation-id"
                            );
                });
    }

    @Test
    void shouldDisableAllObservability() {

        contextRunner
                .withPropertyValues(
                        "company.observability.enabled=false"
                )
                .run(context -> {

                    assertThat(context)
                            .doesNotHaveBean(
                                    CorrelationIdGenerator.class
                            );

                    assertThat(context)
                            .doesNotHaveBean(
                                    CorrelationIdFilter.class
                            );
                });
    }

    @Test
    void shouldDisableOnlyCorrelationFilter() {

        contextRunner
                .withPropertyValues(
                        "company.observability." +
                                "correlation.enabled=false"
                )
                .run(context -> {

                    assertThat(context)
                            .hasSingleBean(
                                    CorrelationIdGenerator.class
                            );

                    assertThat(context)
                            .doesNotHaveBean(
                                    CorrelationIdFilter.class
                            );
                });
    }
}