package com.model.cjx.base.fragment;

import com.model.cjx.delegate.DelegateRecyclerView;
import com.model.cjx.delegate.DelegateTreeSelectView;

/**
 * create by cjx on 2018/12/18
 */
public abstract class BaseTreeSelectFragment<T> extends BaseRecyclerFragment<T> {
    DelegateTreeSelectView<T> delegateTreeSelectView;

    @Override
    DelegateRecyclerView<T> getDelegateRecyclerView() {
        delegateTreeSelectView = getDelegateTreeSelectView();
        return delegateTreeSelectView;
    }

    protected abstract DelegateTreeSelectView<T> getDelegateTreeSelectView();
}
