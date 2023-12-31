package org.example;

import jakarta.jms.JMSException;
import no.nav.common.KafkaEnvironment;
import org.example.CommandSide.CommandImpl;
import org.example.EventAPI.EventHandler;
import org.example.MovingItem.MovingItemDTO;
import org.example.QuerySide.QueryHandler;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import static java.util.Collections.emptyList;

public class Main {

    //used for embedded -> don't have to use Docker for the Connection
    public static KafkaEnvironment env = new KafkaEnvironment(
            1,
            Arrays.asList("Event"),
            emptyList(),
            false,
            false,
            emptyList(),
            false,
            new Properties()
    );

    public static void main(String[] args) throws Exception {
        //TODO: Update the Tests
        env.start(); //for embedded
        EventHandler eventHandler = new EventHandler();

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
        Thread.sleep(5000);

        CommandImpl item = new CommandImpl();
        item.createItem("Alice");
        item.moveItem("Alice", new int[]{3, 5, 7});
        item.createItem("Tom", new int[]{1, 2, 3}, 0);
        item.changeValue("Tom", 7);
        item.moveItem("Tom", new int[]{2, 3, 4});
        item.createItem("Lisa", new int[]{3, 5, 7}, 0);
        /*item.createItem("Otto", new int[]{0, 0, 3}, 0);
        item.createItem("Bob");
        item.moveItem("Bob", new int[]{0, 0, 3});*/

        Thread.sleep(1000);
        QueryHandler query = new QueryHandler();
        //System.out.println(query.getMovingItemByName("Tom").getName());
        queryGetAll(query);
        queryAtPosition(query, new int[]{3, 5, 7});

    }

    public static void queryGetAll(QueryHandler query) {
        System.out.println("All items:");
        Enumeration<MovingItemDTO> allItems = query.getMovingItems();
        while (allItems.hasMoreElements()) {
            MovingItemDTO itemsIteration = allItems.nextElement();
            System.out.println(itemsIteration.getName() + ", " + itemsIteration.getValue() + ", " + Arrays.toString(itemsIteration.getLocation()) + ", " + itemsIteration.getNumberOfMoves());
        }
    }

    public static void queryAtPosition(QueryHandler query, int[] position) {
        System.out.println("Item at position:");
        Enumeration<MovingItemDTO> itemsAtPosition = query.getMovingItemsAtPosition(position);
        while (itemsAtPosition.hasMoreElements()) {
            MovingItemDTO itemAtPosition = itemsAtPosition.nextElement();
            System.out.println(itemAtPosition.getName() + ", " + itemAtPosition.getValue() + ", " + Arrays.toString(itemAtPosition.getLocation()) + ", " + itemAtPosition.getNumberOfMoves());
        }
    }
}