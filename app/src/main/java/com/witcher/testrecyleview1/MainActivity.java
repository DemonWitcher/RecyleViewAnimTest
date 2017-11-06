package com.witcher.testrecyleview1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView listview;
    ArrayList<Group> dataList = new ArrayList<>();
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.listview);
        initDataList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listview.setLayoutManager(layoutManager);

        adapter = new MyAdapter(this,dataList);

        listview.setAdapter(adapter);

        listview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {

            }
        });
        listview.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
//                if(view instanceof )
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        });

    }

    private void initDataList() {
        for (int i = 0; i < 26; ++i) {
            Group group = new Group();
            group.setName("group" + i);
            ArrayList<Child> children = new ArrayList<>();
            for (int j = 0; j < 6; ++j) {
                children.add(new Child(i + "child" + j));
            }
            group.setChildren(children);
            dataList.add(group);
        }

    }

    public void action1(View view){
        L.i("childCount:"+listview.getChildCount());
    }

}
