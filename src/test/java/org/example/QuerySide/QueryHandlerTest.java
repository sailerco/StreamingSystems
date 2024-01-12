package org.example.QuerySide;

import org.example.MovingItem.MovingItemDTO;
import org.example.MovingItem.MovingItemDTOImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryHandlerTest {

    QueryHandler handler = new QueryHandler();

    @BeforeAll
    public static void setup() {
        MovingItemDTO itemA = new MovingItemDTOImpl("Tom", new int[]{1, 2, 3}, 0, 0);
        MovingItemDTO itemB = new MovingItemDTOImpl("Alice", new int[]{0, 4, 1}, 2, 1);
        QueryModel.query_database.put("Tom", itemA);
        QueryModel.query_database.put("Alice", itemB);
    }

    @Test
    void getMovingItemByName() {
        MovingItemDTO item = handler.getMovingItemByName("Tom");
        assertEquals("Tom", item.getName());
        assertArrayEquals(new int[]{1, 2, 3}, item.getLocation());
        assertEquals(0, item.getValue());
        assertEquals(0, item.getNumberOfMoves());
        assertNull(handler.getMovingItemByName("Bob"));
    }

    @Test
    void getMovingItems() {
        Enumeration<MovingItemDTO> result = handler.getMovingItems();
        List<MovingItemDTO> resultList = Collections.list(result);

        assertEquals(2, resultList.size());
        assertEquals("Tom", resultList.get(0).getName());
        assertEquals("Alice", resultList.get(1).getName());

        assertNotEquals("Tom", resultList.get(1).getName());
        assertNotEquals("Bob", resultList.get(1).getName());
    }

    @Test
    void getMovingItemsAtPosition() {
        Enumeration<MovingItemDTO> result = handler.getMovingItemsAtPosition(new int[]{1, 2, 3});
        List<MovingItemDTO> resultList = Collections.list(result);

        assertEquals(1, resultList.size());
        assertEquals("Tom", resultList.get(0).getName());
        assertNotEquals("Alice", resultList.get(0).getName());
    }
}