package org.example;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.EventPrompts.Event;

public class ConnectionMQ {

    private static final String topic = "ITEM";
    private String brokerUrl = "tcp://localhost:61616";
    private transient ActiveMQConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private transient MessageConsumer consumer;
    private transient MessageProducer producer;
    private transient Destination itemTopic;

    public ConnectionMQ() throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerUrl);
        factory.setTrustAllPackages(true);
        connection = factory.createConnection("consumer", "password");
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // use publish-subscribe
        itemTopic = session.createTopic(topic);
        // use point-2-point
        //itemTopic = session.createQueue(topic);
        consumer = session.createConsumer(itemTopic);
        producer = session.createProducer(itemTopic);
    }

    public void sendMessage(Event event) throws JMSException {
        ObjectMessage message = session.createObjectMessage(event);
        message.setLongProperty("timestamp", System.currentTimeMillis());
        producer.send(message);

    }

    public void close() throws JMSException {
        if (connection != null) connection.close();
    }

    public Message consumeMessage() throws JMSException {
        return consumer.receive();
        /*try {
            consumer.setMessageListener(new Listener());
            System.out.println(consumer.getClass());
        } catch (JMSException e) {
            e.printStackTrace();
        }*/
    }

    /*public void consumeMessageAsync() {
        try {
            consumer.setMessageListener(new Listener());
            System.out.println(consumer.getClass());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }*/
}
