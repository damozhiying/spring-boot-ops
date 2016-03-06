Spring Boot Ops Example
======================

Small example app that makes Spring Boot more ops-friendly.

Includes reusable configuration primitives and a VM to test metric reporting.

Example app is a single-endpoint http service with yaml-configurable metrics reporters and health indicators.

Ties together
- [gradle](http://gradle.org)
- [Spring Boot](http://projects.spring.io/spring-boot/)
- [Dropwizard Metrics](https://dropwizard.github.io/metrics/3.1.0/)
- Spring [HealthIndicators](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/health/class-use/HealthIndicator.html)
- [Graphite](http://graphite.wikidot.com/)/[Grafana](http://grafana.org/)

Spring Boot
-----------------

To run the app, use gradle:

```sh
./gradlew bootRun
```

Then, head to [localhost:8081](localhost:8081). You can check out the
Spring Boot Actuactor's metrics endpoint at [localhost:8081/metrics](localhost:8081/metrics) and see
health indicators in action at [localhost:8081/health](localhost:8081/health).

Metrics Configuration
-----------------

Default metrics configuration uses a graphite reporter directed at `localhost:2003`

```yaml
metrics:
  # report to graphite
  type: graphite

  # report every 30 seconds
  interval: 30
  timeUnit: seconds

  # prefix all metrics with hello.example
  prefix: hello.example

  # graphite settings
  host: localhost
  port: 2003
```

Other options for `type` are `console` and `slf4j`. The `slf4j` reporter takes an optional
`logger` parameter, which can be used to direct output to a specific logger:

```yaml
metrics:
  type: slf4j
  interval: 30
  timeUnit: seconds
  prefix: hello.example
  logger: com.mycompany.metrics
```

Health Indicators
-----------------

The example app also registers a configurable example `HealthIndicator`. It will report
as healthy as long as the root endpoint, `localhost:8081/`, has been hit a certain number of times:

```yaml
health:
  minHealthyHits: 5
```

By default, the app will report as unhealthy until it has received 5 hits.
The health can be inspected at [localhost:8081/health](localhost:8081/health):

```json
{
    "status": "UP",
    "traffic": {
        "status": "UP",
        "hits": 9,
        "minimum": 5
    },
    "diskSpace": {
        "status": "UP",
        "total": 108899942400,
        "free": 53076361216,
        "threshold": 10485760
    }
}
```

Graphite VM
------------

Best way to see your monitoring in action is to spin up the Graphite VM using [Chef](https://www.chef.io/chef
) and [Test Kitchen](http://kitchen.ci).

To start the vm, you'll need to get your hands on [Vagrant](https://www.vagrantup.com/) and [Bundler](https://bundler.io/):

```sh
cd graphite_vm
bundle install
```

Use `kitchen` to create and provision the vm:

```sh
kitchen converge
```

Tying it all together
---------------------

Once you've booted the graphite VM, head to [localhost:8080/](localhost:8080/) and
explore the codahale metrics as well as the metrics that ship with Spring Boot by
default. You can use the [Graphite Dashboard](localhost:8080/dashboard/) to
configure any combination of the provided stats.


#### Grafana

To view the same values in Grafana, check out the grafana dashboard at
[localhost:3000](localhost:3000). Default login is `admin`/`admin`.
You'll have to add `localhost:8000` as a graphite
data source in the grafana UI to get access to the underlying graphite data.

TODO
----
- There's some stuff in here that can be unit tested
- Add VMs with nagios/nrpe or zabbix to monitor `HealthIndicators`

License
----------

MIT
