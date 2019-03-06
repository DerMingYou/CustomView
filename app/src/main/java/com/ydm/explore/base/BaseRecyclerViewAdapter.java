package com.ydm.explore.base;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

/**
 * Description:
 * Data：2019/3/6-14:32
 * Author: DerMing_You
 */
public abstract class BaseRecyclerViewAdapter <T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }

    protected void onItemHolderClick(RecyclerView.ViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        } else {
            throw new IllegalStateException("Please call setOnItemClickListener method set the click event listeners");
        }
    }
}
