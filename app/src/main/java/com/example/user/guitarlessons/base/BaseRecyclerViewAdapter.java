package com.example.user.guitarlessons.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.user.guitarlessons.ModelType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 25.02.2018.
 */

public abstract class BaseRecyclerViewAdapter<T extends ModelType, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected List<T> mList = new ArrayList<>();

    public void setList(List<T> list) {
        if (this.mList != null) {
            this.mList.clear();
            this.mList.addAll(list);
        }
    }

    public void addList(List<T> list) {
        if (mList != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void removeObject(int index) {
        if (mList != null) {
            mList.remove(index);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
