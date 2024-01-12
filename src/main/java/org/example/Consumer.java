package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.EventPrompts.Event;

import java.util.*;

import static org.example.Main.env;

public class Consumer {

    static String bootstrapServers = "localhost:" + env.getBrokers().getFirst().getPort();
    static String topicName = "Event";
    KafkaConsumer<Integer, Event> consumer;
    ConsumerRecords<Integer, Event> records;

    public Consumer() {
        consumer = new KafkaConsumer<>(getKafkaProperties());
        consumer.subscribe(Collections.singletonList(topicName));
    }

    public List<Event> getEvent(int time) {
        records = consumer.poll(time);
        consumer.seekToBeginning(consumer.assignment());
        List<Event> events = new ArrayList<>();
        for (ConsumerRecord<Integer, Event> record : records) {
            events.add(record.value());
        }
        return events;
    }

    public Map<Event, Long> getEventWithTimestamp(int time) {
        records = consumer.poll(time);
        Map<Event, Long> eventMap = new HashMap<>();
        for (ConsumerRecord<Integer, Event> record : records) {
            eventMap.put(record.value(), record.timestamp());
        }
        return eventMap;
    }

    public void close() {
        consumer.close();
    }

    private Properties getKafkaProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.example.EventPrompts.EventDeserializer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        return props;
    }
}
