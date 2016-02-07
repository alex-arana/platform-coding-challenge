#!/bin/sh
cd $(dirname $0)

# This is the port number that the embedded web server will run on
PORT=8080

# We detect the java executable to use according to the following algorithm:
#
# 1. If it is located in JAVA_HOME, then we use that; or
# 2. Use the java that is in the command path.
#
if [ -d "$JAVA_HOME" -a -x "$JAVA_HOME/bin/java" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD=java
fi

$JAVACMD -Xmx128m -jar target/dependency/jetty-runner.jar --port $PORT target/*.war

exit
