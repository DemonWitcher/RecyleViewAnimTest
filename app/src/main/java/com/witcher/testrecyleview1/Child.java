package com.witcher.testrecyleview1;

/**
 * Created by witcher on 2017/9/20.
 */

public class Child {
    String name;
    boolean isFirstBind = true;
    Group group;

    public Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
