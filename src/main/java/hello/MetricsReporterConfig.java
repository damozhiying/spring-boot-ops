package hello;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "metrics")
public class MetricsReporterConfig {

    public enum Type {
        console(
        ) {
            @Override
            ScheduledReporter createReporter(MetricRegistry metricRegistry, MetricsReporterConfig config) {
                return ConsoleReporter
                        .forRegistry(metricRegistry)
                        .build();
            }
        },
        slf4j() {
            @Override
            ScheduledReporter createReporter(MetricRegistry metricRegistry, MetricsReporterConfig config) {
                return Slf4jReporter
                        .forRegistry(metricRegistry)
                        .prefixedWith(config.prefix)
                        .outputTo(LoggerFactory.getLogger(config.logger))
                        .build();
            }
        },
        graphite() {
            @Override
            ScheduledReporter createReporter(MetricRegistry metricRegistry, MetricsReporterConfig config) {
                return GraphiteReporter
                        .forRegistry(metricRegistry)
                        .prefixedWith(config.prefix)
                        .build(new Graphite(config.host, config.port));
            }
        };

        abstract ScheduledReporter createReporter(MetricRegistry metricRegistry, MetricsReporterConfig config);

    }


    int interval = 2;
    TimeUnit timeUnit = TimeUnit.MINUTES;
    Type type = Type.console;
    String prefix = "";
    String host = "localhost";
    int port = 2003;
    String logger = "hello.metrics";

    @Autowired
    MetricRegistry metricRegistry;

    @Bean
    ScheduledReporter scheduledReporter() {
        return type.createReporter(metricRegistry, this);
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

}
