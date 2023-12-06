package org.example.EventAPI;

import jakarta.jms.TextMessage;

import org.joda.time.Instant;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

public class Listener implements MessageListener {

    public void onMessage(Message message) {
        try {
            String text = ((TextMessage)message).getText();
            System.out.println("Receiving: " + text + " at " + new Instant());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}