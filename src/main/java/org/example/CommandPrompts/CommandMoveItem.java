package org.example.CommandPrompts;

public class CommandMoveItem {
    public String id;
    public int[] vector;
    //todo: kann eventuell weg, einfach checken wie oft id in eventstore
    public int move = 1;
    public CommandMoveItem(String id, int[] vector){
        this.id = id;
        this.vector = vector;
    }
}
