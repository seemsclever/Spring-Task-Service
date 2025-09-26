package com.seemsclever;

import com.seemsclever.entities.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskKafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(TaskKafkaProducer.class);

    public final KafkaTemplate<String, Object> kafkaTemplate;

    public TaskKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskToKafka(Task task){
        try {
            logger.info("Attempting to send task to Kafka: {}", task.getId());

            kafkaTemplate.send("tasks", task)
                    .whenComplete((result, error) -> {
                        if (error == null) {
                            logger.info("Successfully sent task {} to Kafka. Offset: {}",
                                    task.getId(), result.getRecordMetadata().offset());
                        } else {
                            logger.error("Failed to send task {} to Kafka: {}",
                                    task.getId(), error.getMessage());
                        }
                    });

        } catch (Exception e) {
            logger.error("Error sending task to Kafka: {}", e.getMessage(), e);
        }
    }

}
