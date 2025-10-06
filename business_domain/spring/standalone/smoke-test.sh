#!/bin/bash
set -euo pipefail

# Since the app does not do anything, we just check the build
BUILD_LOG=$(./mvnw clean package)

# Validate build log contents
if echo "$BUILD_LOG" | grep -q "BUILD SUCCESS"; then
    echo "✅ Validation passed: 'BUILD SUCCESS' found in response."
else
    echo "❌ Validation failed: 'BUILD SUCCESS' not found in response."
    exit 1
fi

if echo "$BUILD_LOG" | grep -q "spring-boot"; then
    echo "✅ Validation passed: 'spring-boot' found in response."
else
    echo "❌ Validation failed: 'spring-boot' not found in response."
    exit 1
fi