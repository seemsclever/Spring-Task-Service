package com.seemsclever;

import com.seemsclever.entities.Task;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskKafkaProducer {

    public final KafkaTemplate<String, Object> kafkaTemplate;

    public TaskKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskToKafka(Task task){
        kafkaTemplate.send("tasks", task);
    }

}
