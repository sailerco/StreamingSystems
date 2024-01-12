package org.example;

import jakarta.jms.JMSException;
import org.example.CommandSide.CommandImpl;
import org.example.CommandSide.DomainModel;
import org.example.EventAPI.EventHandler;
import org.example.MovingItem.MovingItemDTO;
import org.example.QuerySide.QueryHandler;

import java.util.Arrays;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) throws Exception {
        Broker broker = new Broker();
        broker.startBroker();

        EventHandler eventHandler = new EventHandler();

        System.out.println("Processing Data...");
        CommandImpl item = new CommandImpl();
        item.createItem("Tom", new int[]{1, 2, 3}, 0);
        item.changeValue("Tom", 7);
        item.moveItem("Tom", new int[]{2, 3, 4});
        item.createItem("Alice");
        item.moveItem("Alice", new int[]{3, 5, 7});
        item.createItem("Bob");
        item.deleteItem("Bob");
        item.createItem("Otto");

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    eventHandler.processMessage();
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

        Thread.sleep(1000);
        System.out.println("------");
        System.out.println("Producer to Consumer mean time was " + eventHandler.calculateMeanTime() + "ms");
        System.out.println("------ Query Side ------");
        QueryHandler query = new QueryHandler();

        System.out.println("All items:");
        printMovingItems(query.getMovingItems());

        System.out.println("Get by Name Alice");
        MovingItemDTO itemByName = query.getMovingItemByName("Alice");
        System.out.println(itemByName.getName() + ", " + itemByName.getValue() + ", " + Arrays.toString(itemByName.getLocation()) + ", " + itemByName.getNumberOfMoves());

        System.out.println("Item at position:");
        printMovingItems(query.getMovingItemsAtPosition(new int[]{3, 5, 7}));

        DomainModel.stop();
        EventHandler.stop();
        broker.stopBroker();
    }

    private static void printMovingItems(Enumeration<MovingItemDTO> items) {
        while (items.hasMoreElements()) {
            MovingItemDTO item = items.nextElement();
            System.out.println(item.getName() + ", " + item.getValue() + ", " + Arrays.toString(item.getLocation()) + ", " + item.getNumberOfMoves());
        }
    }
}