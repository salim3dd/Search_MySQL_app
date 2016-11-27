package com.salim3dd.search_mysql;

/**
 * Created by Salim3dd on 27/11/2016.
 */

public class ListItem {
    public int id;
    public String Name;

    public ListItem(int id, String name) {
        this.id = id;
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
