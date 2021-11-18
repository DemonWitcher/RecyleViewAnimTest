package com.witcher.testrecyleview1;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


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
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(0);
        defaultItemAnimator.setChangeDuration(0);
        defaultItemAnimator.setMoveDuration(0);
        defaultItemAnimator.setRemoveDuration(0);
        listview.setItemAnimator(defaultItemAnimator);
        adapter = new MyAdapter(this, dataList, listview);

        listview.setAdapter(adapter);

        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                L.i("**********************");
//                for(View view:adapter.groupList){
//                    L.i("tx:"+view.getTranslationX()+",sx:"+view.getScrollX()+",x:"+view.getX());
//                }
//                L.i("**********************");
            }
        });

        initView();
        initChildView();
    }

    private void initDataList() {
        for (int i = 0; i < 26; ++i) {
            Group group = new Group();
            group.setName("" + i);
            ArrayList<Child> children = new ArrayList<>();
            for (int j = 0; j < 6; ++j) {
                Child child = new Child("" + j);
                child.group = group;
                children.add(child);
            }
            group.setChildren(children);
            dataList.add(group);
        }

    }

    public void action1(View view) {
        L.i("childCount:" + listview.getChildCount());
    }

    public void action2(View view) {
        listview.scrollBy(480, 0);
    }

    public void action3(View view) {
        MyAdapter.IS_ONLY_ONE_OPEN = !MyAdapter.IS_ONLY_ONE_OPEN;
    }


//------------------------------------------------------------------------------------------------------------------------


    LinearLayout ll1;
    View view1, view2, view3, view4, view5, view6;
    View child1, child2, child3, child4, child5;

    ArrayList<View> childList = new ArrayList<>();
    ArrayList<View> groupList = new ArrayList<>();
    ArrayList<Boolean> booleanList = new ArrayList<>();

    private void initChildView() {
        child1 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child_test, null);
        child2 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child_test, null);
        child3 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child_test, null);
        child4 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child_test, null);
        child5 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child_test, null);
        ((TextView) child1.findViewById(R.id.tv_name)).setText("1");
        ((TextView) child2.findViewById(R.id.tv_name)).setText("2");
        ((TextView) child3.findViewById(R.id.tv_name)).setText("3");
        ((TextView) child4.findViewById(R.id.tv_name)).setText("4");
        ((TextView) child5.findViewById(R.id.tv_name)).setText("5");
        childList.add(child1);
        childList.add(child2);
        childList.add(child3);
        childList.add(child4);
        childList.add(child5);
    }

    private void initView() {
        view1 = findViewById(R.id.rl1);
        view2 = findViewById(R.id.rl2);
        view3 = findViewById(R.id.rl3);
        view4 = findViewById(R.id.rl4);
        view5 = findViewById(R.id.rl5);
        view6 = findViewById(R.id.rl6);
        groupList.add(view1);
        groupList.add(view2);
        groupList.add(view3);
        groupList.add(view4);
        groupList.add(view5);
        groupList.add(view6);
        booleanList.add(false);
        booleanList.add(false);
        booleanList.add(false);
        booleanList.add(false);
        booleanList.add(false);
        booleanList.add(false);
        ll1 = findViewById(R.id.ll1);
        for (View view : groupList) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int index = ll1.indexOfChild(v);
        boolean isZhankai = booleanList.get(index);
        if (isZhankai) {
            close(index);
        } else {
            open(index);
        }
        booleanList.set(index, !isZhankai);
    }

    //移植到RV,提取宽度
    private void open(final int clickIndex) {
        final int childSize = childList.size();
        final int groupSize = groupList.size();
        for (int i = childSize - 1; i > -1; --i) {
            View child = childList.get(i);
            child.setPivotX(60);
            ll1.addView(child, clickIndex + 1);
        }

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(6000);
        valueAnimator.setFloatValues(0, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                float leftTranslation = -value * 120 * clickIndex;
                float rightTranslation = -(childSize * 120 - (value * 120 * (groupSize - 1 - clickIndex)));
                for (int i = 0; i < groupSize; ++i) {
                    View group = groupList.get(i);
                    if (i <= clickIndex) {
                        group.setTranslationX(leftTranslation);
                    } else {
                        group.setTranslationX(rightTranslation);
                    }
                }
                float scaleLeftMargin = 60 - (value * 60);
                for (int i = 0; i < childSize; ++i) {
                    View child = childList.get(i);
                    child.setScaleX(value);
                    child.setTranslationX((-i * 120) - (value * ((childSize - i - (groupSize - 1 - clickIndex)) * 120)) - scaleLeftMargin);
                }
            }
        });
        valueAnimator.start();
    }

    private void close(final int clickIndex) {
        //子列表以左侧为中心缩放到0 同时位移 右侧的item们跟着位移
        final int childSize = childList.size();
        final int groupSize = groupList.size();

        final float[] groupTranslationXArr = new float[groupSize];
        final float[] childTranslationXArr = new float[childSize];
        for (int i = 0; i < childSize; ++i) {
            View child = childList.get(i);
            child.setPivotX(0);
            childTranslationXArr[i] = child.getTranslationX();
        }
        for (int i = 0; i < groupSize; ++i) {
            View group = groupList.get(i);
            groupTranslationXArr[i] = group.getTranslationX();
        }
        final float childWidthCount = childSize * 120;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(6000);
        valueAnimator.setFloatValues(1f, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                for (int i = 0; i < groupSize; ++i) {
                    if (i > clickIndex) {
                        View group = groupList.get(i);
                        group.setTranslationX(groupTranslationXArr[i] - (childWidthCount - (childWidthCount * value)));//0-600
                    }
                }
                for (int i = 0; i < childSize; ++i) {
                    View child = childList.get(i);
                    child.setScaleX(value);
                    child.setTranslationX(childTranslationXArr[i] - (i * (120 - (value * 120))));//0-120
                }
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (View view : childList) {
                    ll1.removeView(view);
                }
                for (int i = 0; i < groupSize; ++i) {
                    if (i > clickIndex) {
                        View group = groupList.get(i);
                        group.setTranslationX(groupTranslationXArr[i]);//0-600
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.start();
    }
}
