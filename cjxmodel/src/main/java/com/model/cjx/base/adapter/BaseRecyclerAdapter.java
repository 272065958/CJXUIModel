package com.model.cjx.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * create by cjx on 2018/12/17
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected int count;
    protected ArrayList<T> list;
    protected LayoutInflater inflater;

    public BaseRecyclerAdapter(ArrayList<T> list, Context context) {
        this.list = list;
        setCount();
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        return bindViewHolder(createView(viewGroup, inflater, type), type);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public void notifyDataSetChanged(ArrayList<T> list) {
        this.list = list;
        setCount();
        notifyDataSetChanged();
    }

    public ArrayList<T> getList() {
        return list;
    }

    /**
     * 移除指定位置的一个item
     */
    void itemRemoved(int position) {
        try {
            list.remove(position);
            itemRemovedWidthList(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新计算数据长度, 刷新界面
     */
    void itemRemovedWidthList(int position) {
        setCount();
        notifyItemRemoved(position);
    }

    private void setCount() {
        count = list == null ? 0 : list.size();
    }

    /**
     * 创建一个itemview
     */
    abstract View createView(ViewGroup viewGroup, LayoutInflater inflater, int viewType);

    /**
     * 绑定数据到view上
     */
    abstract RecyclerView.ViewHolder bindViewHolder(View view, int viewType);

}
