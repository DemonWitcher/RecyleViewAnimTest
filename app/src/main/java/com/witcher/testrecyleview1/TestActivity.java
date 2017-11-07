package com.witcher.testrecyleview1;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by witcher on 2017/11/7.
 */

public class TestActivity extends AppCompatActivity {

    LinearLayout ll1;
    View view1, view2, view3, view4, view5, view6;
    View child1,child2,child3,child4,child5;

    ArrayList<View> childList = new ArrayList<>();
    ArrayList<View> groupList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        view1 = findViewById(R.id.rl1);
        view2 = findViewById(R.id.rl2);
        view3 = findViewById(R.id.rl3);
        view4 = findViewById(R.id.rl4);
        view5 = findViewById(R.id.rl5);
        view6 = findViewById(R.id.rl6);
        ll1 = findViewById(R.id.ll1);
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });
    }

    private void open() {
        child1 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child, null);
        child2 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child, null);
        child3 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child, null);
        child4 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child, null);
        child5 = LayoutInflater.from(this).inflate(R.layout.item_exlist_child, null);
        ((TextView)child1.findViewById(R.id.tv_name)).setText("1");
        ((TextView)child2.findViewById(R.id.tv_name)).setText("2");
        ((TextView)child3.findViewById(R.id.tv_name)).setText("3");
        ((TextView)child4.findViewById(R.id.tv_name)).setText("4");
        ((TextView)child5.findViewById(R.id.tv_name)).setText("5");
        ll1.addView(child5, 4);
        ll1.addView(child4, 4);
        ll1.addView(child3, 4);
        ll1.addView(child2, 4);
        ll1.addView(child1, 4);
        child1.setScaleX(0);
        child2.setScaleX(0);
        child3.setScaleX(0);
        child4.setScaleX(0);
        child5.setScaleX(0);
        child2.setTranslationX(-120);
        child3.setTranslationX(-240);
        child4.setTranslationX(-360);
        child5.setTranslationX(-480);
        view5.setTranslationX(-600);
        view6.setTranslationX(-600);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(6000);
        valueAnimator.setFloatValues(0, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                float leftTranslation = -value * 360;
                float rightTranslation = -(600 -(value * 240));//-600 - -360
                view1.setTranslationX(leftTranslation);
                view2.setTranslationX(leftTranslation);
                view3.setTranslationX(leftTranslation);
                view4.setTranslationX(leftTranslation);
                view5.setTranslationX(rightTranslation);
                view6.setTranslationX(rightTranslation);

                child1.setScaleX(value);
                child2.setScaleX(value);
                child3.setScaleX(value);
                child4.setScaleX(value);
                child5.setScaleX(value);
                //一开始左侧间距是一个 然后越来越小    缩放的左侧留出的空白没计算在内 60-0 view.width/2 - 0
                float scaleLeftMargin = 60 -(value * 60);
                child1.setTranslationX(-value*360 - scaleLeftMargin);//0- -360  0-width * leftSize
                child2.setTranslationX(-120 -(value * 240)-scaleLeftMargin);//-120 - -360 -width - -width*leftSize
                child3.setTranslationX(-240 -(value * 120)-scaleLeftMargin);//-240 - -360

                child4.setTranslationX(-360-scaleLeftMargin);//-360 - -360
                child5.setTranslationX(-480 +(value * 120)-scaleLeftMargin);//-480 - -360
            }
        });
        valueAnimator.start();
    }
}
