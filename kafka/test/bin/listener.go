package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"strings"
	"syscall"
	"time"

	"github.com/IBM/sarama"
)

func main() {
	// Kafka broker addresses
	brokers := []string{"localhost:9092"}
	
	// Topic to consume from
	topic := "test-topic"
	
	// Consumer group ID
	groupID := "listener-group"

	// Create consumer group configuration
	config := sarama.NewConfig()
	config.Consumer.Return.Errors = true
	config.Consumer.Group.Rebalance.Strategy = sarama.BalanceStrategyRoundRobin
	config.Consumer.Offsets.Initial = sarama.OffsetNewest

	// Create consumer group Retry parameters
    maxRetries := 5
    delay := 3 * time.Second

    var consumerGroup sarama.ConsumerGroup
    var err error

    // Try Create consumer group
	for i := 1; i <= maxRetries; i++ {
        consumerGroup, err = sarama.NewConsumerGroup(brokers, groupID, config)
        if err == nil {
            log.Println("Successfully created consumer group")
            break
        }

        log.Printf("Attempt %d/%d: Error creating consumer group: %v", i, maxRetries, err)
        if i < maxRetries {
            log.Printf("Retrying in %v...", delay)
            time.Sleep(delay)
        }
    }

	if err != nil {
		log.Fatalf("Error creating consumer group client: %v", err)
	}
	defer consumerGroup.Close()

	// Set up signal handling for graceful shutdown
	ctx, cancel := context.WithCancel(context.Background())
	signals := make(chan os.Signal, 1)
	signal.Notify(signals, syscall.SIGINT, syscall.SIGTERM)

	// Consumer handler
	consumer := &Consumer{}

	// Start consuming in a goroutine
	go func() {
		for {
			if err := consumerGroup.Consume(ctx, []string{topic}, consumer); err != nil {
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

	log.Printf("Kafka listener started. Consuming from topic '%s' on brokers %s", topic, strings.Join(brokers, ","))
	log.Println("Press Ctrl+C to stop...")

	// Wait for termination signal
	<-signals
	log.Println("Termination signal received. Shutting down...")
	cancel()

	// Wait for consumer to finish
	time.Sleep(1 * time.Second)
	log.Println("Listener stopped.")
}

// Consumer represents a Sarama consumer group consumer
type Consumer struct{}

// Setup is run at the beginning of a new session, before ConsumeClaim
func (consumer *Consumer) Setup(sarama.ConsumerGroupSession) error {
	log.Println("Consumer setup completed")
	return nil
}

// Cleanup is run at the end of a session, once all ConsumeClaim goroutines have exited
func (consumer *Consumer) Cleanup(sarama.ConsumerGroupSession) error {
	log.Println("Consumer cleanup completed")
	return nil
}

// ConsumeClaim must start a consumer loop of ConsumerGroupClaim's Messages()
func (consumer *Consumer) ConsumeClaim(session sarama.ConsumerGroupSession, claim sarama.ConsumerGroupClaim) error {
	for {
		select {
		case message := <-claim.Messages():
			if message == nil {
				return nil
			}
			
			// Log the incoming message
			timestamp := message.Timestamp.Format("2006-01-02 15:04:05")
			log.Printf("[%s] Received message - Topic: %s, Partition: %d, Offset: %d, Key: %s, Value: %s",
				timestamp,
				message.Topic,
				message.Partition,
				message.Offset,
				string(message.Key),
				string(message.Value),
			)
			
			// Mark message as processed
			session.MarkMessage(message, "")
			
		case <-session.Context().Done():
			return nil
		}
	}
}
