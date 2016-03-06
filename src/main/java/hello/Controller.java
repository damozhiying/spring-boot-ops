package hello;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


    @Autowired
    MetricRegistry metricRegistry;

    @RequestMapping("/")
    public String meteredEndpoint() {

        Meter meter = metricRegistry.meter("endpoints.index");
        meter.mark();

        return "Greetings from Spring Boot!\n" +
                "Endpoint has been called " + meter.getCount() +
                " times.";
    }

}
