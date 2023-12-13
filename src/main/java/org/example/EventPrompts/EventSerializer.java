package org.example.EventPrompts;

import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class EventSerializer implements Serializer<Event> {

    @Override
    public byte[] serialize(String topic, Event event) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objStream = new ObjectOutputStream(byteStream)){
            objStream.writeObject(event);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
