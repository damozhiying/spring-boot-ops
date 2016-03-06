package hello;

import com.codahale.metrics.ScheduledReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@ConditionalOnBean(ScheduledReporter.class)
public class MetricsService implements AutoCloseable {

    @Autowired
    MetricsReporterConfig metricsReporterConfig;

    @Autowired
    ScheduledReporter scheduledReporter;

    @PostConstruct
    public void startReporter() {
        scheduledReporter.start(metricsReporterConfig.interval, metricsReporterConfig.timeUnit);
    }

    @PreDestroy
    @Override
    public void close() throws Exception {
        scheduledReporter.stop();
    }
}
