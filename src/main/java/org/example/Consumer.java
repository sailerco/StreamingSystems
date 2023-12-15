package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.EventPrompts.Event;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static org.example.Main.env;

public class Consumer {

    static String bootstrapServers = "localhost:" + env.getBrokers().getFirst().getPort();
    static String topicName = "Event";
    KafkaConsumer<Integer, Event> consumer;
    ConsumerRecords<Integer, Event> records;
    Boolean fromStart;

    public Consumer(Boolean fromStart) {
        this.fromStart = fromStart;
        consumer = new KafkaConsumer<>(getKafkaProperties());
        consumer.subscribe(Collections.singletonList(topicName));
    }

    public List<Event> getEvent(int time) {
        records = consumer.poll(time);
        //System.out.println("size of records polled is " + records.count() + " " + fromStart);
        for (ConsumerRecord<Integer, Event> record : records) {
            System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
        }
        if (fromStart) consumer.seekToBeginning(consumer.assignment());
        return StreamSupport.stream(records.spliterator(), false).toList().stream().map(record -> record.value()).toList();
    }

    public void close() {
        consumer.close();
    }

    private Properties getKafkaProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.example.EventPrompts.EventDeserializer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        if (fromStart) props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //props.put("session.timeout.ms", "20000");
        return props;
    }
}
