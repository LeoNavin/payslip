#!/bin/bash

# Define the directory where you want to run the JAR file
RUN_DIR="${GITHUB_WORKSPACE}/target"  # Adjust to your JAR file path

# Create the directory if it doesn't exist
mkdir -p $RUN_DIR

# Run the JAR file and capture logs
java -jar $RUN_DIR/your-app.jar > app.log 2>&1 &

# Display the last 10 lines of the log file
echo "Application started. Logs:"
tail -n 10 app.log
