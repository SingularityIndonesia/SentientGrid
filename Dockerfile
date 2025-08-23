# Use Ubuntu 24.10 as base image
FROM ubuntu:24.10

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive
ENV KAFKA_VERSION=2.13-3.6.0
ENV KAFKA_HOME=/opt/kafka
ENV PATH=$PATH:$KAFKA_HOME/bin

# Update system and install dependencies
RUN apt-get update && apt-get install -y \
    openjdk-11-jdk \
    wget \
    curl \
    nano \
    net-tools \
    && rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# Create kafka user
RUN groupadd kafka && \
    useradd -r -g kafka -d $KAFKA_HOME -s /bin/false kafka

# Download and install Kafka
RUN wget -O /tmp/kafka_$KAFKA_VERSION.tgz \
    https://downloads.apache.org/kafka/3.6.0/kafka_$KAFKA_VERSION.tgz && \
    tar -xzf /tmp/kafka_$KAFKA_VERSION.tgz -C /opt && \
    mv /opt/kafka_$KAFKA_VERSION $KAFKA_HOME && \
    rm /tmp/kafka_$KAFKA_VERSION.tgz

# Create necessary directories
RUN mkdir -p $KAFKA_HOME/logs && \
    chown -R kafka:kafka $KAFKA_HOME

# Note: Using default Kafka configurations
# If you need custom configurations, create a config/ directory with:
# - server.properties 
# - zookeeper.properties

# Create startup script
RUN echo '#!/bin/bash' > /opt/start-kafka.sh && \
    echo 'cd $KAFKA_HOME' >> /opt/start-kafka.sh && \
    echo 'echo "Starting Zookeeper..."' >> /opt/start-kafka.sh && \
    echo 'bin/zookeeper-server-start.sh config/zookeeper.properties &' >> /opt/start-kafka.sh && \
    echo 'sleep 10' >> /opt/start-kafka.sh && \
    echo 'echo "Starting Kafka..."' >> /opt/start-kafka.sh && \
    echo 'bin/kafka-server-start.sh config/server.properties' >> /opt/start-kafka.sh && \
    chmod +x /opt/start-kafka.sh

# Expose ports
# 2181: Zookeeper
# 9092: Kafka
EXPOSE 2181 9092

# Switch to kafka user
USER kafka

# Set working directory
WORKDIR $KAFKA_HOME

# Start Kafka
CMD ["/opt/start-kafka.sh"]