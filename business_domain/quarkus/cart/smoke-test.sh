#!/bin/bash
set -euo pipefail

# Run client
EXECUTION_LOG=$(./mvnw -f cart-appclient/pom.xml quarkus:run)

# Validate log contents
if echo "$EXECUTION_LOG" | grep -q "Retrieving book title from cart: Infinite Jest"; then
    echo "✅ Validation passed: 'Retrieving book title from cart: Infinite Jest' found in response."
else
    echo "❌ Validation failed: 'Retrieving book title from cart: Infinite Jest' not found in response."
    exit 1
fi

if echo "$EXECUTION_LOG" | grep -q "Retrieving book title from cart: Bel Canto"; then
    echo "✅ Validation passed: 'Retrieving book title from cart: Bel Canto' found in response."
else
    echo "❌ Validation failed: 'Retrieving book title from cart: Bel Canto' not found in response."
    exit 1
fi

if echo "$EXECUTION_LOG" | grep -q "Retrieving book title from cart: Kafka on the Shore"; then
    echo "✅ Validation passed: 'Retrieving book title from cart: Kafka on the Shore' found in response."
else
    echo "❌ Validation failed: 'Retrieving book title from cart: Kafka on the Shore' not found in response."
    exit 1
fi

if echo "$EXECUTION_LOG" | grep -q "\"Gravity's Rainbow\" not in cart."; then
    echo "✅ Validation passed: '\"Gravity's Rainbow\" not in cart.' found in response."
else
    echo "❌ Validation failed: '\"Gravity's Rainbow\" not in cart.' not found in response."
    exit 1
fi
