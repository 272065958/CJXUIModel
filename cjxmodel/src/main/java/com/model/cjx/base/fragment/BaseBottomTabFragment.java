package com.model.cjx.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.model.cjx.R;
import com.model.cjx.delegate.DelegateBottomTabView;


/**
 * create by cjx on 2018/12/13
 * 底部放tab的分类界面
 */
public abstract class BaseBottomTabFragment extends BaseFragment {

    protected DelegateBottomTabView delegateBottomTabView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegateBottomTabView = getDelegateBottomTabView();
    }

    @Override
    protected View createView(@NonNull LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.activity_base_main_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup tabContentView = view.findViewById(R.id.main_tab_content);
        int mainViewId = R.id.main_view;
        delegateBottomTabView.initTabContent(getContext(), mainViewId, tabContentView);

    }

    protected abstract @NonNull DelegateBottomTabView getDelegateBottomTabView();
}
