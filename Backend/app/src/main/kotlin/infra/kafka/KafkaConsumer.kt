package infra.kafka

import infra.config.KafkaConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.LoggerFactory
import java.time.Duration

class KafkaConsumerService {
    private val logger = LoggerFactory.getLogger(KafkaConsumerService::class.java)
    private val consumer = KafkaConsumer<String, String>(KafkaConfig.getConsumerProperties())
    private var consumerJob: Job? = null

    fun startConsuming(
        topics: List<String>,
        messageHandler: suspend (ConsumerRecord<String, String>) -> Unit,
        scope: CoroutineScope
    ) {
        consumerJob = scope.launch {
            try {
                consumer.subscribe(topics)
                logger.info("Started consuming from topics: $topics")

                while (isActive) {
                    val records = consumer.poll(Duration.ofMillis(1000))

                    for (record in records) {
                        try {
                            logger.debug("Received message: topic=${record.topic()}, partition=${record.partition()}, offset=${record.offset()}, key=${record.key()}")
                            messageHandler(record)

                            // Manual commit after successful processing
                            consumer.commitSync()
                        } catch (e: Exception) {
                            logger.error("Error processing message from topic ${record.topic()}", e)
                            // Decide whether to continue or stop processing based on your error handling strategy
                        }
                    }
                }
            } catch (e: Exception) {
                logger.error("Error in Kafka consumer", e)
            } finally {
                consumer.close()
                logger.info("Kafka consumer closed")
            }
        }
    }

    fun stopConsuming() {
        consumerJob?.cancel()
        consumer.wakeup()
        logger.info("Kafka consumer stop requested")
    }

    fun close() {
        stopConsuming()
        consumer.close()
    }
}
