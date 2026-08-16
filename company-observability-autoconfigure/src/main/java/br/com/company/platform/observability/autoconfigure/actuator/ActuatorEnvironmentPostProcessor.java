package br.com.company.platform.observability.autoconfigure.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

public class ActuatorEnvironmentPostProcessor
        implements EnvironmentPostProcessor, Ordered {

    private static final String PROPERTY_SOURCE_NAME =
            "companyObservabilityActuatorDefaults";

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment,
            SpringApplication application) {

        if (!isEnabled(
                environment,
                ActuatorPropertyNames.OBSERVABILITY_ENABLED,
                true
        )) {
            return;
        }

        if (!isEnabled(
                environment,
                ActuatorPropertyNames.ACTUATOR_ENABLED,
                true
        )) {
            return;
        }

        Map<String, Object> defaults =
                createDefaults(environment);

        /*
         * Baixa precedência:
         * application.yml e variáveis de ambiente
         * podem sobrescrever os defaults.
         */
        environment.getPropertySources()
                .addLast(
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

        String basePath =
                environment.getProperty(
                        ActuatorPropertyNames.BASE_PATH,
                        "/actuator"
                );

        String exposure =
                environment.getProperty(
                        ActuatorPropertyNames.EXPOSURE,
                        "health,info,metrics"
                );

        String showDetails =
                environment.getProperty(
                        ActuatorPropertyNames.SHOW_DETAILS,
                        "never"
                );

        boolean probesEnabled =
                isEnabled(
                        environment,
                        ActuatorPropertyNames.PROBES_ENABLED,
                        true
                );

        boolean additionalProbePaths =
                isEnabled(
                        environment,
                        ActuatorPropertyNames.ADDITIONAL_PROBE_PATHS,
                        false
                );

        boolean infoEnabled =
                isEnabled(
                        environment,
                        ActuatorPropertyNames.INFO_ENABLED,
                        true
                );

        String applicationName =
                environment.getProperty(
                        "spring.application.name",
                        "application"
                );

        properties.put(
                "info.app.name",
                applicationName
        );

        String environmentName =
                environment.getProperty(
                        "company.observability.metrics.environment",
                        environment.getProperty(
                                "APP_ENVIRONMENT",
                                "local"
                        )
                );

        properties.put(
                "info.app.environment",
                environmentName
        );

        properties.put(
                "management.endpoints.web.base-path",
                basePath
        );

        properties.put(
                "management.endpoints.web.exposure.include",
                exposure
        );

        properties.put(
                "management.endpoint.health.show-details",
                showDetails
        );

        properties.put(
                "management.endpoint.health.probes.enabled",
                probesEnabled
        );

        properties.put(
                "management.endpoint.health.probes.add-additional-paths",
                additionalProbePaths
        );

        properties.put(
                "management.info.env.enabled",
                infoEnabled
        );

        return properties;
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
        return Ordered.LOWEST_PRECEDENCE;
    }
}