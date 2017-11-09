package com.witcher.testrecyleview1;

import android.animation.Animator;
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
 *
 */

public class MyAdapter extends RecyclerView.Adapter {

    private static final int TYPE_GROUP = 1;
    private static final int TYPE_CHILD = 2;

    private static final int ANIM_TIME = 6000;
    private static final boolean IS_NOLY_ONE_OPEN = false;

    private ArrayList<Object> mData = new ArrayList<>();
    private ArrayList<Group> mGroupList;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private final LayoutInflater mLayoutInflater;

    private int mOpenAnimLastScrollByX;
    private final ArrayList<View> mCurrentCloseChildList = new ArrayList<>();
    private ValueAnimator mCloseValueAnimator;
    private ValueAnimator mOpenValueAnimator;
    private Group mCurrentCloseGroup;


    public MyAdapter(Context context, ArrayList<Group> groups, RecyclerView rv) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mGroupList = groups;
        this.mRecyclerView = rv;
        mData.addAll(groups);
        initValueAnimator();
    }

    private void initValueAnimator() {
        mCloseValueAnimator = new ValueAnimator();
        mOpenValueAnimator = new ValueAnimator();
        mCloseValueAnimator.setDuration(ANIM_TIME);
        mOpenValueAnimator.setDuration(ANIM_TIME);
        mOpenValueAnimator.setFloatValues(0, 1f);
        mCloseValueAnimator.setFloatValues(0, 1f);
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
            if (mData.get(position) instanceof Group) {
                GroupViewHolder viewHolder = (GroupViewHolder) holder;
                Group bean = (Group) mData.get(position);
                bindGroupItem(viewHolder, bean, position);
            }
        } else if (holder instanceof ChildViewHolder) {
            if (mData.get(position) instanceof Child) {
                ChildViewHolder viewHolder = (ChildViewHolder) holder;
                Child bean = (Child) mData.get(position);
                bindChildItem(viewHolder, bean, position);
            }
        }
    }

    private void bindGroupItem(final GroupViewHolder viewHolder, final Group bean, final int position) {
        viewHolder.tvName.setText(bean.getName());
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.isZhankai) {
                    for (Child child : bean.children) {
                        child.isFirstBind = true;
                    }
                    animateClose(viewHolder, bean, position);
                } else {
                    mData.addAll(position + 1, bean.children);
                    MyAdapter.this.notifyItemRangeInserted(position + 1,bean.children.size());
                    MyAdapter.this.notifyItemRangeChanged(position + 1, bean.children.size());
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            animateOpen(viewHolder, bean);
                        }
                    });
                }
                bean.isZhankai = !bean.isZhankai;
            }
        });
    }

    //第一次bind都是0 后面都是60  如果点的是屏幕上没显示完的最后一个..先滚动到屏幕内 然后再做这个函数
    //
    private void animateOpen(final GroupViewHolder viewHolder, final Group bean) {
        //屏幕宽度 - X < view宽度
        int outScreenRight = (int) (mRecyclerView.getWidth() - viewHolder.itemView.getX());
        if(outScreenRight<120){
            //滚动到屏幕内再做后续动作
           int firstScroll = 120 - outScreenRight + 1;
           mRecyclerView.scrollBy(firstScroll,0);
           animateOpen(viewHolder,bean);
           return;
        }
        if(IS_NOLY_ONE_OPEN){
            int groupSize = mGroupList.size();
            for(int i= 0; i<groupSize;++i){
                Group group = mGroupList.get(i);
                if(group!=bean&&group.isZhankai){
                    mData.removeAll(group.children);
                    MyAdapter.this.notifyItemRangeRemoved(i + 1,group.children.size());
                    MyAdapter.this.notifyItemRangeChanged(i + 1, group.children.size());
                    group.isZhankai = false;
                }
            }
        }
        mOpenAnimLastScrollByX = 0;
        final ArrayList<View> childList = new ArrayList<>();
        int start = mRecyclerView.indexOfChild(viewHolder.itemView) + 1;
        int end = start + bean.children.size();
        for (int i = start; i < end; ++i) {
            if (mRecyclerView.getChildAt(i) != null) {
                childList.add(mRecyclerView.getChildAt(i));
            }
        }
        final int childSize = childList.size();
        final int marginScreenLeft = (int) viewHolder.itemView.getX();
        //这个LEFT的数据控制一下
        mOpenValueAnimator.removeAllUpdateListeners();
        mOpenValueAnimator.removeAllListeners();
        mOpenValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                for (int i = 0; i < childSize; ++i) {
                    View child = childList.get(i);
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                    lp.width = (int) (value * 120);
                }
                int newBy = (int) (value * marginScreenLeft);
                int by = newBy - mOpenAnimLastScrollByX;
                mRecyclerView.scrollBy(by, 0);
                mRecyclerView.requestLayout();
                mOpenAnimLastScrollByX = newBy;
            }
        });
        mOpenValueAnimator.start();
//        if(bean.name.equals("8")){
//            Group group = (Group)mData.get(4);
//            mData.removeAll(group.children);
//            MyAdapter.this.notifyItemRangeRemoved(5+1,group.children.size());
//            MyAdapter.this.notifyItemRangeChanged(5 + 1, group.children.size());
//        }
    }

    //动画过程中滑进来的item也立刻跟着这个一起变
    private void animateClose(GroupViewHolder viewHolder, final Group bean, final int position) {
        mCurrentCloseGroup = bean;
        mCurrentCloseChildList.clear();
        int start = mRecyclerView.indexOfChild(viewHolder.itemView) + 1;
        int end = start + bean.children.size();
        for (int i = start; i < end; ++i) {
            if (mRecyclerView.getChildAt(i) != null) {
                mCurrentCloseChildList.add(mRecyclerView.getChildAt(i));
            }
        }

        mCloseValueAnimator.removeAllUpdateListeners();
        mCloseValueAnimator.removeAllListeners();
        mCloseValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                int childSize = mCurrentCloseChildList.size();
                for (int i = 0; i < childSize; ++i) {
                    View child = mCurrentCloseChildList.get(i);
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                    lp.width = 120 - (int) (value * 120);
                }
                mRecyclerView.requestLayout();
            }
        });
        mCloseValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mData.removeAll(bean.children);
                MyAdapter.this.notifyItemRangeRemoved(position+1,bean.children.size());
                MyAdapter.this.notifyItemRangeChanged(position + 1, bean.children.size());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mCloseValueAnimator.start();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof ChildViewHolder) {
            if (mCloseValueAnimator.isRunning()) {
                if (((ChildViewHolder) holder).group == mCurrentCloseGroup) {
                    mCurrentCloseChildList.add(holder.itemView);
                }
            }
        }
    }

    private void bindChildItem(ChildViewHolder viewHolder, final Child bean, int position) {
        if (bean.isFirstBind) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            lp.width = 0;
            bean.isFirstBind = false;
        } else {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            lp.width = 120;
        }
        mRecyclerView.requestLayout();
        viewHolder.group = bean.group;
        viewHolder.tvName.setText(bean.getName());
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, bean.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof Group) {
            return TYPE_GROUP;
        } else if (mData.get(position) instanceof Child) {
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
        Group group;

        ChildViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            root = itemView.findViewById(R.id.fl_root);
        }
    }
}
