package hello;

import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "health")
public class HealthIndicatorConfig {

    int minHealthyHits;

    @Autowired
    MetricRegistry metricRegistry;

    @Bean
    public HealthIndicator traffic() {
        return () -> {
            final long count = metricRegistry.meter("endpoints.index").getCount();
            return count < minHealthyHits ? down(count) : up(count);
        };
    }

    private Health up(long count) {
        return Health.up().withDetail("hits", count).withDetail("minimum", minHealthyHits).build();
    }

    private Health down(long count) {
        return Health.down().withDetail("hits", count).withDetail("minimum", minHealthyHits).build();
    }

    public int getMinHealthyHits() {
        return minHealthyHits;
    }

    public void setMinHealthyHits(int minHealthyHits) {
        this.minHealthyHits = minHealthyHits;
    }
}
