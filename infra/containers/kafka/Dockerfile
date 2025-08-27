# Use Ubuntu 24.10 as base image
FROM ubuntu:24.10

# Server Configuration
ENV KAKFA_PORT=9092
ENV KRAFT_PORT=9093
ENV SOCKET_SEND_BUFFER_BYTES=102400
ENV SOCKET_RECEIVE_BUFFER_BYTES=102400
ENV SOCKET_REQUEST_MAX_BYTES=104857600
ENV NUM_NETWORK_THREADS=3
ENV NUM_IO_THREADS=8
ENV NUM_PARTITIONS=1
ENV NUM_RECOVERY_THREADS_PER_DATA_DIR=1
ENV OFFSETS_TOPIC_REPLICATION_FACTOR=1
ENV TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1
ENV TRANSACTION_STATE_LOG_MIN_ISR=1
ENV LOG_RETENTION_HOURS=168
ENV LOG_RETENTION_CHECK_INTERVAL_MS=300000
ENV LOG_SEGMENT_BYTES=1073741824

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# PATH
ENV KAFKA_HOME=/opt/kafka
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
ENV PATH=$PATH:$KAFKA_HOME/bin

# Update system and install dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    nano \
    net-tools \
    uuid-runtime \
    wget
RUN rm -rf /var/lib/apt/lists/*

# Create kafka user and group
RUN groupadd kafka
RUN useradd -r -g kafka -d /opt -s /bin/false kafka

#Setup Kafka
ENV KAFKA_VERSION=2.13-4.0.0
ENV KAFKA_DOWNLOAD_URL=https://dlcdn.apache.org/kafka/4.0.0/kafka_2.13-4.0.0.tgz

RUN wget -O /tmp/kafka_$KAFKA_VERSION.tgz $KAFKA_DOWNLOAD_URL
RUN echo "Extracting Kafka from local file..."
RUN tar -xzf /tmp/kafka_$KAFKA_VERSION.tgz -C /opt
RUN mv /opt/kafka_$KAFKA_VERSION $KAFKA_HOME
RUN rm /tmp/kafka_$KAFKA_VERSION.tgz

# Create Kafka directories
RUN mkdir -p $KAFKA_HOME/logs
RUN mkdir -p $KAFKA_HOME/kraft-logs

# Create KRaft configuration for Kafka 4.0.0
RUN cp $KAFKA_HOME/config/server.properties $KAFKA_HOME/config/server.properties.backup
RUN echo "# KRaft mode configuration" > $KAFKA_HOME/config/server.properties
RUN echo "process.roles=broker,controller" >> $KAFKA_HOME/config/server.properties
RUN echo "node.id=1" >> $KAFKA_HOME/config/server.properties
RUN echo "controller.quorum.voters=1@localhost:$KRAFT_PORT" >> $KAFKA_HOME/config/server.properties
RUN echo "listeners=PLAINTEXT://:$KAKFA_PORT,CONTROLLER://:$KRAFT_PORT" >> $KAFKA_HOME/config/server.properties
RUN echo "inter.broker.listener.name=PLAINTEXT" >> $KAFKA_HOME/config/server.properties
RUN echo "advertised.listeners=PLAINTEXT://localhost:$KAKFA_PORT" >> $KAFKA_HOME/config/server.properties
RUN echo "controller.listener.names=CONTROLLER" >> $KAFKA_HOME/config/server.properties
RUN echo "listener.security.protocol.map=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT" >> $KAFKA_HOME/config/server.properties
RUN echo "num.network.threads=$NUM_NETWORK_THREADS" >> $KAFKA_HOME/config/server.properties
RUN echo "num.io.threads=$NUM_IO_THREADS" >> $KAFKA_HOME/config/server.properties
RUN echo "socket.send.buffer.bytes=$SOCKET_SEND_BUFFER_BYTES" >> $KAFKA_HOME/config/server.properties
RUN echo "socket.receive.buffer.bytes=$SOCKET_RECEIVE_BUFFER_BYTES" >> $KAFKA_HOME/config/server.properties
RUN echo "socket.request.max.bytes=$SOCKET_REQUEST_MAX_BYTES" >> $KAFKA_HOME/config/server.properties
RUN echo "log.dirs=$KAFKA_HOME/kraft-logs" >> $KAFKA_HOME/config/server.properties
RUN echo "num.partitions=$NUM_PARTITIONS" >> $KAFKA_HOME/config/server.properties
RUN echo "num.recovery.threads.per.data.dir=$NUM_RECOVERY_THREADS_PER_DATA_DIR" >> $KAFKA_HOME/config/server.properties
RUN echo "offsets.topic.replication.factor=$OFFSETS_TOPIC_REPLICATION_FACTOR" >> $KAFKA_HOME/config/server.properties
RUN echo "transaction.state.log.replication.factor=$TRANSACTION_STATE_LOG_REPLICATION_FACTOR" >> $KAFKA_HOME/config/server.properties
RUN echo "transaction.state.log.min.isr=$TRANSACTION_STATE_LOG_MIN_ISR" >> $KAFKA_HOME/config/server.properties
RUN echo "log.retention.hours=$LOG_RETENTION_HOURS" >> $KAFKA_HOME/config/server.properties
RUN echo "log.retention.check.interval.ms=$LOG_RETENTION_CHECK_INTERVAL_MS" >> $KAFKA_HOME/config/server.properties
RUN echo "log.segment.bytes=$LOG_SEGMENT_BYTES" >> $KAFKA_HOME/config/server.properties

# Set Kafka ownership
RUN chown -R kafka:kafka $KAFKA_HOME

# Create Kafka startup script (KRaft mode)
RUN echo '#!/bin/bash' > /opt/start-kafka-kraft.sh
RUN echo "cd $KAFKA_HOME" >> /opt/start-kafka-kraft.sh
RUN echo 'echo "Generating cluster UUID..."' >> /opt/start-kafka-kraft.sh
RUN echo 'CLUSTER_UUID=$(bin/kafka-storage.sh random-uuid)' >> /opt/start-kafka-kraft.sh
RUN echo 'echo "Cluster UUID: $CLUSTER_UUID"' >> /opt/start-kafka-kraft.sh
RUN echo 'echo "Formatting Kafka storage..."' >> /opt/start-kafka-kraft.sh
RUN echo 'bin/kafka-storage.sh format -t $CLUSTER_UUID -c config/server.properties' >> /opt/start-kafka-kraft.sh
RUN echo 'echo "Starting Kafka in KRaft mode..."' >> /opt/start-kafka-kraft.sh
RUN echo 'bin/kafka-server-start.sh config/server.properties' >> /opt/start-kafka-kraft.sh
RUN chmod +x /opt/start-kafka-kraft.sh

RUN echo "Kafka installation completed"

# Expose ports
# 9092: Kafka
# 9093: Kafka Controller (KRaft mode)
EXPOSE $KAKFA_PORT $KRAFT_PORT

# Switch to kafka user
USER kafka

# Set working directory
WORKDIR $KAFKA_HOME

# Start Kafka in KRaft mode
CMD ["/opt/start-kafka-kraft.sh"]