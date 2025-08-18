package infra.kafka

import infra.config.KafkaConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.LoggerFactory
import java.util.concurrent.Future

class KafkaProducerService {
    
    private val logger = LoggerFactory.getLogger(KafkaProducerService::class.java)
    private val producer = KafkaProducer<String, String>(KafkaConfig.getProducerProperties())
    
    suspend fun <T> sendMessage(topic: String, key: String?, message: T): Result<RecordMetadata> {
        return try {
            val jsonMessage: String = when (message) {
                is String -> message
                else -> /*Json.encodeToString(message)*/ TODO()
            }
            
            val record = ProducerRecord(topic, key, jsonMessage)
            val future: Future<RecordMetadata> = producer.send(record) { metadata, exception ->
                if (exception != null) {
                    logger.error("Failed to send message to topic $topic", exception)
                } else {
                    logger.info("Message sent successfully to topic: ${metadata.topic()}, partition: ${metadata.partition()}, offset: ${metadata.offset()}")
                }
            }
            
            Result.success(future.get()) // This blocks until the message is sent
        } catch (e: Exception) {
            logger.error("Error sending message to Kafka topic $topic", e)
            Result.failure(e)
        }
    }
    
    fun sendMessageAsync(topic: String, key: String?, message: String, callback: ((RecordMetadata?, Exception?) -> Unit)? = null) {
        val record = ProducerRecord(topic, key, message)
        producer.send(record) { metadata, exception ->
            callback?.invoke(metadata, exception)
            if (exception != null) {
                logger.error("Failed to send message to topic $topic", exception)
            } else {
                logger.info("Message sent successfully to topic: ${metadata?.topic()}, partition: ${metadata?.partition()}, offset: ${metadata?.offset()}")
            }
        }
    }
    
    fun close() {
        try {
            producer.close()
            logger.info("Kafka producer closed successfully")
        } catch (e: Exception) {
            logger.error("Error closing Kafka producer", e)
        }
    }
}
