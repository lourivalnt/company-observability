package br.com.company.platform.observability.autoconfigure.actuator;

final class ActuatorPropertyNames {

    static final String OBSERVABILITY_ENABLED =
            "company.observability.enabled";

    static final String ACTUATOR_ENABLED =
            "company.observability.actuator.enabled";

    static final String BASE_PATH =
            "company.observability.actuator.base-path";

    static final String EXPOSURE =
            "company.observability.actuator.exposure";

    static final String SHOW_DETAILS =
            "company.observability.actuator.show-details";

    static final String PROBES_ENABLED =
            "company.observability.actuator.probes-enabled";

    static final String ADDITIONAL_PROBE_PATHS =
            "company.observability.actuator.additional-probe-paths";

    static final String INFO_ENABLED =
            "company.observability.actuator.info-enabled";

    private ActuatorPropertyNames() {
    }
}