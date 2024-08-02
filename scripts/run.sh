#!/bin/bash

export BASE_SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "BASE_SCRIPTS_DIR: $BASE_SCRIPTS_DIR"

# The .env file is located one directory above the scripts folder
ENV_FILE="${BASE_SCRIPTS_DIR}/../.env"

# Check if the .env file exists
if [ -f "$ENV_FILE" ]; then
    echo "Loading environment variables from $ENV_FILE"
    # Import all environment variables from .env file
    set -o allexport && source "$ENV_FILE" && set +o allexport
else
    echo "Error: .env file not found at $ENV_FILE"
    exit 1
fi

echo "Current JAVA_HOME: $JAVA_HOME"

export JAVA_HOME=$(/usr/libexec/java_home -v 21)
echo "Current JAVA_HOME after export: $JAVA_HOME"

mvn clean install
echo "mvn is run..."

./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-javaagent:${PWD}/opt/newrelic/newrelic.jar"
