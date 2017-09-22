package com.witcher.testrecyleview1;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by witcher on 2017/9/22.
 */

public class MyAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,MyViewHolder> {

    private OnItemExpandListener onItemExpandListener;

    public interface OnItemExpandListener{
        void onItemExpand(int index);
    }

    public MyAdapter(List<MultiItemEntity> data,OnItemExpandListener onItemExpandListener) {
        super(data);
        this.onItemExpandListener = onItemExpandListener;
        addItemType(0,R.layout.item_exlist);
        addItemType(1,R.layout.item_exlist_child);
    }

    @Override
    protected void convert(final MyViewHolder helper,final MultiItemEntity item) {
        if(helper.getItemViewType() == 0){
            final Group group = (Group) item;
            helper.setText(R.id.tv_name,group.getName());

            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = helper.getAdapterPosition();
                    if (group.isExpanded()) {
                        collapse(pos);
                    } else {
                        if(onItemExpandListener!=null){
                            onItemExpandListener.onItemExpand(pos);
                        }
                        expand(pos);
                    }
                }});

        }else{
            Child child = (Child) item;
            helper.setText(R.id.tv_name,child.getName());
        }
    }

}
