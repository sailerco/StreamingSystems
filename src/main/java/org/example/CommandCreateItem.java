package org.example;

public class CommandCreateItem {
    public MovingItem item;
    public CommandCreateItem(String id){
        item = new MovingItemImpl(id, 0);
    }
    public CommandCreateItem(String id, int[] position, int value){
        item = new MovingItemImpl(id, position, value);
    }
    public MovingItem getItem() {
        return item;
    }
}
