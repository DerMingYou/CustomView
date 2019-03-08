package com.ydm.explore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydm.explore.R;
import com.ydm.explore.base.BaseRecyclerViewAdapter;
import com.ydm.explore.bean.OpenBookBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * Data：2019/3/6-16:38
 * Author: DerMing_You
 */
public class BookOpenModeAdapter extends BaseRecyclerViewAdapter<BookOpenModeAdapter.ViewHolder> {

    private ArrayList<OpenBookBean> values;
    private Context context;

    public BookOpenModeAdapter(ArrayList<OpenBookBean> values, Context context) {
        this.values = values;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_open_mode, parent, false);
        return new ViewHolder(rootView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        OpenBookBean openBookBean = values.get(i);
        viewHolder.ivLogo.setImageResource(openBookBean.getImage());
        viewHolder.tvTitle.setText(openBookBean.getName());

    }

    @Override
    public int getItemCount() {
        return values == null ? 0 : values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View itemView, BookOpenModeAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
