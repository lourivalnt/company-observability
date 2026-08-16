package br.com.company.platform.observability.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "company.observability"
)
public class ObservabilityProperties {

    private boolean enabled = true;

    private final Correlation correlation =
            new Correlation();

    private final Logging logging =
            new Logging();

    private final Metrics metrics =
            new Metrics();

    private final Actuator actuator =
            new Actuator();

    public Actuator getActuator() {
        return actuator;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(
            boolean enabled
    ) {
        this.enabled = enabled;
    }

    public Correlation getCorrelation() {
        return correlation;
    }

    public Logging getLogging() {
        return logging;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public static class Correlation {

        private boolean enabled = true;

        private String headerName =
                "X-Correlation-Id";

        private boolean generateWhenMissing = true;

        private boolean includeInResponse = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(
                boolean enabled
        ) {
            this.enabled = enabled;
        }

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(
                String headerName
        ) {
            this.headerName = headerName;
        }

        public boolean isGenerateWhenMissing() {
            return generateWhenMissing;
        }

        public void setGenerateWhenMissing(
                boolean generateWhenMissing
        ) {
            this.generateWhenMissing =
                    generateWhenMissing;
        }

        public boolean isIncludeInResponse() {
            return includeInResponse;
        }

        public void setIncludeInResponse(
                boolean includeInResponse
        ) {
            this.includeInResponse =
                    includeInResponse;
        }
    }

    public static class Logging {

        /**
         * Ativa os defaults corporativos de logging.
         */
        private boolean enabled = true;

        /**
         * Formato nativo do Spring Boot:
         * logstash, ecs ou gelf.
         */
        private String format = "logstash";

        /**
         * Ambiente incluído no JSON.
         */
        private String environment = "local";

        /**
         * Geração de arquivo é opt-in.
         * Em containers, o padrão será stdout.
         */
        private boolean fileEnabled = false;

        /**
         * Nome do arquivo quando fileEnabled=true.
         */
        private String fileName =
                "logs/application.json";

        /**
         * Limite do stack trace incluído no JSON.
         */
        private int stacktraceMaxLength = 4096;

        /**
         * Profundidade máxima do stack trace.
         */
        private int stacktraceMaxThrowableDepth = 30;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(
                boolean enabled
        ) {
            this.enabled = enabled;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(
                String format
        ) {
            this.format = format;
        }

        public String getEnvironment() {
            return environment;
        }

        public void setEnvironment(
                String environment
        ) {
            this.environment = environment;
        }

        public boolean isFileEnabled() {
            return fileEnabled;
        }

        public void setFileEnabled(
                boolean fileEnabled
        ) {
            this.fileEnabled = fileEnabled;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(
                String fileName
        ) {
            this.fileName = fileName;
        }

        public int getStacktraceMaxLength() {
            return stacktraceMaxLength;
        }

        public void setStacktraceMaxLength(
                int stacktraceMaxLength
        ) {
            this.stacktraceMaxLength =
                    stacktraceMaxLength;
        }

        public int getStacktraceMaxThrowableDepth() {
            return stacktraceMaxThrowableDepth;
        }

        public void setStacktraceMaxThrowableDepth(
                int stacktraceMaxThrowableDepth
        ) {
            this.stacktraceMaxThrowableDepth =
                    stacktraceMaxThrowableDepth;
        }
    }

    public static class Metrics {

        /**
         * Ativa os defaults corporativos de métricas.
         */
        private boolean enabled = true;

        /**
         * Ambiente adicionado como tag nas métricas.
         */
        private String environment = "local";

        /**
         * Adiciona a tag application.
         */
        private boolean applicationTagEnabled = true;

        /**
         * Adiciona a tag environment.
         */
        private boolean environmentTagEnabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(
                boolean enabled
        ) {
            this.enabled = enabled;
        }

        public String getEnvironment() {
            return environment;
        }

        public void setEnvironment(
                String environment
        ) {
            this.environment = environment;
        }

        public boolean isApplicationTagEnabled() {
            return applicationTagEnabled;
        }

        public void setApplicationTagEnabled(
                boolean applicationTagEnabled
        ) {
            this.applicationTagEnabled =
                    applicationTagEnabled;
        }

        public boolean isEnvironmentTagEnabled() {
            return environmentTagEnabled;
        }

        public void setEnvironmentTagEnabled(
                boolean environmentTagEnabled
        ) {
            this.environmentTagEnabled =
                    environmentTagEnabled;
        }
    }

    public static class Actuator {

        private boolean enabled = true;

        private String basePath =
                "/actuator";

        private String exposure =
                "health,info,metrics";

        private String showDetails =
                "never";

        private boolean probesEnabled =
                true;

        private boolean additionalProbePaths =
                false;

        private boolean infoEnabled =
                true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }

        public String getExposure() {
            return exposure;
        }

        public void setExposure(String exposure) {
            this.exposure = exposure;
        }

        public String getShowDetails() {
            return showDetails;
        }

        public void setShowDetails(String showDetails) {
            this.showDetails = showDetails;
        }

        public boolean isProbesEnabled() {
            return probesEnabled;
        }

        public void setProbesEnabled(
                boolean probesEnabled) {

            this.probesEnabled =
                    probesEnabled;
        }

        public boolean isAdditionalProbePaths() {
            return additionalProbePaths;
        }

        public void setAdditionalProbePaths(
                boolean additionalProbePaths) {

            this.additionalProbePaths =
                    additionalProbePaths;
        }

        public boolean isInfoEnabled() {
            return infoEnabled;
        }

        public void setInfoEnabled(
                boolean infoEnabled) {

            this.infoEnabled =
                    infoEnabled;
        }
    }
}