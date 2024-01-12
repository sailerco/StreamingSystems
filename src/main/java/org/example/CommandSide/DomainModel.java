package org.example.CommandSide;

import jakarta.jms.JMSException;
import org.example.CommandPrompts.CommandChangeValue;
import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.example.CommandPrompts.CommandPrompt;
import org.example.ConnectionMQ;
import org.example.EventPrompts.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DomainModel {

    public static final int MOVES_LIMIT = 20;
    public static Map<String, Integer> idsAndMoves = new HashMap<>();
    public static Map<String, int[]> usedPositions = new HashMap<>();
    static ConnectionMQ producer;

    public DomainModel() throws Exception {
        producer = new ConnectionMQ("publisher");
    }

    static public void stop() throws JMSException {
        producer.close();
    }

    //The item will be added to the Maps and the Creation Event will be called.
    public void create(CommandCreateItem command) throws JMSException {
        String key = getKeyByPosition(command.location);
        if (!exists(command.id) && (Arrays.equals(command.location, new int[]{0, 0, 0}) || !usedPositions.containsKey(key))) {
            idsAndMoves.put(command.id, 0);
            usedPositions.put(command.id, command.location);
            producer.sendMessage(new EventMovingItemCreated(command.id, command.location, command.value));
        } else if (!exists(command.id) && usedPositions.containsKey(key)) {
            producer.sendMessage(new EventMovingItemCreatedOnUsedPosition(key, command.id, command.location, command.value));
            removeFromHashes(key);
            usedPositions.put(command.id, command.location);
            idsAndMoves.put(command.id, 0);
        } else System.out.println("Item with id " + command.id + " already exists");
    }

    //If the item exists the value will be changed
    public void changeValue(CommandChangeValue command) throws JMSException {
        if (exists(command.id)) {
            producer.sendMessage(new EventMovingItemChangedValue(command.id, command.newValue));
        } else System.out.println("Item with id " + command.id + "doesn't exist and therefore cannot be changed");
    }

    //If the item exists HashMaps will be updated and the Deletion Event will be called.
    public void remove(CommandPrompt command) throws JMSException {
        if (exists(command.id)) {
            producer.sendMessage(new EventMovingItemDeleted(command.id));
            removeFromHashes(command.id);
        } else System.out.println("Item with id " + command.id + "doesn't exist and therefore cannot be deleted");
    }

    //*Verifies that the item exists, that it's getting moved by a non-zero vector,
    // and it's not moved more than 20 times. It then moves the item and handles possible collisions.*//
    public void moveItem(CommandMoveItem command) throws JMSException {
        if (exists(command.id) && !Arrays.equals(command.vector, new int[]{0, 0, 0})) {
            if (!movedOverLimit(command.id)) {
                int[] newPosition = computePosition(usedPositions.get(command.id), command.vector);
                String key = getKeyByPosition(newPosition);
                if (usedPositions.containsKey(key)) handleCollisionAndMove(key, command.id, command.vector);
                else {
                    producer.sendMessage(new EventMovingItemMoved(command.id, command.vector));
                    usedPositions.put(command.id, newPosition);
                }
            } else remove(command);
        } else System.out.println("Item with id " + command.id + "doesn't exist and therefore cannot be moved");
    }

    //the following methods are helper functions
    private void handleCollisionAndMove(String collidedItem, String movedItem, int[] position) throws JMSException {
        producer.sendMessage(new EventDeleteItemAndMoveAnotherItem(collidedItem, movedItem, position));
        usedPositions.put(movedItem, position);
        removeFromHashes(collidedItem);
    }

    //If the Item was moved >= 20 times it will be removed. Otherwise, the IDsAndMoves field is updated.
    private boolean movedOverLimit(String id) {
        int moves = idsAndMoves.get(id) + 1;
        if (moves >= MOVES_LIMIT) return true;
        else {
            idsAndMoves.put(id, moves);
            return false;
        }
    }

    private int[] computePosition(int[] first, int[] second) {
        int[] sum = new int[3];
        for (int i = 0; i < first.length; i++)
            sum[i] = first[i] + second[i];
        return sum;
    }

    private String getKeyByPosition(int[] position) {
        for (Map.Entry<String, int[]> entry : usedPositions.entrySet()) {
            if (Arrays.equals(entry.getValue(), position)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void removeFromHashes(String item) {
        idsAndMoves.remove(item);
        usedPositions.remove(item);
    }

    public Boolean exists(String id) {
        return idsAndMoves.containsKey(id);
    }
}
