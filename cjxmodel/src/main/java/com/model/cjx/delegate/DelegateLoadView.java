package com.model.cjx.delegate;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.model.cjx.BaseApplication;
import com.model.cjx.R;
import com.model.cjx.component.EmptyView;
import com.model.cjx.component.MyLoadView;

/**
 * create by cjx on 2018/12/17
 */
public abstract class DelegateLoadView {

    private View contentView;
    public ViewGroup parentView;
    private MyLoadView loadView;
    private EmptyView emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int topMargin;

    private boolean isLoading;
    private boolean isInitContent;

    private boolean openRefresh;

    protected Context context;

    public DelegateLoadView(Context context) {
        this.context = context;
    }

    public void init() {
        showLoadView();
        startLoad();
    }

    public void setOpenRefresh(boolean openRefresh) {
        this.openRefresh = openRefresh;
    }

    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    public ViewGroup createParentView() {
        parentView = new FrameLayout(context);
        return parentView;
    }

    /**
     * 加载网络数据, 第一次初始化和下拉刷新时会被触发
     */
    public void startLoad() {
        isLoading = true;
        loadData();
    }

    /**
     * 关闭加载ui
     */
    public void endLoad() {
        isLoading = false;
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        } else {
            hideLoadView();
        }
    }

    /**
     * 重新加载
     */
    public void reloadData() {
        showLoadView();
        startLoad();
    }

    /**
     * 显示加载进度框
     */
    public void showLoadView() {
        if (loadView == null) {
            loadView = new MyLoadView(context);
            loadView.setLayoutParams(getCenterLayoutParams());
        }
        if (loadView.getParent() == null) {
            parentView.addView(loadView);
        }
    }

    /**
     * 隐藏加载进度
     */
    public void hideLoadView() {
        if (loadView != null) {
            parentView.removeView(loadView);
        }
    }

    public void showEmptyView(String emptyTitle, int icon) {
        if (emptyView == null) {
            emptyView = new EmptyView(context);
            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideEmptyView();
                    reloadData();
                }
            });
        }
        emptyView.setEmptyData(emptyTitle, icon);
        if (emptyView.getParent() == null) {
            parentView.addView(emptyView);
        }
    }

    /**
     * 移除空数据ui
     */
    public void hideEmptyView() {
        if (emptyView != null) {
            parentView.removeView(emptyView);
        }
    }

    /**
     * 初始化下拉刷新的控件
     */
    protected void initRefreshView() {
        swipeRefreshLayout = new SwipeRefreshLayout(context);
        if (((Activity) context).getApplication() instanceof BaseApplication) {
            swipeRefreshLayout.setColorSchemeColors(BaseApplication.instance.getColorPrimaryDark());
        } else {
            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.cjx_colorPrimaryDark));
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoading) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    startLoad();
                }
            }
        });
    }

    /**
     * 初始化加载内容后的布局, 通常在获取数据完成,并且有数据的时候调用
     */
    public void showContentView() {
        // 如果初始化view, 则重新添加view到parentView上, 如果没有, 创建contentView
        if (!isInitContent) {
            isInitContent = true;
            if (openRefresh) {
                initRefreshView();
                contentView = createContentView();
                swipeRefreshLayout.addView(contentView, getFullLayoutParams());
                parentView.addView(swipeRefreshLayout, getFullLayoutParams());
            } else {
                contentView = createContentView();
                parentView.addView(contentView, getFullLayoutParams());
            }
        } else {
            if (openRefresh) {
                if (swipeRefreshLayout.getParent() == null) {
                    parentView.addView(swipeRefreshLayout);
                }
            } else {
                if (contentView.getParent() == null) {
                    parentView.addView(contentView);
                }
            }
        }
    }

    /**
     * 获取默认居中的布局
     */
    FrameLayout.LayoutParams getCenterLayoutParams() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        lp.topMargin = topMargin / 2;
        return lp;
    }

    FrameLayout.LayoutParams getFullLayoutParams() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = topMargin;
        return layoutParams;
    }

    public abstract void loadData();

    public abstract View createContentView();

}
