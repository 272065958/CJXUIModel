package com.model.cjx.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.model.cjx.delegate.DelegateLoadView;

/**
 * create by cjx on 2018/12/17
 */
public abstract class BaseLoadFragment extends BaseFragment {
    private DelegateLoadView delegateLoadView;
    private boolean hasLoaded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegateLoadView = getDelegateLoadView();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup viewGroup) {
        return delegateLoadView.createParentView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        delegateLoadView.init();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (hasLoaded) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        } else if (nextAnim > 0) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), nextAnim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    delegateLoadView.startLoad();
                    hasLoaded = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            return anim;
        } else {
            startLoad();
            hasLoaded = true;
            return null;
        }
    }

    public void setMargetTop(int margetTop) {
        if (delegateLoadView != null) {
            delegateLoadView.setTopMargin(margetTop);
        }
    }

    public void setOpenRefresh(boolean openRefresh) {
        if (delegateLoadView != null) {
            delegateLoadView.setOpenRefresh(openRefresh);
        }
    }

    /**
     * 加载网络数据, 第一次初始化和下拉刷新时会被触发
     */
    public void startLoad() {
        delegateLoadView.startLoad();
    }

    /**
     * 关闭加载ui
     */
    public void endLoad() {
        delegateLoadView.endLoad();
    }

    /**
     * 重新加载
     */
    public void reloadData() {
        delegateLoadView.reloadData();
    }

    /**
     * 显示加载进度框
     */
    public void showLoadView() {
        delegateLoadView.showLoadView();
    }

    /**
     * 隐藏加载进度
     */
    public void hideLoadView() {
        delegateLoadView.hideLoadView();
    }

    public void showEmptyView(String emptyTitle, int icon) {
        delegateLoadView.showEmptyView(emptyTitle, icon);
    }

    /**
     * 移除空数据ui
     */
    public void hideEmptyView() {
        delegateLoadView.hideEmptyView();
    }

    abstract DelegateLoadView getDelegateLoadView();
}
