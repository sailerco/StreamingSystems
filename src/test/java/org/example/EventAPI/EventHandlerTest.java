package org.example.EventAPI;

import org.example.EventPrompts.*;
import org.example.MovingItem.MovingItemDTOImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.example.Main.env;
import static org.example.QuerySide.QueryModel.query_database;
import static org.junit.jupiter.api.Assertions.*;

class EventHandlerTest {

    EventHandler handler = new EventHandler();

    EventHandlerTest() {
    }

    @BeforeAll
    static void start() {
        env.start();
    }

    @AfterAll
    static void finish() {
        query_database.clear();
        env.close();
    }

    @BeforeEach
    void setup() {
        query_database.clear();
        query_database.put("Tom", new MovingItemDTOImpl("Tom", new int[]{1, 2, 3}, 0, 0));
    }

    @Test
    void consumeEventItemCreated() {
        handler.consumeEvent(new EventMovingItemCreated("Bob", new int[]{0, 0, 0}, 0));
        assertTrue(query_database.containsKey("Bob"));
        assertFalse(query_database.containsKey("Alice"));
    }

    @Test
    void consumeEventMoveItem() {
        handler.consumeEvent(new EventMovingItemMoved("Tom", new int[]{1, 2, 3}));
        assertArrayEquals(new int[]{2, 4, 6}, query_database.get("Tom").getLocation());
        assertEquals(1, query_database.get("Tom").getNumberOfMoves());
    }

    @Test
    void consumeEventDeleteItemAndMoveAnotherItem() {
        handler.consumeEvent(new EventMovingItemCreated("Bob", new int[]{0, 0, 0}, 0));
        handler.consumeEvent(new EventDeleteItemAndMoveAnotherItem("Tom", "Bob", new int[]{1, 2, 3}));
        assertFalse(query_database.containsKey("Tom"));
        assertArrayEquals(new int[]{1, 2, 3}, query_database.get("Bob").getLocation());
    }

    @Test
    void consumeEventChangeValue() {
        handler.consumeEvent(new EventMovingItemChangedValue("Tom", 5));
        assertEquals(5, query_database.get("Tom").getValue());
    }

    @Test
    void consumeEventDeleted() {
        handler.consumeEvent(new EventMovingItemDeleted("Tom"));
        assertFalse(query_database.containsKey("Tom"));
    }
}