package br.com.company.platform.observability.autoconfigure.correlation;

import br.com.company.platform.observability.autoconfigure.ObservabilityProperties;
import br.com.company.platform.observability.correlation.CorrelationIdGenerator;
import br.com.company.platform.observability.correlation.UuidCorrelationIdGenerator;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@AutoConfiguration
@ConditionalOnClass(Filter.class)
@EnableConfigurationProperties(
        ObservabilityProperties.class
)
@ConditionalOnProperty(
        prefix = "company.observability",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class CorrelationIdAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CorrelationIdGenerator
    correlationIdGenerator() {

        return new UuidCorrelationIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix =
                    "company.observability.correlation",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public CorrelationIdFilter correlationIdFilter(
            ObservabilityProperties properties,
            CorrelationIdGenerator generator) {

        return new CorrelationIdFilter(
                properties,
                generator
        );
    }

    @Bean
    @ConditionalOnMissingBean(
            name = "correlationIdFilterRegistration"
    )
    @ConditionalOnProperty(
            prefix =
                    "company.observability.correlation",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public FilterRegistrationBean<CorrelationIdFilter>
    correlationIdFilterRegistration(
            CorrelationIdFilter filter) {

        FilterRegistrationBean<CorrelationIdFilter>
                registration =
                new FilterRegistrationBean<>();

        registration.setName(
                "companyCorrelationIdFilter"
        );

        registration.setFilter(filter);

        registration.setOrder(
                Ordered.HIGHEST_PRECEDENCE
        );

        registration.addUrlPatterns("/*");

        return registration;
    }
}