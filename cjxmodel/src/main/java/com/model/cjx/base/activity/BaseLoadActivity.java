package com.model.cjx.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.model.cjx.delegate.DelegateLoadView;

/**
 * create by cjx on 2018/12/17
 */
public abstract class BaseLoadActivity extends BaseActivity {
    protected DelegateLoadView delegateLoadView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegateLoadView = getDelegateLoadView();
        setContentView(delegateLoadView.createParentView());
        delegateLoadView.init();
    }

    abstract DelegateLoadView getDelegateLoadView();
}
