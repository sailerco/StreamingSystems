package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.EventPrompts.Event;

import java.util.Properties;

import static org.example.Main.env;

public class Producer {
    static String bootstrapServers = "localhost:" + env.getBrokers().getFirst().getPort();
    static String topic = "Event";
    static KafkaProducer producer;

    public Producer() {
        Properties properties = getKafkaProperties();
        producer = new KafkaProducer<String, String>(properties);
    }

    private static Properties getKafkaProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.example.EventPrompts.EventSerializer");
        return props;
    }

    public void sendObjectMessage(String a, Event event) {
        ProducerRecord<String, Event> message = new ProducerRecord<>(topic, a, event);
        producer.send(message);
    }
}