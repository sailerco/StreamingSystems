package org.example.EventPrompts;

public class EventMovingItemMoved extends Event{
    public String id;
    public int[] vector;
    //todo: move?
    int move = 1;
    public EventMovingItemMoved(String id, int[] vector){
        this.id = id;
        this.vector = vector;
    }
}
