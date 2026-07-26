package br.com.company.platform.observability.correlation;

@FunctionalInterface
public interface CorrelationIdGenerator {

    String generate();
}