package org.example.EventPrompts;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class EventDeserializer implements Deserializer<Event> {
    @Override
    public Event deserialize(String topic, byte[] data) {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
             ObjectInputStream objStream = new ObjectInputStream(byteStream)) {
            return (Event) objStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
