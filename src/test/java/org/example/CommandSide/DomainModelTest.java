package org.example.CommandSide;

import jakarta.jms.JMSException;
import org.example.CommandPrompts.CommandCreateItem;
import org.example.CommandPrompts.CommandDeleteItem;
import org.example.CommandPrompts.CommandMoveItem;
import org.example.Consumer;
import org.example.EventPrompts.*;
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

//TODO: change the tests that it works without the idsAndMoves, usedPositions and Broker
class DomainModelTest {
    static List<Event> events = new ArrayList<>(Arrays.asList(new EventMovingItemCreated("Alice", new int[]{0, 0, 0}, 0),
            new EventMovingItemMoved("Alice", new int[]{3, 5, 7}),
            new EventMovingItemCreated("Tom", new int[]{1, 2, 3}, 0),
            new EventMovingItemChangedValue("Tom", 7),
            new EventDeleteItemAndMoveAnotherItem("Alice", "Tom", new int[]{2, 3, 4}),
            new EventMovingItemCreatedOnUsedPosition("Tom", "Lisa", new int[]{3, 5, 7}, 0)
    ));
    static Producer p = mock(Producer.class);
    ;
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
        events.addAll(new ArrayList<>(Arrays.asList(new EventMovingItemCreated("Alice", new int[]{0, 0, 0}, 0),
                new EventMovingItemMoved("Alice", new int[]{3, 5, 7}),
                new EventMovingItemCreated("Tom", new int[]{1, 2, 3}, 0),
                new EventMovingItemChangedValue("Tom", 7),
                new EventDeleteItemAndMoveAnotherItem("Alice", "Tom", new int[]{2, 3, 4}),
                new EventMovingItemCreatedOnUsedPosition("Tom", "Lisa", new int[]{3, 5, 7}, 0)
        )));
    }

    @Test
    public void createTest() {
        model.createItem(new CommandCreateItem("Bob", new int[]{0, 0, 0}, 0));
        assertTrue(model.exists("Bob"));
    }

    @Test
    public void moveItemTest() {

        model.createItem(new CommandCreateItem("Otto", new int[]{0, 0, 0}, 0));
        assertTrue(model.exists("Otto"));

        //TODO: Finish tests

/*        model.moveItem(new CommandMoveItem("Tom", new int[]{1, 1, 4}));
        assertFalse(model.exists("Tom"));

        model.moveItem(new CommandMoveItem("Bob", new int[]{3, 4, 1}));
        assertTrue(model.exists("Bob"));
        assertEquals(idsAndMoves.get("Bob"), 1);

        model.createItem(new CommandCreateItem("Alice", new int[]{0, 0, 0}, 0));
        model.moveItem(new CommandMoveItem("Alice", new int[]{3, 4, 1}));
        assertFalse(model.exists("Bob"));

        model.createItem(new CommandCreateItem("Otto", new int[]{3, 4, 1}, 0));
        assertFalse(model.exists("Alice"));
        assertTrue(model.exists("Otto"));

        model.createItem(new CommandCreateItem("Dora"));
        model.createItem(new CommandCreateItem("Florian"));
        assertTrue(model.exists("Dora"));
        assertTrue(model.exists("Florian"));*/
    }

    @Test
    public void deleteItem() throws JMSException {
        model.removeItem(new CommandDeleteItem("Tom"));
        assertFalse(model.exists("Tom"));
    }
}