#!/bin/sh

# Check if Docker is installed
if ! command -v docker >/dev/null 2>&1; then
    echo "Docker not found. Installing Docker..."
    exit 1
fi

# Check if Docker daemon is running
if ! docker info >/dev/null 2>&1; then
    echo "Docker daemon not running. Starting Docker..."
    exit 1
fi

# Build the Docker image
echo "Building Docker image..."
docker build -t sentientgrid-kafka infra/containers/kafka/
