package com.witcher.testrecyleview1;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

/**
 * Created by witcher on 2017/9/20.
 */

public class Group extends AbstractExpandableItem<Child> implements MultiItemEntity {
    String name;
    ArrayList<Child> children;
    boolean isZhankai;

    public Group(String name, ArrayList<Child> children) {
        this.name = name;
        this.children = children;
    }

    public Group() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
