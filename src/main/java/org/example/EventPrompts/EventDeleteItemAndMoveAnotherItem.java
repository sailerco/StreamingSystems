package org.example.EventPrompts;

import java.io.Serializable;

public class EventDeleteItemAndMoveAnotherItem extends EventVector implements Serializable {
    public String new_id;

    public EventDeleteItemAndMoveAnotherItem(String id, String new_id, int[] vector) {
        this.id = id;
        this.new_id = new_id;
        this.vector = vector;
    }
}
