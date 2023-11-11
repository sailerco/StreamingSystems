package org.example.CommandSide;

import java.util.ArrayList;
import java.util.List;

public class DomainModel {
    public static List<String> domainIDs = new ArrayList<>();

    public Boolean exists(String id){
        return domainIDs.contains(id);
    }
    public void add(String id){
        domainIDs.add(id);
    }
    public void remove(String id){
        domainIDs.remove(id);
    }
}
