package org.example;

import org.example.CommandSide.CommandImpl;
import org.example.MovingItem.MovingItemDTO;
import org.example.QuerySide.QueryHandler;

import java.util.Arrays;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) {
        CommandImpl item = new CommandImpl();
        item.createItem("Tom", new int[]{1, 2, 3}, 0);
        item.moveItem("Tom", new int[]{2, 3, 4});
        item.changeValue("Tom", 7);
        item.createItem("Lisa", new int[]{6, 7, 8}, 0);
        item.deleteItem("Lisa");
        item.createItem("Otto", new int[]{0, 0, 3}, 6);

        QueryHandler query = new QueryHandler();
        System.out.println("Item at position:");
        printMovingItems(query.getMovingItemsAtPosition(new int[]{0, 0, 3}));
        MovingItemDTO itemByName = query.getMovingItemByName("Tom");
        if(itemByName != null)
            System.out.println("Get item by name: " + itemByName.getName() + ", " + itemByName.getValue() + ", " + Arrays.toString(itemByName.getLocation()) + ", " + itemByName.getNumberOfMoves());

        System.out.println("All items:");
        printMovingItems(query.getMovingItems());
    }

    private static void printMovingItems(Enumeration<MovingItemDTO> items) {
        while (items.hasMoreElements()) {
            MovingItemDTO item = items.nextElement();
            System.out.println(item.getName() + ", " + item.getValue() + ", " + Arrays.toString(item.getLocation()) + ", " + item.getNumberOfMoves());
        }
    }
}