#!/bin/sh

# Error codes
DOCKER_INSTALL_SUCCESS=0
DOCKER_INSTALL_FAILED=1
UNSUPPORTED_OS=2

# Function to check if running on Ubuntu
check_ubuntu() {
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        if [ "$ID" = "ubuntu" ]; then
            echo "✓ Detected Ubuntu $VERSION_ID"
            return 0
        else
            echo "Error: This script only supports Ubuntu"
            return 1
        fi
    else
        echo "Error: Cannot detect operating system"
        return 1
    fi
}

# Function to install Docker using apt
install_docker() {
    echo "Installing Docker..."
    
    # Update package index
    echo "Updating package list..."
    if ! sudo apt-get update >/dev/null 2>&1; then
        echo "Error: Failed to update package list"
        return 1
    fi
    
    # Install Docker
    if sudo apt-get install -y docker.io >/dev/null 2>&1; then
        echo "✓ Docker installed successfully"
        return 0
    else
        echo "Error: Failed to install Docker"
        return 1
    fi
}

# Function to verify installation
verify_installation() {
    if docker --version >/dev/null 2>&1; then
        echo "✓ Docker installation verified"
        docker --version
        return 0
    else
        echo "Error: Docker installation verification failed"
        return 1
    fi
}

# Main function
main() {
    echo "Installing Docker on Ubuntu..."
    
    # Check Ubuntu
    check_ubuntu || exit $UNSUPPORTED_OS
    
    # Check if already installed
    if command -v docker >/dev/null 2>&1; then
        echo "Docker is already installed:"
        docker --version
        exit $DOCKER_INSTALL_SUCCESS
    fi
    
    # Install and setup
    install_docker || exit $DOCKER_INSTALL_FAILED
    verify_installation || exit $DOCKER_INSTALL_FAILED
    
    echo "🎉 Docker installation completed!"
    
    exit $DOCKER_INSTALL_SUCCESS
}

# Run main
main "$@"
