package org.example;

import java.util.Arrays;
import java.util.Map;

import static org.example.QueryModel.query_database;

public class Main {

    public static void main(String[] args) {
       /* CommandHandler test = new CommandHandler();
        test.createItem("Test", new int[]{1,4,2}, 0);
        test.moveItem("Test", new int[]{2,3,3});
        System.out.println(Arrays.toString(test.items.get("Test").getLocation()));*/
        System.out.println("hi");
        CommandImpl item = new CommandImpl();
        item.createItem("Tom", new int []{1,2,3}, 0);
        item.moveItem("Tom", new int[]{2,3,4});
        item.createItem("Lisa", new int []{6,7,8}, 0);
        item.createItem("Otto", new int []{0,0,3}, 0);

        /*for(Map.Entry<String, MovingItemImpl> entry: query_database.entrySet()){
            System.out.println(entry.getKey() +" : "+ entry.getValue().getName() + ", "+ entry.getValue().getValue() +", "+ Arrays.toString(entry.getValue().getLocation()) + ", " + entry.getValue().getNumberOfMoves());
        }*/

        QueryHandler query = new QueryHandler();
        System.out.println(query.getMovingItemsAtPosition(new int[]{0,0,3}));
        // System.out.println(query.getMovingItemsAtPosition(new int[]{0,0,2}));

        System.out.println(query.getMovingItemByName("Tom"));
        System.out.println(query.getMovingItems());

    }
}