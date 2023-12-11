package org.example;

import jakarta.jms.JMSException;
import org.example.CommandSide.CommandImpl;
import org.example.CommandSide.DomainModel;
import org.example.EventAPI.EventHandler;
import org.example.MovingItem.MovingItemDTO;
import org.example.QuerySide.QueryHandler;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import static org.example.QuerySide.QueryModel.query_database;

public class Main {

    public static void main(String[] args) throws Exception {

        Broker broker = new Broker();
        broker.startBroker();
        EventHandler eventHandler = new EventHandler();

        //TODO: Tests schreiben / fixen
        //TODO: Refactoring

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
        for (Map.Entry<String, MovingItemDTO> entry : query_database.entrySet()) {
            System.out.println("for loop: " + entry.getKey() + " : " + entry.getValue().getName() + ", "
                    + entry.getValue().getValue() + ", " + Arrays.toString(entry.getValue().getLocation()) + ", "
                    + entry.getValue().getNumberOfMoves());
        }
        QueryHandler query = new QueryHandler();
        System.out.println(query.getMovingItemByName("Alice").getName());
        queryGetAll(query);
        queryAtPosition(query, new int[]{3, 5, 7});

        //item.changeValue("Tom", 7);
        /*item.createItem("Lisa", new int[]{6, 7, 8}, 0);
        item.createItem("Otto", new int[]{0, 0, 3}, 0);
        item.createItem("Bob");
        item.moveItem("Bob", new int[]{0, 0, 3});*/

        DomainModel.stop();
        EventHandler.stop();
        broker.stopBroker();
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