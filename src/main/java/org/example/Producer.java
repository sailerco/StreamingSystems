package org.example;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.EventPrompts.Event;

public class Producer {
    static String bootstrapServers = "localhost:29092";
    static String topicName = "";

    static KafkaProducer producer;
    private static Properties getKafkaProperties(){
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers); //TODO: correct url
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer", "org.example.EventPrompts.EventSerializer");
        props.put("group.id", "0");
        return props;
    }

    public Producer(){
        Properties properties = getKafkaProperties();
        producer = new KafkaProducer<String, String>(properties);
    }

    public void sendObjectMessage(String a, Event event){
        String topic = "Event";
        ProducerRecord<String, Event> message = new ProducerRecord<>(topic, a, event);
        producer.send(message);
    }
}