package com.witcher.testrecyleview1;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by witcher on 2017/9/22.
 */

public class MyAdapter extends RecyclerView.Adapter{

    public static final int TYPE_GROUP = 1;
    public static final int TYPE_CHILD = 2;
    public static final int ANIM_TIME = 8000;

    private ArrayList<Group> groups;
    private ArrayList<Object> data = new ArrayList<>();

    private Context context;
    private final LayoutInflater mLayoutInflater;


    public MyAdapter(Context context, ArrayList<Group> groups) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.groups = groups;
        data.addAll(groups);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GROUP: {
                View view = mLayoutInflater.inflate(R.layout.item_exlist, parent, false);
                return new GroupViewHolder(view);
            }
            case TYPE_CHILD: {
                View view = mLayoutInflater.inflate(R.layout.item_exlist_child, parent, false);
                return new ChildViewHolder(view);
            }
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GroupViewHolder) {
            if (data.get(position) instanceof Group) {
                GroupViewHolder viewHolder = (GroupViewHolder) holder;
                Group bean = (Group) data.get(position);
                bindGroupItem(viewHolder, bean, position);
            }
        } else if (holder instanceof ChildViewHolder) {
            if (data.get(position) instanceof Child) {
                ChildViewHolder viewHolder = (ChildViewHolder) holder;
                Child bean = (Child) data.get(position);
                bindChildItem(viewHolder, bean, position);
            }
        }
    }

    private void bindGroupItem(final GroupViewHolder viewHolder, final Group bean, final int position) {
        viewHolder.tvName.setText(bean.getName());
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bean.isZhankai){
                    data.removeAll(bean.children);
                    MyAdapter.this.notifyItemRangeRemoved(position+1,bean.children.size());
                    MyAdapter.this.notifyItemRangeChanged(position+1,bean.children.size());
//                    MyAdapter.this.notifyDataSetChanged();
                    animateClose(viewHolder,position);
                }else{
                    data.addAll(position+1,bean.children);
                    MyAdapter.this.notifyItemRangeInserted(position+1,bean.children.size());
                    MyAdapter.this.notifyItemRangeChanged(position+1,bean.children.size());
                    MyAdapter.this.notifyDataSetChanged();
                    animateOpen(viewHolder, bean,position);
                }
                bean.isZhankai = !bean.isZhankai;
            }
        });
    }


    private void animateOpen(GroupViewHolder viewHolder, final Group bean, final int position){
        //1.点击item位置把父视图位移到屏幕左侧正好
        //2.子视图动态提升宽度
        //3.子视图要动态改变X轴缩放
        //4.子视图要往左右两边扩散
        final View item = viewHolder.itemView;
        final ViewGroup vp = (ViewGroup) item.getParent();
        vp.post(new Runnable() {
            @Override
            public void run() {
               final ArrayList<View> childs = new ArrayList<>();
                int size = bean.children.size();
                for(int i=0;i<size;++i){
                    View child = vp.getChildAt(position + i+1);
                    if(child == null){
                        continue;
                    }
                    childs.add(child);
                    child.setScaleX(0f);
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                    lp.width = 0;
                }
                vp.requestLayout();
                ValueAnimator valueAnimator = new ValueAnimator();
                valueAnimator.setDuration(ANIM_TIME);
                valueAnimator.setIntValues(0,120);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        Integer value = (Integer) valueAnimator.getAnimatedValue();
                        for(View child : childs){
                            if(child == null){
                                continue;
                            }
                            float scaleValue = value/120f;
                            L.i("scaleValue:"+scaleValue);
                            child.setScaleX(scaleValue);
                            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                            lp.width = value;
                        }
                        vp.requestLayout();
                    }
                });
                valueAnimator.start();
            }
        });
    }

    private void animateClose(GroupViewHolder viewHolder,int position){

    }






    private void bindChildItem(ChildViewHolder viewHolder,final Child bean, int position) {
        viewHolder.tvName.setText(bean.getName());
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,bean.getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof Group) {
            return TYPE_GROUP;
        } else if (data.get(position) instanceof Child) {
            return TYPE_CHILD;
        }
        return super.getItemViewType(position);
    }
    private static class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        View root;

        GroupViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            root = itemView.findViewById(R.id.rl_root);
        }
    }
    private static class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        View root;

        ChildViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            root = itemView.findViewById(R.id.rl_root);
        }
    }
}
