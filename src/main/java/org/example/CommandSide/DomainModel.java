package org.example.CommandSide;

import org.example.CommandPrompts.CommandChangeValue;
import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.example.CommandPrompts.CommandPrompt;
import org.example.Consumer;
import org.example.EventPrompts.*;
import org.example.Producer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainModel {
    public static int MOVES_LIMIT = 20;
    public static Producer producer = new Producer();
    public static Consumer consumer = new Consumer();
    List<Event> events;

    //The item will be added to the Maps and the Creation Event will be called.
    public void createItem(CommandCreateItem command) {
        events = retrieveEvents();
        String key = checkIfPositionIsUsed(command.location);
        if (!exists(command.id) && (Arrays.equals(command.location, new int[]{0, 0, 0}) || key == null)) {
            producer.sendObjectMessage(command.id, new EventMovingItemCreated(command.id, command.location, command.value));
        } else if (!exists(command.id) && key != null) {
            producer.sendObjectMessage(command.id, new EventMovingItemCreatedOnUsedPosition(key, command.id, command.location, command.value));
        } else System.out.println("Item with id " + command.id + " already exists");
    }

    //If the item exists the value will be changed
    public void changeValue(CommandChangeValue command) {
        events = retrieveEvents();
        if (exists(command.id)) {
            producer.sendObjectMessage(command.id, new EventMovingItemChangedValue(command.id, command.newValue));
        } else System.out.println("Item with id " + command.id + " doesn't exist and therefore cannot be changed");
    }

    //If the item exists HashMaps will be updated and the Deletion Event will be called.
    public void removeItem(CommandPrompt command) {
        events = retrieveEvents();
        if (exists(command.id)) {
            producer.sendObjectMessage(command.id, new EventMovingItemDeleted(command.id));
        } else System.out.println("Item with id " + command.id + " doesn't exist and therefore cannot be deleted");
    }


    //*Verifies that the item exists, that it's getting moved by a non-zero vector,
    // and it's not moved more than 20 times.
    // It then moves the item and handles collisions.*//
    public void moveItem(CommandMoveItem command) {
        events = retrieveEvents();
        if (exists(command.id) && !Arrays.equals(command.vector, new int[]{0, 0, 0})) {
            if (!movedOverLimit(command.id)) {
                int[] newPosition = computePosition(getPosition(command.id), command.vector);
                String key = checkIfPositionIsUsed(newPosition);
                if (key != null)
                    handleCollisionAndMove(key, command.id, command.vector);
                else
                    producer.sendObjectMessage(command.id, new EventMovingItemMoved(command.id, command.vector));
            } else {
                System.out.println(command.id + " moved too many times and will be deleted.");
                producer.sendObjectMessage(command.id, new EventMovingItemDeleted(command.id));
            }
        } else System.out.println("Item with id " + command.id + " doesn't exist and therefore cannot be moved");
    }

    //the following methods are helper functions
    private void handleCollisionAndMove(String collidedItem, String movedItem, int[] position) {
        producer.sendObjectMessage(movedItem, new EventDeleteItemAndMoveAnotherItem(collidedItem, movedItem, position));
    }

    //Sum of two vector
    private int[] computePosition(int[] first, int[] second) {
        int[] sum = new int[3];
        for (int i = 0; i < first.length; i++)
            sum[i] = first[i] + second[i];
        return sum;
    }

    //checks the existence of an ID
    public Boolean exists(String id) {
        return retrieveIDs().contains(id);
    }

    //Retrieves all IDs that currently exist, through Events
    private List<String> retrieveIDs() {
        List<String> ids = new ArrayList<>();
        for (Event event : events) {
            if (event instanceof EventMovingItemCreated) {
                ids.add(event.id);
            } else if (event instanceof EventMovingItemDeleted) {
                ids.remove(event.id);
            } else if (event instanceof EventMovingItemCreatedOnUsedPosition) {
                ids.remove(event.id);
                ids.add(((EventMovingItemCreatedOnUsedPosition) event).new_id);
            } else if (event instanceof EventDeleteItemAndMoveAnotherItem) {
                ids.remove(event.id);
            }
        }
        return ids;
    }

    //retrieves events through consumer
    private List<Event> retrieveEvents() {
        return consumer.getEvent(1000);
    }

    //get the Position of a specific id, through event iteration
    private int[] getPosition(String id) {
        int[] result = new int[3];
        for (Event event : events) {
            if (event.id.equals(id)) {
                boolean itemWasDeleted = itemWasDeleted(event, id);
                if (event instanceof EventMovingItemCreated) {
                    result = ((EventMovingItemCreated) event).item.getLocation();
                } else if (event instanceof EventMovingItemMoved) {
                    result = computePosition(result, ((EventMovingItemMoved) event).vector);
                } else if (itemWasDeleted) {
                    result = null;
                    break;
                }
            } else if (event instanceof EventDeleteItemAndMoveAnotherItem
                    && ((EventDeleteItemAndMoveAnotherItem) event).new_id.equals(id)) {
                result = computePosition(result, ((EventDeleteItemAndMoveAnotherItem) event).vector);
            } else if (event instanceof EventMovingItemCreatedOnUsedPosition
                    && ((EventMovingItemCreatedOnUsedPosition) event).new_id.equals(id)) {
                result = ((EventMovingItemCreatedOnUsedPosition) event).item.getLocation();
            }
        }
        return result;
    }


    //checks if the Item with the id was deleted through an Event
    private boolean itemWasDeleted(Event event, String id) {
        if (event instanceof EventMovingItemDeleted) {
            return true;
        } else if (event instanceof EventMovingItemCreatedOnUsedPosition
                || event instanceof EventDeleteItemAndMoveAnotherItem) {
            return id.equals(event.id);
        }
        return false;
    }

    //if the item was moved too many times it will be removed.
    private boolean movedOverLimit(String id) {
        int moves = 0;
        for (Event event : events) {
            if (event.id.equals(id)) {
                boolean itemWasDeleted = itemWasDeleted(event, id);
                if (event instanceof EventMovingItemCreated || itemWasDeleted) {
                    moves = 0;
                } else moves += 1;
            } else if (event instanceof EventDeleteItemAndMoveAnotherItem
                    && ((EventDeleteItemAndMoveAnotherItem) event).new_id.equals(id)) {
                moves += 1;
            } else if (event instanceof EventMovingItemCreatedOnUsedPosition
                    && ((EventMovingItemCreatedOnUsedPosition) event).new_id.equals(id)) {
                moves += 1;
            }
        }
        return moves >= MOVES_LIMIT;
    }

    //check if an item occupies the target position
    private String checkIfPositionIsUsed(int[] targetPosition) {
        List<String> ids = retrieveIDs();
        List<int[]> positions = new ArrayList<>();
        for (String name : ids) {
            positions.add(getPosition(name));
        }
        for (int i = 0; i <= positions.size() - 1; i++) {
            if (Arrays.equals(positions.get(i), targetPosition)) {
                return ids.get(i);
            }
        }
        return null;
    }
}
