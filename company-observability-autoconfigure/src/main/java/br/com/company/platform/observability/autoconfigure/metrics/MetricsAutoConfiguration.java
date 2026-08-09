package br.com.company.platform.observability.autoconfigure.metrics;

import br.com.company.platform.observability.autoconfigure.ObservabilityProperties;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(MeterRegistry.class)
@EnableConfigurationProperties(ObservabilityProperties.class)
@ConditionalOnProperty(
        prefix = "company.observability.metrics",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class MetricsAutoConfiguration {

    @Bean(name = "companyMeterRegistryCustomizer")
    @ConditionalOnMissingBean(
            name = "companyMeterRegistryCustomizer"
    )
    public MeterRegistryCustomizer<MeterRegistry>
    companyMeterRegistryCustomizer(
            @Value("${spring.application.name:application}")
            String applicationName,
            ObservabilityProperties properties) {

        return registry -> {

            ObservabilityProperties.Metrics metrics =
                    properties.getMetrics();

            if (metrics.isApplicationTagEnabled()) {
                registry.config()
                        .commonTags(
                                "application",
                                applicationName
                        );
            }

            if (metrics.isEnvironmentTagEnabled()) {
                registry.config()
                        .commonTags(
                                "environment",
                                metrics.getEnvironment()
                        );
            }
        };
    }
}