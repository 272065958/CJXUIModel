package com.model.cjx.base.fragment;

import com.model.cjx.delegate.DelegateLoadView;
import com.model.cjx.delegate.DelegateRecyclerView;

import java.util.ArrayList;

/**
 * create by cjx on 2018/12/18
 */
public abstract class BaseRecyclerFragment<T> extends BaseLoadFragment {

    DelegateRecyclerView<T> delegateRecyclerView;

    @Override
    DelegateLoadView getDelegateLoadView() {
        delegateRecyclerView = getDelegateRecyclerView();
        return delegateRecyclerView;
    }

    public void displayList(ArrayList<T> list, boolean isLastPage) {
        delegateRecyclerView.displayList(list, isLastPage);
    }

    public void displayList(ArrayList<T> list, boolean isLastPage,
                            String emptyTitle, int emptyIcon) {
        delegateRecyclerView.displayList(list, isLastPage, emptyTitle, emptyIcon);
    }

    abstract DelegateRecyclerView<T> getDelegateRecyclerView();
}
