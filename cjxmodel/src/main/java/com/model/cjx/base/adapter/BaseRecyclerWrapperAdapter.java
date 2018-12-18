package com.model.cjx.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * create by cjx on 2018/12/17
 */
public abstract class BaseRecyclerWrapperAdapter<T> extends BaseRecyclerAdapter<T> {

    private final int HEADER_TYPE = 199000;
    private final int FOOTER_TYPE = 200000;

    public BaseRecyclerWrapperAdapter(ArrayList<T> list, Context context) {
        super(list, context);
    }

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    public boolean isHeaderViewPosition(int position) {
        return position < mHeaderViews.size();
    }

    public boolean isFooterViewPosition(int position) {
        return position >= mHeaderViews.size() + count;
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + HEADER_TYPE, view);
    }

    public void addFooterView(View view) {
        mFooterViews.put(mFooterViews.size() + FOOTER_TYPE, view);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if (mHeaderViews.get(type) != null) {
            return new WrapperViewHolder(mHeaderViews.get(type));
        } else if (mFooterViews.get(type) != null) {
            return new WrapperViewHolder(mFooterViews.get(type));
        } else {
            return bindViewHolder(createView(viewGroup, inflater, type), type);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPosition(position)) {
            return HEADER_TYPE + position;
        } else if (isFooterViewPosition(position)) {
            return FOOTER_TYPE + position - mHeaderViews.size() - count;
        } else {
            return position;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (isHeaderViewPosition(position) || isFooterViewPosition(position)) {
            return;
        }
        wrapperBingViewHolder(viewHolder, position - mHeaderViews.size());
    }

    /**
     * 适应grid布局的头尾适配器
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (mHeaderViews.get(type) != null || mFooterViews.get(type) != null) {
                        return manager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
            manager.setSpanCount(((GridLayoutManager) layoutManager).getSpanCount());
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * 适应staggeredGrid布局的头尾适配器
     */
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        if (isHeaderViewPosition(position) || isFooterViewPosition(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
            }
        }
    }

    /**
     * header的个数
     */
    int getHeaderCount() {
        return mHeaderViews.size();
    }

    /**
     * footer的个数
     */
    int getFooterCount() {
        return mFooterViews.size();
    }

    /**
     * 绑定数据到itemview上
     */
    abstract void wrapperBingViewHolder(RecyclerView.ViewHolder holder, int position);

    class WrapperViewHolder extends RecyclerView.ViewHolder {
        WrapperViewHolder(View view) {
            super(view);
        }
    }
}
