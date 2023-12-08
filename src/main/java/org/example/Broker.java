package org.example;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;

import java.util.ArrayList;
import java.util.List;


public class Broker {

    BrokerService broker;

    public void startBroker() throws Exception {
        broker = new BrokerService();
        broker.setBrokerName("myBroker");
        broker.setDataDirectory("data/");
        SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();
        List<AuthenticationUser> users = new ArrayList<>();
        //users.add(new AuthenticationUser("admin", "password", "admins,publishers,consumers"));
        users.add(new AuthenticationUser("publisher", "password", "publishers,consumers"));
        users.add(new AuthenticationUser("consumer", "password", "consumers"));
        // todo: users.add(new AuthenticationUser("guest", "password", "guests"));
        authentication.setUsers(users);

        broker.setPlugins(new BrokerPlugin[]{authentication});
        broker.addConnector("tcp://localhost:61616");
        //broker.addConnector("ssl://localhost:61617");
        //broker.addConnector("mqtt://localhost:1883");
        broker.start();
    }

    public void stopBroker() throws Exception {
        broker.stop();
    }
}
