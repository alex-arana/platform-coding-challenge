# platform-coding-challenge
Commonwealth Interview - Platform Coding Challenge

## Overview
This project implements a solution to the Commonwealth Coding Challenge as of February 2016. For details about the said challenge please refer to the document "Platform Engineering - Coding Challenge.pdf".

In summary the problem requests to write a program, in a programming language of choice, to read in a list of URLs from a file and then perform a GET request to each website to measure the request latency outputting it in a JSON file.

## Implementation
The solution delivered here is a Java project implemented as a multi-module Maven project containing the following modules:

- platform.coding.challenge. Top-level project. Used to trigger the multi-module build.
- latency-measurement-schema. Generates application model classes from an XML schema.
- latency-measurement-service. Implements the business logic to calculate remote network latency.
- latency-measurement-endpoint. Exposes the above business functionality as a REST web service.
- latency-measurement-cli. Implements a standalone command-line utility to read a list of URLs from a file, calculate the latency for each entry and then output the results to a file using JSON format.

## Building the solution
In order to build the program, the following is required

- Java 8 JDK
- Maven 3.x
- A working internet connection (to download required library dependencies)

In order to start a project build clone this project to your development machine then at the top-level directory type:
$ mvn clean package

After a few minutes and if everything goes well you will se the following output:

```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO]
[INFO] Latency Measurement Builder ....................... SUCCESS [1.103s]
[INFO] Latency Measurement Schema ........................ SUCCESS [3.271s]
[INFO] Latency Measurement Business Service .............. SUCCESS [4.033s]
[INFO] Latency Measurement REST endpoint ................. SUCCESS [8.624s]
[INFO] Latency Measurement CLI ........................... SUCCESS [0.293s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 17.464s
[INFO] Finished at: Sun Feb 07 02:34:52 EST 2016
[INFO] Final Memory: 43M/254M
[INFO] ------------------------------------------------------------------------
```

## Running the program
After building the application you can run the latency measurement command-line utility by performing the following steps:

1. At the top-level directory run the following command to start the embedded web server which launches the application backend:

```
# you must have successfully run a maven build in order to start the web server
platform-coding-challenge $ ./latency-measurement-endpoint/run.sh
```

You can check if the application backend has started successfully by opening the following URL in your browser. This will launch the [Swagger UI](https://github.com/swagger-api/swagger-ui) front-end which can be used to test the REST API in standalone mode.

[http://localhost:8080/latency-measurement-endpoint/swagger-ui/index.html](http://localhost:8080/latency-measurement-endpoint/swagger-ui/index.html#!/latency-measurement-service-operations/getRoundTripLatency)

2. At the top-level directory run the following command to start the embedded web server which launches the client application:

```
platform-coding-challenge $ ./latency-measurement-cli/run.sh
```

If the program has run successfully you should see something resembling the following output on your console:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.2.6.RELEASE)

2016-02-07 15:59:11.199  INFO 59293 --- [           main] .m.c.LatencyMeasurementClientApplication : Starting LatencyMeasurementClientApplication on Alexs-MacBook-Air.local with PID 59293 (/Users/alex/Workspaces/GitHub/platform-coding-challenge/latency-measurement-cli/target/classes started by alex in /Users/alex/Workspaces/GitHub/platform-coding-challenge/latency-measurement-cli)
2016-02-07 15:59:11.243  INFO 59293 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@692a79e4: startup date [Sun Feb 07 15:59:11 AEDT 2016]; root of context hierarchy
2016-02-07 15:59:12.004  INFO 59293 --- [           main] f.a.AutowiredAnnotationBeanPostProcessor : JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
2016-02-07 15:59:12.744  INFO 59293 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2016-02-07 15:59:12.753  INFO 59293 --- [           main] .l.m.c.s.i.LatencyMeasurementServiceImpl : Performing network latency tests using input file: URL.txt
2016-02-07 15:59:18.797  INFO 59293 --- [           main] .m.c.LatencyMeasurementClientApplication : LatencyMeasurementService returned {
  "responses" : [ {
    "url" : "http://www.xkcd.com/443?foo=bar",
    "latency_Ms" : 183.0,
    "count" : 3,
    "timestamp" : "2016-02-07T04:59:15.599Z",
    "min" : 167,
    "max" : 1975,
    "range" : 1808,
    "average" : 775.0,
    "median" : 183.0
  }, {
    "url" : "https://twitter.com",
    "latency_Ms" : 1005.0,
    "count" : 3,
    "timestamp" : "2016-02-07T04:59:16.849Z",
    "min" : 977,
    "max" : 1595,
    "range" : 618,
    "average" : 1192.3333333333333,
    "median" : 1005.0
  }, {
    "url" : "https://www.facebook.com:443",
    "latency_Ms" : 1369.0,
    "count" : 3,
    "timestamp" : "2016-02-07T04:59:17.448Z",
    "min" : 711,
    "max" : 1957,
    "range" : 1246,
    "average" : 1345.6666666666667,
    "median" : 1369.0
  }, {
    "url" : "https://www.google.com",
    "latency_Ms" : 1356.0,
    "count" : 3,
    "timestamp" : "2016-02-07T04:59:18.641Z",
    "min" : 1338,
    "max" : 2537,
    "range" : 1199,
    "average" : 1743.6666666666667,
    "median" : 1356.0
  } ]
}
2016-02-07 15:59:18.800  INFO 59293 --- [           main] .m.c.LatencyMeasurementClientApplication : Latency measurement results have been saved to file '/Users/alex/Workspaces/GitHub/platform-coding-challenge/latency-measurement-cli/latency.json'
2016-02-07 15:59:18.801  INFO 59293 --- [           main] .m.c.LatencyMeasurementClientApplication : Started LatencyMeasurementClientApplication in 7.807 seconds (JVM running for 15.674)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 14.270s
[INFO] Finished at: Sun Feb 07 15:59:18 AEDT 2016
[INFO] Final Memory: 41M/350M
[INFO] ------------------------------------------------------------------------

```

## Additional Notes

- If you want to change the port on which the web server is run you will need to modify the run script (run.sh) located in the `latency-measurement-endpoint` subfolder. Accordingly, you also need to modify the web service URL which is part of the CLI configuration within `latency-measurement-cli/application.properties`.

- By default, the CLI program uses a file called `URL.txt` as input and it creates or writes to an output file called `latency.json`. If you wish to change either of those parameters you can do so by passing the following parameters to the client run script (run.sh):

```
$ ./latency-measurement-cli/run.sh --input="path_to_input_file" --output="path_to_output_file"
```

