package org.example;

public interface MovingItemDTO {
    //todo: schauen ob wert nie abgefragt wird und raus kann
    String getName();
    int[] getLocation();
    int getNumberOfMoves();
    int getValue();
}
