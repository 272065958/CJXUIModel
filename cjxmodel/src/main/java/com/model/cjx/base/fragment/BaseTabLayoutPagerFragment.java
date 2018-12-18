package com.model.cjx.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.model.cjx.delegate.DelegateTabLayoutPagerView;

/**
 * create by cjx on 2018/12/18
 */
public abstract class BaseTabLayoutPagerFragment extends BaseFragment {

    DelegateTabLayoutPagerView delegateTabLayoutPagerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegateTabLayoutPagerView = getDelegateTabLayoutPagerView();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup viewGroup) {
        return delegateTabLayoutPagerView.createView(inflater, viewGroup);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim < 1) {
            delegateTabLayoutPagerView.setupView(getChildFragmentManager());
            return super.onCreateAnimation(transit, enter, nextAnim);
        } else {
            Animation anim = AnimationUtils.loadAnimation(getContext(), nextAnim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    delegateTabLayoutPagerView.setupView(getChildFragmentManager());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            return anim;
        }
    }

    abstract DelegateTabLayoutPagerView getDelegateTabLayoutPagerView();
}
