package main

import (
	"context"
	"flag"
	"fmt"
	"log"
	"os"
	"os/signal"
	"strings"
	"sync"
	"syscall"
	"time"

	"github.com/IBM/sarama"
)

// MessageBuffer holds buffered messages and their total size
type MessageBuffer struct {
	messages  []MessageData
	totalSize int
	mutex     sync.Mutex
}

// MessageData represents a single message with metadata
type MessageData struct {
	Topic     string
	Partition int32
	Offset    int64
	Key       string
	Value     string
	Timestamp time.Time
	Size      int
}

// BufferedSink handles message buffering with configurable size limit
type BufferedSink struct {
	buffer      *MessageBuffer
	maxSizeKB   int
	maxSizeBytes int
	verbose     bool
}

// SinkConsumer implements the Sarama ConsumerGroupHandler interface
type SinkConsumer struct {
	sink *BufferedSink
}

func main() {
	// Command line flags for configuration
	var (
		brokers    = flag.String("brokers", "localhost:9092", "Comma-separated list of Kafka brokers")
		groupID    = flag.String("group", "sink-group", "Consumer group ID")
		bufferKB   = flag.Int("buffer", 1, "Buffer size in KB (default: 1KB)")
		allTopics  = flag.Bool("all-topics", true, "Listen to all topics (default: true)")
		topic      = flag.String("topic", "", "Specific topic to consume from (overrides all-topics if specified)")
		verbose    = flag.Bool("verbose", false, "Enable verbose logging (default: false)")
	)
	flag.Parse()

	// Validate buffer size
	if *bufferKB < 1 {
		log.Fatal("Buffer size must be at least 1KB")
	}

	// Parse broker addresses
	brokerList := strings.Split(*brokers, ",")
	for i, broker := range brokerList {
		brokerList[i] = strings.TrimSpace(broker)
	}

	// Determine topics to subscribe to
	var topicsToSubscribe []string
	if *topic != "" {
		// Use specific topic if provided
		topicsToSubscribe = []string{*topic}
		log.Printf("Starting Kafka Sink with %dKB buffer - listening to specific topic: %s", *bufferKB, *topic)
	} else if *allTopics {
		// Get all available topics
		var err error
		topicsToSubscribe, err = getAllTopics(brokerList, *verbose)
		if err != nil {
			log.Fatalf("Failed to get topics: %v", err)
		}
		if len(topicsToSubscribe) == 0 {
			log.Fatal("No topics found on the Kafka cluster")
		}
		log.Printf("Starting Kafka Sink with %dKB buffer - listening to all topics (%d found): %s", 
			*bufferKB, len(topicsToSubscribe), strings.Join(topicsToSubscribe, ", "))
	} else {
		log.Fatal("No topics specified. Use -topic for specific topic or -all-topics=true for all topics")
	}

	// Create buffered sink
	sink := &BufferedSink{
		buffer: &MessageBuffer{
			messages:  make([]MessageData, 0),
			totalSize: 0,
		},
		maxSizeKB:    *bufferKB,
		maxSizeBytes: *bufferKB * 1024, // Convert KB to bytes
		verbose:      *verbose,
	}

	// Create consumer group configuration
	config := sarama.NewConfig()
	config.Consumer.Return.Errors = true
	config.Consumer.Group.Rebalance.Strategy = sarama.BalanceStrategyRoundRobin
	config.Consumer.Offsets.Initial = sarama.OffsetNewest

	// Create consumer group
	consumerGroup, err := sarama.NewConsumerGroup(brokerList, *groupID, config)
	if err != nil {
		log.Fatalf("Error creating consumer group client: %v", err)
	}
	defer consumerGroup.Close()

	// Set up signal handling for graceful shutdown
	ctx, cancel := context.WithCancel(context.Background())
	signals := make(chan os.Signal, 1)
	signal.Notify(signals, syscall.SIGINT, syscall.SIGTERM)

	// Consumer handler
	consumer := &SinkConsumer{sink: sink}

	// Start consuming in a goroutine
	go func() {
		for {
			if err := consumerGroup.Consume(ctx, topicsToSubscribe, consumer); err != nil {
				log.Printf("Error from consumer: %v", err)
				return
			}
			if ctx.Err() != nil {
				return
			}
		}
	}()

	// Handle consumer group errors
	go func() {
		for err := range consumerGroup.Errors() {
			log.Printf("Consumer group error: %v", err)
		}
	}()

	log.Printf("Kafka Sink started. Consuming from %d topic(s) on brokers %s", len(topicsToSubscribe), strings.Join(brokerList, ","))
	log.Printf("Topics: %s", strings.Join(topicsToSubscribe, ", "))
	log.Printf("Buffer configured for %dKB (%d bytes)", *bufferKB, sink.maxSizeBytes)
	if *verbose {
		log.Println("Verbose logging enabled")
	}
	log.Println("Press Ctrl+C to stop...")

	// Wait for termination signal
	<-signals
	log.Println("Termination signal received. Shutting down...")
	cancel()

	// Flush remaining buffer before exit
	log.Println("Flushing remaining buffer...")
	sink.flushBuffer()

	// Wait for consumer to finish
	time.Sleep(1 * time.Second)
	log.Println("Sink stopped.")
}

// getAllTopics retrieves all available topics from the Kafka cluster
func getAllTopics(brokers []string, verbose bool) ([]string, error) {
	if verbose {
		log.Printf("Connecting to Kafka brokers: %s", strings.Join(brokers, ", "))
	}
	
	// Create a client to get cluster metadata
	config := sarama.NewConfig()
	config.Version = sarama.V2_6_0_0 // Use a stable version
	
	client, err := sarama.NewClient(brokers, config)
	if err != nil {
		return nil, fmt.Errorf("failed to create Kafka client: %w", err)
	}
	defer client.Close()

	if verbose {
		log.Println("Successfully connected to Kafka cluster")
	}

	// Get all topics
	topics, err := client.Topics()
	if err != nil {
		return nil, fmt.Errorf("failed to get topics: %w", err)
	}

	if verbose {
		log.Printf("Found %d total topics in cluster: %s", len(topics), strings.Join(topics, ", "))
	}

	// Filter out internal Kafka topics (those starting with underscore)
	var filteredTopics []string
	var internalTopics []string
	for _, topic := range topics {
		if !strings.HasPrefix(topic, "_") && !strings.HasPrefix(topic, "__") {
			filteredTopics = append(filteredTopics, topic)
		} else {
			internalTopics = append(internalTopics, topic)
		}
	}

	if verbose && len(internalTopics) > 0 {
		log.Printf("Filtered out %d internal topics: %s", len(internalTopics), strings.Join(internalTopics, ", "))
	}

	if verbose {
		log.Printf("Selected %d user topics for consumption", len(filteredTopics))
	}

	return filteredTopics, nil
}

// Setup is run at the beginning of a new session, before ConsumeClaim
func (consumer *SinkConsumer) Setup(session sarama.ConsumerGroupSession) error {
	if consumer.sink.verbose {
		log.Printf("Consumer setup - Member ID: %s, Generation ID: %d", session.MemberID(), session.GenerationID())
		log.Printf("Assigned partitions: %v", session.Claims())
	}
	log.Println("Sink consumer setup completed")
	return nil
}

// Cleanup is run at the end of a session, once all ConsumeClaim goroutines have exited
func (consumer *SinkConsumer) Cleanup(session sarama.ConsumerGroupSession) error {
	if consumer.sink.verbose {
		log.Printf("Consumer cleanup - Member ID: %s, Generation ID: %d", session.MemberID(), session.GenerationID())
	}
	log.Println("Sink consumer cleanup completed")
	return nil
}

// ConsumeClaim must start a consumer loop of ConsumerGroupClaim's Messages()
func (consumer *SinkConsumer) ConsumeClaim(session sarama.ConsumerGroupSession, claim sarama.ConsumerGroupClaim) error {
	if consumer.sink.verbose {
		log.Printf("Started consuming from topic: %s, partition: %d", claim.Topic(), claim.Partition())
	}
	
	for {
		select {
		case message := <-claim.Messages():
			if message == nil {
				if consumer.sink.verbose {
					log.Printf("Received nil message from topic: %s, partition: %d", claim.Topic(), claim.Partition())
				}
				return nil
			}

			if consumer.sink.verbose {
				log.Printf("Received message - Topic: %s, Partition: %d, Offset: %d, Key: %s, Value length: %d bytes", 
					message.Topic, message.Partition, message.Offset, string(message.Key), len(message.Value))
			}

			// Create message data
			msgData := MessageData{
				Topic:     message.Topic,
				Partition: message.Partition,
				Offset:    message.Offset,
				Key:       string(message.Key),
				Value:     string(message.Value),
				Timestamp: message.Timestamp,
				Size:      len(message.Key) + len(message.Value),
			}

			// Add to buffer
			consumer.sink.addMessage(msgData)

			// Mark message as processed
			session.MarkMessage(message, "")
			
			if consumer.sink.verbose {
				log.Printf("Message processed and marked - Topic: %s, Partition: %d, Offset: %d", 
					message.Topic, message.Partition, message.Offset)
			}

		case <-session.Context().Done():
			if consumer.sink.verbose {
				log.Printf("Context done - stopping consumption from topic: %s, partition: %d", claim.Topic(), claim.Partition())
			}
			return nil
		}
	}
}

// addMessage adds a message to the buffer and flushes if size limit is reached
func (sink *BufferedSink) addMessage(msg MessageData) {
	sink.buffer.mutex.Lock()
	defer sink.buffer.mutex.Unlock()

	// Add message to buffer
	sink.buffer.messages = append(sink.buffer.messages, msg)
	sink.buffer.totalSize += msg.Size

	if sink.verbose {
		percentFull := float64(sink.buffer.totalSize) / float64(sink.maxSizeBytes) * 100
		log.Printf("Buffered message [%d/%d bytes, %.1f%% full]: Topic: %s, Partition: %d, Offset: %d, Size: %d bytes", 
			sink.buffer.totalSize, sink.maxSizeBytes, percentFull, msg.Topic, msg.Partition, msg.Offset, msg.Size)
	}

	// Check if buffer size limit is reached
	if sink.buffer.totalSize >= sink.maxSizeBytes {
		log.Printf("Buffer size limit reached (%d bytes). Flushing buffer...", sink.buffer.totalSize)
		sink.flushBufferUnsafe()
	}
}

// flushBuffer safely flushes the buffer
func (sink *BufferedSink) flushBuffer() {
	sink.buffer.mutex.Lock()
	defer sink.buffer.mutex.Unlock()
	sink.flushBufferUnsafe()
}

// flushBufferUnsafe flushes the buffer without acquiring mutex (must be called with mutex held)
func (sink *BufferedSink) flushBufferUnsafe() {
	if len(sink.buffer.messages) == 0 {
		if sink.verbose {
			log.Println("Buffer is empty, nothing to flush")
		}
		return
	}

	if sink.verbose {
		log.Printf("Starting buffer flush - %d messages, %d total bytes", 
			len(sink.buffer.messages), sink.buffer.totalSize)
	}

	log.Printf("=== FLUSHING BUFFER: %d messages, %d total bytes ===", 
		len(sink.buffer.messages), sink.buffer.totalSize)

	// Print all buffered messages
	for i, msg := range sink.buffer.messages {
		timestamp := msg.Timestamp.Format("2006-01-02 15:04:05")
		if sink.verbose {
			// Include headers and more detail in verbose mode
			fmt.Printf("[%d] [%s] Topic: %s | Partition: %d | Offset: %d | Key: '%s' | Value: '%s' | Size: %d bytes\n",
				i+1, timestamp, msg.Topic, msg.Partition, msg.Offset, msg.Key, msg.Value, msg.Size)
		} else {
			// Standard output
			fmt.Printf("[%d] [%s] Topic: %s | Partition: %d | Offset: %d | Key: %s | Value: %s | Size: %d bytes\n",
				i+1, timestamp, msg.Topic, msg.Partition, msg.Offset, msg.Key, msg.Value, msg.Size)
		}
	}

	log.Printf("=== BUFFER FLUSH COMPLETED: %d messages processed ===", len(sink.buffer.messages))

	if sink.verbose {
		log.Printf("Buffer cleared - resetting counters")
	}

	// Clear buffer
	sink.buffer.messages = sink.buffer.messages[:0]
	sink.buffer.totalSize = 0
}

// getBufferStats returns current buffer statistics
func (sink *BufferedSink) getBufferStats() (int, int, float64) {
	sink.buffer.mutex.Lock()
	defer sink.buffer.mutex.Unlock()
	
	messageCount := len(sink.buffer.messages)
	currentSize := sink.buffer.totalSize
	percentFull := float64(currentSize) / float64(sink.maxSizeBytes) * 100
	
	return messageCount, currentSize, percentFull
}
