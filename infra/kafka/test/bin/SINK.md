# Kafka Sink - Buffered Message Consumer

A configurable Kafka consumer that buffers messages and flushes them when a size threshold is reached. Perfect for monitoring, debugging, and batch processing of Kafka messages.

## Features

- 🔄 **Multi-Topic Support**: Listen to all topics or specific topics
- 📦 **Configurable Buffering**: Set buffer size in KB for batch processing
- 🔍 **Automatic Topic Discovery**: Discovers all available topics dynamically
- 🛡️ **Thread-Safe Operations**: Safe concurrent message processing
- 📊 **Detailed Logging**: Comprehensive message and buffer status logging
- 🔍 **Verbose Mode**: Extra detailed logging for debugging and monitoring
- 🚦 **Graceful Shutdown**: Proper cleanup with buffer flushing on exit

## Installation & Setup

1. **Prerequisites**: Go 1.21+ and access to a Kafka cluster

2. **Dependencies**: The project uses Sarama Kafka client
   ```bash
   cd test/
   go mod tidy
   ```

3. **Build** (optional):
   ```bash
   go build -o sink bin/sink.go
   ```

## Usage

### Basic Usage

```bash
# Listen to all topics with default 1KB buffer
go run bin/sink.go

# Or if built:
./sink
```

### Command Line Options

| Flag | Default | Description |
|------|---------|-------------|
| `-brokers` | `localhost:9092` | Comma-separated list of Kafka brokers |
| `-group` | `sink-group` | Consumer group ID |
| `-buffer` | `1` | Buffer size in KB |
| `-all-topics` | `true` | Listen to all topics |
| `-topic` | `""` | Specific topic to consume from |
| `-verbose` | `false` | Enable verbose logging for detailed information |

### Common Usage Examples

#### 1. Monitor All Topics (Default)
```bash
go run bin/sink.go
```

#### 2. Verbose Mode for Debugging
```bash
go run bin/sink.go -verbose
```

#### 3. Custom Buffer with Verbose
```bash
go run bin/sink.go -verbose -buffer 10 -topic "orders"
```

#### 4. Production Setup
```bash
go run bin/sink.go \
  -brokers "prod-kafka1:9092,prod-kafka2:9092" \
  -group "production-monitor" \
  -buffer 50 \
  -verbose
```

## Verbose Mode Benefits

The `-verbose` flag provides:
- **Connection Details**: Broker connection status and cluster metadata
- **Topic Discovery**: Complete topic discovery and filtering process
- **Consumer Group Info**: Member ID, generation ID, partition assignments
- **Message Processing**: Individual message details and processing status
- **Buffer Statistics**: Percentage full and detailed size information

### Example Verbose Output

```bash
go run bin/sink.go -verbose -buffer 5
```

```
Connecting to Kafka brokers: localhost:9092
Successfully connected to Kafka cluster
Found 4 total topics in cluster: orders, users, events, _consumer_offsets
Filtered out 1 internal topics: _consumer_offsets
Selected 3 user topics for consumption
Consumer setup - Member ID: sink-group-12345, Generation ID: 1
Assigned partitions: map[orders:[0] users:[0] events:[0]]
Started consuming from topic: orders, partition: 0
Received message - Topic: orders, Partition: 0, Offset: 123, Key: order-001, Value length: 45 bytes
Buffered message [45/5120 bytes, 0.9% full]: Topic: orders, Partition: 0, Offset: 123, Size: 45 bytes
Message processed and marked - Topic: orders, Partition: 0, Offset: 123
```

## Use Cases

### Development & Testing
```bash
# Verbose monitoring with small buffer for immediate feedback
go run bin/sink.go -verbose -buffer 2
```

### Production Monitoring
```bash
# Monitor all production topics
go run bin/sink.go -brokers "prod-broker:9092" -buffer 50
```

### Debugging Issues
```bash
# Focus on specific topic with detailed logging
go run bin/sink.go -topic "error-logs" -verbose -buffer 5
```

## Troubleshooting

### Use Verbose Mode
For any issues, enable verbose mode to get detailed information:
```bash
go run bin/sink.go -verbose
```

This shows:
- Broker connection attempts and status
- Complete topic discovery process
- Consumer group coordination details
- Individual message processing flow

## Contributing

When modifying the sink:
1. Test with both normal and verbose modes
2. Ensure verbose logging is helpful but not overwhelming
3. Update this documentation for new features

## License

Part of the ThingsBE project - see main project license.
