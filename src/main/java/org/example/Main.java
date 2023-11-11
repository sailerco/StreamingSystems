package org.example;

import org.example.CommandSide.CommandImpl;
import org.example.MovingItem.MovingItemDTO;
import org.example.MovingItem.MovingItemDTOImpl;
import org.example.QuerySide.QueryHandler;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import static org.example.QuerySide.QueryModel.query_database;

public class Main {

    public static void main(String[] args) {
        //todo: item eventuell unglück beannt weil es ja nicht items speichert
        // müssen den Namen ja immer selber merken und als String angeben
        // gibt es ne Lösung wo es auch hier item sind?
        CommandImpl item = new CommandImpl();
        item.createItem("Tom", new int[]{1, 2, 3}, 0);
        item.moveItem("Tom", new int[]{2, 3, 4});
        item.createItem("Lisa", new int[]{6, 7, 8}, 0);
        item.createItem("Otto", new int[]{0, 0, 3}, 0);

        for (Map.Entry<String, MovingItemDTO> entry : query_database.entrySet()) {
            System.out.println("for loop: " + entry.getKey() + " : " + entry.getValue().getName() + ", " + entry.getValue().getValue() + ", " + Arrays.toString(entry.getValue().getLocation()) + ", " + entry.getValue().getNumberOfMoves());
        }

        QueryHandler query = new QueryHandler();
        System.out.println("Item at position:");
        Enumeration<MovingItemDTO> itemsAtPosition = query.getMovingItemsAtPosition(new int[]{0, 0, 3});
        while (itemsAtPosition.hasMoreElements()) {
            MovingItemDTO itemAtPosition = itemsAtPosition.nextElement();
            System.out.println(itemAtPosition.getName() + ", " + itemAtPosition.getValue() + ", " + Arrays.toString(itemAtPosition.getLocation()) + ", " + itemAtPosition.getNumberOfMoves());
        }
        System.out.println(query.getMovingItemByName("Tom").getName());

        System.out.println("All items:");
        Enumeration<MovingItemDTO> allItems = query.getMovingItems();
        while (allItems.hasMoreElements()) {
            MovingItemDTO itemsIteration = allItems.nextElement();
            System.out.println(itemsIteration.getName() + ", " + itemsIteration.getValue() + ", " + Arrays.toString(itemsIteration.getLocation()) + ", " + itemsIteration.getNumberOfMoves());
        }
    }
}