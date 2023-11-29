package org.example.CommandSide;

public interface Commands {
    void createItem(String id) throws Exception;
    void createItem(String id, int[] position, int value) throws Exception;
    void deleteItem(String id) throws Exception;
    void moveItem(String id, int[] vector) throws Exception;
    void changeValue(String id, int newValue) throws Exception;
}
