package com.model.cjx.base.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.model.cjx.R;
import com.model.cjx.delegate.DelegateBottomTabView;

/**
 * create by cjx on 2018/12/13
 * 底部放tab的分类界面
 */
public abstract class BaseMainTabActivity extends BaseActivity {
    DelegateBottomTabView delegateBottomTabView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_main_tab);

        delegateBottomTabView = getDelegateBottomTabView();
        ViewGroup tabContentView = findViewById(R.id.main_tab_content);
        int mainViewId = R.id.main_view;
        delegateBottomTabView.initTabContent(this, mainViewId, tabContentView);
    }

    protected abstract @NonNull DelegateBottomTabView getDelegateBottomTabView();
}
