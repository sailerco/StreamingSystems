package org.example.MovingItem;

public interface MovingItemDTO {
    void setMoves();

    String getName();

    int[] getLocation();

    void setLocation(int[] location);

    int getNumberOfMoves();

    int getValue();

    void setValue(int value);


}
