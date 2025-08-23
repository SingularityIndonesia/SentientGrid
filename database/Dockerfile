# Use Ubuntu 24.10 as base image
FROM ubuntu:24.10

# Server Configuration
ENV DB_PORT=9094
ENV POSTGRES_DB=thingsdb
ENV POSTGRES_USER=thingsuser
ENV POSTGRES_PASSWORD=thingspass

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# PATH
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
ENV PGDATA=/var/lib/postgresql/data
ENV DB_HOME=/var/lib/postgresql

# Update system and install dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    postgresql \
    postgresql-contrib \
    nano \
    net-tools \
    uuid-runtime \
    sudo
RUN rm -rf /var/lib/apt/lists/*

# Create database user and group (postgresql user already exists from package)
RUN usermod -d $DB_HOME postgres
RUN mkdir -p $DB_HOME
RUN mkdir -p $PGDATA

# Setup PostgreSQL
ENV POSTGRE_VERSION=16
RUN echo "PostgreSQL $POSTGRE_VERSION installed from Ubuntu package"

# Initialize PostgreSQL database
RUN service postgresql start && \
    sudo -u postgres psql -c "CREATE USER $POSTGRES_USER WITH PASSWORD '$POSTGRES_PASSWORD';" && \
    sudo -u postgres psql -c "CREATE DATABASE $POSTGRES_DB OWNER $POSTGRES_USER;" && \
    sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DB TO $POSTGRES_USER;" && \
    service postgresql stop

# Configure PostgreSQL
RUN echo "listen_addresses = '*'" >> /etc/postgresql/16/main/postgresql.conf
RUN echo "port = $DB_PORT" >> /etc/postgresql/16/main/postgresql.conf
RUN echo "host all all 0.0.0.0/0 md5" >> /etc/postgresql/16/main/pg_hba.conf

# Create PostgreSQL startup script
RUN echo '#!/bin/bash' > /opt/start-postgres.sh
RUN echo 'echo "Starting PostgreSQL..."' >> /opt/start-postgres.sh
RUN echo 'service postgresql start' >> /opt/start-postgres.sh
RUN echo 'echo "PostgreSQL started on port $DB_PORT"' >> /opt/start-postgres.sh
RUN echo 'echo "Database: $POSTGRES_DB"' >> /opt/start-postgres.sh
RUN echo 'echo "User: $POSTGRES_USER"' >> /opt/start-postgres.sh
RUN echo 'echo "Keeping container running..."' >> /opt/start-postgres.sh
RUN echo 'tail -f /var/log/postgresql/postgresql-16-main.log' >> /opt/start-postgres.sh
RUN chmod +x /opt/start-postgres.sh

# Set PostgreSQL ownership
RUN chown -R postgres:postgres $DB_HOME
RUN chown -R postgres:postgres $PGDATA

RUN echo "PostgreSQL installation completed"

# Expose ports
EXPOSE $DB_PORT

# Switch to postgres user
USER postgres

# Set working directory
WORKDIR $DB_HOME

# Start PostgreSQL Service
CMD ["/opt/start-postgres.sh"]
