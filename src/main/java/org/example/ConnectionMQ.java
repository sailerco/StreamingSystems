package org.example;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.EventPrompts.Event;

public class ConnectionMQ {

    private static final String topic = "ITEM";
    private final transient Connection connection;
    private final transient Session session;
    private transient MessageConsumer consumer;
    private transient MessageProducer producer;

    public ConnectionMQ(String username) throws JMSException {
        String brokerUrl = "tcp://localhost:61616";
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        factory.setTrustAllPackages(true);
        connection = factory.createConnection(username, "password");
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // use publish-subscribe
        Destination itemTopic = session.createTopic(topic);
        // use point-2-point
        //Destination itemTopic = session.createQueue(topic);
        if (username.equals("consumer")) consumer = session.createConsumer(itemTopic);
        else producer = session.createProducer(itemTopic);
    }

    public void sendMessage(Event event) throws JMSException {
        ObjectMessage message = session.createObjectMessage(event);
        message.setJMSTimestamp(System.currentTimeMillis());
        producer.send(message);
    }

    public void close() throws JMSException {
        if (connection != null) connection.close();
    }

    public Message consumeMessage() throws JMSException {
        if (connection != null) return consumer.receive();
        else return null;
    }
}