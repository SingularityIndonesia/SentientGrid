# Kafka Test Tools

This directory contains simple test tools for testing the Kafka service.

## Prerequisites

1. Make sure your Kafka service is running (use `../script/run.sh` to start the Docker container)
2. Install Go dependencies:
   ```bash
   cd test
   go mod tidy
   ```

## Usage

### Listener (Consumer)
The listener will consume and log all incoming messages from the `test-topic`:

```bash
go run listener.go
```

The listener will:
- Connect to Kafka at `localhost:9092`
- Subscribe to the `test-topic` topic
- Log all incoming messages with timestamp, partition, offset, key, and value
- Handle graceful shutdown with Ctrl+C

### Emitter (Producer)
The emitter is a CLI tool that lets you send messages interactively:

```bash
go run emitter.go
```

The emitter will:
- Connect to Kafka at `localhost:9092`
- Prompt you to enter messages
- Send each message to the `test-topic` with a timestamp
- Show confirmation when messages are sent successfully
- Type `quit` or `exit` to stop

## Example Usage

1. Start your Kafka service:
   ```bash
   ../script/kafka_run.sh
   ```

2. In one terminal, start the listener:
   ```bash
   cd test
   go run listener.go
   ```

3. In another terminal, start the emitter:
   ```bash
   cd test
   go run emitter.go
   ```

4. Type messages in the emitter terminal and see them appear in the listener terminal

## Configuration

Both tools use these default settings:
- **Broker**: `localhost:9092`
- **Topic**: `test-topic`
- **Consumer Group**: `listener-group`

You can modify these values in the source code if needed.
