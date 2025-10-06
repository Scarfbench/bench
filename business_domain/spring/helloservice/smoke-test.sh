#!/bin/bash
set -euo pipefail

HELLO_SERVICE_URL=${HELLO_SERVICE_URL:-http://localhost:8080/HelloServiceBeanService}
REQUEST_FILE="request.xml"

# Make the SOAP request
RESPONSE=$(curl --silent --show-error --fail \
  --header "content-type: text/xml" \
  --data @"$REQUEST_FILE" "$HELLO_SERVICE_URL")

# Validate response content
if echo "$RESPONSE" | grep -q "Hello, John."; then
    echo "✅ Validation passed: 'Hello, John.' found in response."
else
    echo "❌ Validation failed: 'Hello, John.' not found in response."
    exit 1
fi
