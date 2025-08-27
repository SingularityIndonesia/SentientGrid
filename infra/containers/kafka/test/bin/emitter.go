package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"
	"time"

	"github.com/IBM/sarama"
)

func main() {
	// Kafka broker addresses
	brokers := []string{"localhost:9092"}
	
	// Topic to produce to
	topic := "test-topic"

	// Create producer configuration
	config := sarama.NewConfig()
	config.Producer.RequiredAcks = sarama.WaitForAll
	config.Producer.Retry.Max = 3
	config.Producer.Return.Successes = true

	// Create producer
	producer, err := sarama.NewSyncProducer(brokers, config)
	if err != nil {
		log.Fatalf("Failed to create producer: %v", err)
	}
	defer producer.Close()

	fmt.Println("Kafka Message Emitter")
	fmt.Printf("Connected to brokers: %s\n", strings.Join(brokers, ","))
	fmt.Printf("Publishing to topic: %s\n", topic)
	fmt.Println("Type your messages and press Enter to send. Type 'quit' to exit.")
	fmt.Println(strings.Repeat("-", 50))

	// Create scanner for reading user input
	scanner := bufio.NewScanner(os.Stdin)

	messageCount := 0

	for {
		fmt.Print("Message: ")
		
		// Read input from user
		if !scanner.Scan() {
			break
		}
		
		input := strings.TrimSpace(scanner.Text())
		
		// Check for quit command
		if strings.ToLower(input) == "quit" || strings.ToLower(input) == "exit" {
			fmt.Println("Goodbye!")
			break
		}
		
		// Skip empty messages
		if input == "" {
			continue
		}
		
		messageCount++
		
		// Create message with timestamp and counter
		messageKey := fmt.Sprintf("msg-%d", messageCount)
		messageValue := fmt.Sprintf("[%s] %s", time.Now().Format("2006-01-02 15:04:05"), input)
		
		// Create Kafka message
		message := &sarama.ProducerMessage{
			Topic: topic,
			Key:   sarama.StringEncoder(messageKey),
			Value: sarama.StringEncoder(messageValue),
		}
		
		// Send message
		partition, offset, err := producer.SendMessage(message)
		if err != nil {
			log.Printf("Failed to send message: %v", err)
			continue
		}
		
		fmt.Printf("✓ Message sent successfully - Partition: %d, Offset: %d\n", partition, offset)
	}
	
	if err := scanner.Err(); err != nil {
		log.Printf("Error reading input: %v", err)
	}
	
	fmt.Printf("Total messages sent: %d\n", messageCount)
}
