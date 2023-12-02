package org.example.EventAPI;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ConsumerConnection {
    private static final String topic = "ITEM";
    private String brokerUrl = "tcp://localhost:61616";
    private transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private transient MessageConsumer consumer;
    private transient Destination itemTopic;

    public ConsumerConnection() throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerUrl);
        connection = factory.createConnection("consumer", "password");
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //		session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        // use publish-subscribe
        itemTopic = session.createTopic(topic);

        // use point-2-point
        itemTopic = session.createQueue(topic);

        consumer = session.createConsumer(itemTopic);
    }

    protected Message consumeMessage() throws JMSException {
        return consumer.receive(1000);
        /*try {
            consumer.setMessageListener(new Listener());
            System.out.println(consumer.getClass());
        } catch (JMSException e) {
            e.printStackTrace();
        }*/
    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }
}
