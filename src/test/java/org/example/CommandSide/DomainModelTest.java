package org.example.CommandSide;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelTest {
    DomainModel model = new DomainModel();
    @BeforeAll
    public static void setup(){
        DomainModel.domainIDs.add("Tom");
    }
    @Test
    public void existsTest(){
        assertTrue(model.exists("Tom"));
    }
}