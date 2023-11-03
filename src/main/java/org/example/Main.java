package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

       /* CommandHandler test = new CommandHandler();
        test.createItem("Test", new int[]{1,4,2}, 0);
        test.moveItem("Test", new int[]{2,3,3});
        System.out.println(Arrays.toString(test.items.get("Test").getLocation()));*/

        CommandImpl item = new CommandImpl();
        item.createItem("Tom", new int []{1,2,3}, 0);
        item.moveItem("Tom", new int[]{2,3,4});

    }
}