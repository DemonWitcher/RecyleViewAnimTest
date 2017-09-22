package com.witcher.testrecyleview1;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by witcher on 2017/9/20.
 */

public class Child implements MultiItemEntity {
    String name;

    public Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
