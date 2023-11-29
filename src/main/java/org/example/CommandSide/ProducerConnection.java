package org.example.CommandSide;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.EventPrompts.Event;

import javax.jms.*;

//used by CommandHandler/Domainmodel
public class ProducerConnection {
    private static final String topic = "ITEM";
    private final transient Connection connection;
    private final transient Session session;
    private final transient MessageProducer producer;
    private transient Destination ItemTopic;
    private final String brokerUrl = "tcp://localhost:61616";

    public ProducerConnection() throws Exception {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        connection = factory.createConnection("publisher", "password");
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //TODO: Decide on a pattern. In Producer no special implementation.
        // use publish-subscribe
        ItemTopic = session.createTopic(topic);

        // use point-2-point
        ItemTopic = session.createQueue(topic);

        producer = session.createProducer(ItemTopic);
        producer.setTimeToLive(1000);
    }

    protected void sendMessage(Event event) throws JMSException {
        ObjectMessage message = session.createObjectMessage(event);
        System.out.println("sending " + event.id);
        producer.send(message);
    }

    public void close() throws JMSException {
        if (connection != null) connection.close();
    }
}
