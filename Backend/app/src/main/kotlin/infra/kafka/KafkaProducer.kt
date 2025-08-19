package infra.kafka

import infra.config.KafkaConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.LoggerFactory

class KafkaProducerService {
    private val logger = LoggerFactory.getLogger(KafkaProducerService::class.java)
    private val producer = KafkaProducer<String, String>(KafkaConfig.getProducerProperties())

    suspend fun sendMessage(topic: String, key: String?, message: String): Result<RecordMetadata> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val record = ProducerRecord(topic, key, message)

                producer.send(record) { metadata, exception ->
                    if (exception != null) {
                        logger.error("Failed to send message to topic $topic", exception)
                    } else {
                        logger.info("Message sent successfully to topic: ${metadata.topic()}, partition: ${metadata.partition()}, offset: ${metadata.offset()}")
                    }
                }.get()
            }
        }.onFailure { e ->
            logger.error("Error sending message to Kafka topic $topic", e)
        }
    }

    fun close() {
        runCatching {
            producer.close()
        }.onSuccess {
            logger.info("Kafka producer closed successfully")
        }.onFailure {
            logger.error("Error closing Kafka producer", it)
        }
    }
}
