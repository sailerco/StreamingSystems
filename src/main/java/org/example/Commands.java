package org.example;

public interface Commands {
    void createItem(String id);
    void createItem(String id, int[] position, int value);
    void deleteItem(String id);
    void moveItem(String id, int[] vector);
    void changeValue(String id, int newValue);
}
