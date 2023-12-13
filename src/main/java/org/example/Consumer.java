package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.EventPrompts.Event;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {

    static String bootstrapServers = "localhost:29092"; // usually of the form cell-1.streaming.<region>.oci.oraclecloud.com:9092 ;
    static String tenancyName = "<OCI_tenancy_name>";
    static String username = "<your_OCI_username>";
    static String streamPoolId = "<stream_pool_OCID>";
    static String authToken = "<your_OCI_user_auth_token>"; // from step 8 of Prerequisites section
    static String streamOrKafkaTopicName = "<topic_stream_name>"; // from step 2 of Prerequisites section
    KafkaConsumer<Integer, Event> consumer;
    ConsumerRecords<Integer, Event> records;
    public Consumer(String topicName){
        consumer = new KafkaConsumer<>(getKafkaProperties());
        consumer.subscribe(Collections.singletonList(topicName));
        records = consumer.poll(10000);
    }

    public void getEvent(){
        System.out.println("size of records polled is "+ records.count());
        for (ConsumerRecord<Integer, Event> record : records) {
            System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
            System.out.println(record.value().id);
        }
        consumer.commitSync();
    }

    public void close(){
        consumer.close();
    }
    private static Properties getKafkaProperties(){
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.example.EventPrompts.EventDeserializer");
        props.put("group.id", "0");
        //props.put("session.timeout.ms", "20000");


        /*final String value = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
                + tenancyName + "/"
                + username + "/"
                + streamPoolId + "\" "
                + "password=\""
                + authToken + "\";";
        props.put("sasl.jaas.config", value);*/

        return props;
    }
}
