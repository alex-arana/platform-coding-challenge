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



