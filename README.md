### Prerequisites

* `Java 21` should be installed --> `export JAVA_HOME=$(/usr/libexec/java_home -v 21)`
* `Docker` should be installed
* `Maven` should be installed
* `pgAdmin`/`DBeaver` can be installed (Optional)
* `New Relic` is disabled by default. Properties can be set (Optional)
    * If there is a `New Relic` account, log in to [New Relic](https://login.newrelic.com/login)
        1. Generate `api-key` and replace the `NEW_RELIC_LICENSE_KEY` value with yours in the [.env](.env) file
        2. Replace the `NEW_RELIC_ENDPOINT` value with yours in the [.env](.env) file
        3. In the [.env](.env) file, if `api-key` and `endpoint` are present with the correct
           values, `NEW_RELIC_ENABLED` can be `true` and `NEW_RELIC_INFRA_AGENT_REPLICAS` can be greater than zero(0)

-----

### How to start the application

1. Run `mvn clean install` or `mvn clean package`
2. Run `mvn spring-boot:run` or `./mvnw spring-boot:run`
    - Run [./scripts/run.sh](scripts%2Frun.sh) to start the application with `New Relic Java agent` (Optional)
    - `docker-compose --profile start_application up -d --build` can be run to create and run a Docker image of the
      application with `New Relic Java agent` (Optional)

-----

### How to test the application

* Run `mvn test` or `mvn clean install` or `mvn clean package` command to run all the tests
* Swagger Url: http://localhost:8080/swagger-ui.html
* Test User to Get JWT Token via `/auth/signin` API:
    * `username: admin_user`
    * `password: test1234`
    * Copy the generated token and paste it to the following popup in `Swagger`
        * ![img.png](img.png)
* Actuator Url: http://localhost:8080/actuator
* Metric Url: http://localhost:8080/actuator/metrics

-----

### `docker-compose` contains the followings

* PostgreSQL DB connection details
    * `POSTGRES_USER: postgres`
    * `POSTGRES_PASSWORD: postgres`
    * `Port: 5432`
* Zipkin: http://localhost:9411/
* Prometheus: http://localhost:9090/graph
* Grafana: http://localhost:3000/
    * `Email or username: admin`
    * `Password: admin`
    * Add datasource
        * Select Prometheus
        * Prometheus Url: http://prometheus:9090/
        * Save & test
* Kafka-UI: http://localhost:9091/

-----

### References

* https://github.com/newrelic/micrometer-registry-newrelic?tab=readme-ov-file#archival-notice
* https://docs.micrometer.io/micrometer/reference/implementations/new-relic.html

-----