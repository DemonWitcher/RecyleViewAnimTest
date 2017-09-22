package com.witcher.testrecyleview1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView listview;
    ArrayList<MultiItemEntity> dataList = new ArrayList<>();
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (RecyclerView) findViewById(R.id.listview);
        initDataList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listview.setLayoutManager(layoutManager);

        adapter = new MyAdapter(dataList, new MyAdapter.OnItemExpandListener() {
            @Override
            public void onItemExpand(int index) {
                i("index:" + index);
                int scrollX = 0;
                for (int i = 0; i < index; ++i) {
                    Group group = (Group) dataList.get(i);
                    if (group.isExpanded()) {
                        scrollX = scrollX + group.getSubItems().size() * 120;
                        scrollX = scrollX + 120;
                    } else {
                        scrollX = scrollX + 120;
                    }
                }
                i("scrollX:" + scrollX);
                i("listview.getScrollX():"+listview.getScrollX());
                scrollX = scrollX - listview.getScrollX();
                i("scrollX:" + scrollX);
                listview.smoothScrollBy(scrollX - listview.getScrollX(), 0);
            }
        });

        listview.setAdapter(adapter);

    }

    private void initDataList() {
        for (int i = 0; i < 26; ++i) {
            Group group = new Group();
            group.setName("group" + i);
            for (int j = 0; j < 6; ++j) {
                group.addSubItem(new Child(i + "child" + j));
            }
            dataList.add(group);
        }

    }

    public static void i(String content) {
        Log.i("witcher", content);
    }
}
