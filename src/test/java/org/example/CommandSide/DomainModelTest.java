package org.example.CommandSide;

import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandDeleteItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.example.Consumer;
import org.example.EventPrompts.Event;
import org.example.EventPrompts.EventMovingItemCreated;
import org.example.EventPrompts.EventMovingItemMoved;
import org.example.Producer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DomainModelTest {
    static List<Event> events = new ArrayList<>(Arrays.asList(new EventMovingItemCreated("Alice", new int[]{0, 0, 0}, 0),
            new EventMovingItemMoved("Alice", new int[]{3, 5, 7})
    ));
    static Producer p = mock(Producer.class);

    static Consumer c = mock(Consumer.class);
    DomainModel model = new DomainModel();

    @BeforeAll
    static void setup() {
        when(c.getEvent(anyInt())).thenReturn(events);

        doAnswer(invocation -> {
            Event event = invocation.getArgument(1);
            events.add(event);
            return null;
        }).when(p).sendObjectMessage(any(), any());

        DomainModel.producer = p;
        DomainModel.consumer = c;
    }

    @AfterEach
    void cleanUp() {
        events.clear();
        events.addAll(new ArrayList<>(Arrays.asList(
                new EventMovingItemCreated("Alice", new int[]{0, 0, 0}, 0),
                new EventMovingItemMoved("Alice", new int[]{3, 5, 7})
        )));
    }

    @Test
    public void createTest() {
        model.createItem(new CommandCreateItem("Bob", new int[]{0, 0, 0}, 0));
        assertTrue(model.exists("Bob"));
    }

    @Test
    public void moveItemTest() {
        DomainModel.MOVES_LIMIT = 2;
        model.createItem(new CommandCreateItem("Bob", new int[]{2, 3, 4}, 0));
        model.createItem(new CommandCreateItem("Otto", new int[]{2, 3, 4}, 0));
        assertTrue(model.exists("Otto"));
        assertFalse(model.exists("Bob"));

        model.moveItem(new CommandMoveItem("Otto", new int[]{1, 2, 3}));
        assertTrue(model.exists("Otto"));
        assertFalse(model.exists("Alice"));

        model.moveItem(new CommandMoveItem("Otto", new int[]{3, 5, 7}));
        assertFalse(model.exists("Otto"));
    }

    @Test
    public void deleteItem() {
        model.removeItem(new CommandDeleteItem("Alice"));
        assertFalse(model.exists("Alice"));
    }
}