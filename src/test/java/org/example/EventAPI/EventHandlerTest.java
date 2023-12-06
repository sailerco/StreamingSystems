package org.example.EventAPI;

import org.example.EventPrompts.EventMovingItemChangedValue;
import org.example.EventPrompts.EventMovingItemCreated;
import org.example.EventPrompts.EventMovingItemDeleted;
import org.example.EventPrompts.EventMovingItemMoved;
import org.example.MovingItem.MovingItemDTOImpl;
import org.example.QuerySide.QueryModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.jms.JMSException;

import static org.junit.jupiter.api.Assertions.*;

class EventHandlerTest {

    EventHandler handler = new EventHandler();

    EventHandlerTest() throws JMSException {
    }

    @BeforeEach
    void setup() {
        QueryModel.query_database.clear();
        QueryModel.query_database.put("Tom", new MovingItemDTOImpl("Tom", new int[]{1, 2, 3}, 0, 0));
    }

    @Test
    void consumeEventItemCreated() {
        handler.consumeEvent(new EventMovingItemCreated("Bob", new int[]{1, 2, 3}, 0));
        assertTrue(QueryModel.query_database.containsKey("Bob"));
        assertFalse(QueryModel.query_database.containsKey("Alice"));
    }

    @Test
    void consumeEventMoveItem() {
        handler.consumeEvent(new EventMovingItemMoved("Tom", new int[]{1, 2, 3}));
        assertArrayEquals(new int[]{2, 4, 6}, QueryModel.query_database.get("Tom").getLocation());
        assertEquals(1, QueryModel.query_database.get("Tom").getNumberOfMoves());
    }

    @Test
    void consumeEventChangeValue() {
        handler.consumeEvent(new EventMovingItemChangedValue("Tom", 5));
        assertEquals(5, QueryModel.query_database.get("Tom").getValue());
    }

    @Test
    void consumeEventDeleted() {
        handler.consumeEvent(new EventMovingItemDeleted("Tom"));
        assertFalse(QueryModel.query_database.containsKey("Tom"));
    }
}