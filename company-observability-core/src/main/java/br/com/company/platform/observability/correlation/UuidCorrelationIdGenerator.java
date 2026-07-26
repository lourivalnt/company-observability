package br.com.company.platform.observability.correlation;

import java.util.UUID;

public class UuidCorrelationIdGenerator
        implements CorrelationIdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}