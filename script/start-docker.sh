#!/bin/sh

# Error codes
DOCKER_START_FAILED=1
DOCKER_START_SUCCESS=0

# Function to start Docker on SystemD systems (Ubuntu/modern Linux)
start_docker_systemd() {
    echo "Attempting to start Docker using systemctl..."
    if sudo systemctl start docker >/dev/null 2>&1; then
        echo "Docker daemon started successfully via systemctl"
        return 0
    else
        echo "Failed to start Docker daemon via systemctl"
        return 1
    fi
}

# Function to start Docker on SysV systems (older Ubuntu/Linux)
start_docker_sysv() {
    echo "Attempting to start Docker using service command..."
    if sudo service docker start >/dev/null 2>&1; then
        echo "Docker daemon started successfully via service"
        return 0
    else
        echo "Failed to start Docker daemon via service"
        return 1
    fi
}

# Function to wait for Docker to be ready
wait_for_docker() {
    echo "Waiting for Docker daemon to initialize..."
    sleep 3
}

# Function to verify Docker is running
verify_docker_running() {
    if docker info >/dev/null 2>&1; then
        echo "✓ Docker daemon is now running"
        return 0
    else
        echo "Error: Docker daemon failed to start properly"
        return 1
    fi
}

# Main execution
main() {
    echo "Attempting to start Docker daemon..."
    
    # Try SystemD first (modern Ubuntu), then fallback to SysV (older Ubuntu)
    if command -v systemctl >/dev/null 2>&1; then
        start_docker_systemd || exit $DOCKER_START_FAILED
    elif command -v service >/dev/null 2>&1; then
        start_docker_sysv || exit $DOCKER_START_FAILED
    else
        echo "Error: Unable to start Docker on this system"
        echo "This script supports Ubuntu/Linux systems with systemctl or service commands"
        exit $DOCKER_START_FAILED
    fi
    
    # Wait for Docker to initialize
    wait_for_docker
    
    # Verify Docker is running
    if verify_docker_running; then
        exit $DOCKER_START_SUCCESS
    else
        exit $DOCKER_START_FAILED
    fi
}

# Run main function
main "$@"
