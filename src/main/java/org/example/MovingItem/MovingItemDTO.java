package org.example.MovingItem;

public interface MovingItemDTO {
    void setMoves();

    //todo: schauen ob wert nie abgefragt wird und raus kann
    String getName();

    int[] getLocation();

    void setLocation(int[] location);

    int getNumberOfMoves();

    int getValue();

    void setValue(int value);


}
