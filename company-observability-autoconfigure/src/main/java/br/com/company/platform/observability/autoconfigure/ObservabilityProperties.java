package br.com.company.platform.observability.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "company.observability"
)
public class ObservabilityProperties {

    private boolean enabled = true;

    private final Correlation correlation =
            new Correlation();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Correlation getCorrelation() {
        return correlation;
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

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(
                String headerName) {

            this.headerName = headerName;
        }

        public boolean isGenerateWhenMissing() {
            return generateWhenMissing;
        }

        public void setGenerateWhenMissing(
                boolean generateWhenMissing) {

            this.generateWhenMissing =
                    generateWhenMissing;
        }

        public boolean isIncludeInResponse() {
            return includeInResponse;
        }

        public void setIncludeInResponse(
                boolean includeInResponse) {

            this.includeInResponse =
                    includeInResponse;
        }
    }
}