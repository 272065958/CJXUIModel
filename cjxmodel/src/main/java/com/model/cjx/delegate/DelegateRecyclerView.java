package com.model.cjx.delegate;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.model.cjx.R;
import com.model.cjx.base.adapter.BaseRecyclerAdapter;
import com.model.cjx.component.FooterLoadRecyclerView;
import com.model.cjx.component.RecyclerViewLinearDivider;

import java.util.ArrayList;

/**
 * create by cjx on 2018/12/17
 */
public abstract class DelegateRecyclerView<T> extends DelegateLoadView {

    private boolean openLoadMore;

    private FooterLoadRecyclerView recyclerView;
    private BaseRecyclerAdapter<T> adapter;
    private FooterLoadRecyclerView.FooterLoadListener footerLoadListener;
    private View loadNextView;

    private int page = 1;
    private int limit = 14;

    public DelegateRecyclerView(Context context) {
        super(context);
    }

    @Override
    public View createContentView() {
        recyclerView = new FooterLoadRecyclerView(context);
        return recyclerView;
    }

    @Override
    public void startLoad() {
        if (openLoadMore) {
            page = 1;
        }
        super.startLoad();
    }

    @Override
    public void endLoad() {
        if (openLoadMore && loadNextView.getVisibility() == View.VISIBLE) {
            loadNextView.setVisibility(View.GONE);
        }
        super.endLoad();
    }

    public void setOpenLoadMore(boolean openLoadMore) {
        this.openLoadMore = openLoadMore;
    }

    public void setPagelimit(int limit) {
        this.limit = limit;
    }

    /**
     * 加载完显示列表数据
     */
    public void displayList(ArrayList<T> list, boolean isLastPage) {
        if (adapter == null) {
            // 初始化显示数据的contentView
            showContentView();
            // 初始化adapter
            adapter = getMyBaseAdapter(list);
            // 设置分割线
            setLayoutManagerAndDivider(this.recyclerView);
            recyclerView.setAdapter(adapter);
        } else {

            if (!openLoadMore || page == 1) {
                // 没有加载下一页功能 或者 当前加载的是第一页
                adapter.notifyDataSetChanged(list);
            } else {
                // 将当前页数据加载到已有数据上
                ArrayList<T> oldList = adapter.getList();
                oldList.addAll(list);
                adapter.notifyDataSetChanged(oldList);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager &&
                        ((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    recyclerView.smoothScrollBy(context.getResources().getDimensionPixelSize(R.dimen.fab_margin), 0);
                } else {
                    recyclerView.smoothScrollBy(0, context.getResources().getDimensionPixelSize(R.dimen.fab_margin));
                }
            }
        }
        // 添加加载下一页的功能
        if (openLoadMore) {
            if (page == 1) {
                // 滚回顶部
                recyclerView.getLayoutManager().scrollToPosition(0);
            }
            if (isLastPage) {
                recyclerView.setFooterLoadListener(null);
            } else {
                recyclerView.setFooterLoadListener(getFooterLoadListener());
            }
            recyclerView.setFooterLoadState(false);
        } else {
            // 滚回顶部
            recyclerView.getLayoutManager().scrollToPosition(0);
        }
    }

    /**
     * 加载完显示列表数据, 检查是否为空数据
     */
    public void displayList(ArrayList<T> list, boolean isLastPage,
                            String emptyTitle, int emptyIcon) {
        // 当前数据是否为null
        if (list == null || list.isEmpty()) {
            // 如果当前页码是1, 需要显示空界面
            if (page == 1) {
                showEmptyView(emptyTitle, emptyIcon);
            }
            if (openLoadMore && recyclerView != null) {
                recyclerView.setFooterLoadListener(null);
                recyclerView.setFooterLoadState(false);
            }
            return;
        } else {
            // 隐藏空界面
            hideEmptyView();
        }

        displayList(list, isLastPage);
    }

    /**
     * 初始化加载下一页的提示view
     */
    private void initLoadNextView() {
        loadNextView = View.inflate(context, R.layout.load_more_view, null);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        loadNextView.setLayoutParams(lp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadNextView.setElevation(context.getResources().getDimension(R.dimen.elevation_bottom_bar));
        }
    }

    /**
     * 获取加载下一个的回调监听
     */
    private FooterLoadRecyclerView.FooterLoadListener getFooterLoadListener() {
        if (footerLoadListener == null) {
            footerLoadListener = new FooterLoadRecyclerView.FooterLoadListener() {
                @Override
                public void loadMore(RecyclerView view) {
                    // 添加下拉刷新组件
                    if (loadNextView == null) {
                        initLoadNextView();
                        parentView.addView(loadNextView);
                    } else {
                        loadNextView.setVisibility(View.VISIBLE);
                    }
                    page++;
                    loadData();
                }
            };
        }
        return footerLoadListener;
    }

    /**
     * 设置recyclerview的排版和分割线
     */
    public void setLayoutManagerAndDivider(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.ItemDecoration decoration = getItemDecoration(LinearLayoutManager.VERTICAL);
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setLayoutManager(manager);
    }

    /**
     * 当前列表的分隔线
     */
    public RecyclerView.ItemDecoration getItemDecoration(int orientation) {
        return new RecyclerViewLinearDivider(orientation, 2, ContextCompat.getColor(context, R.color.cjx_divider_color));
    }


    protected abstract BaseRecyclerAdapter<T> getMyBaseAdapter(ArrayList<T> list);
}
