package com.model.cjx.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * create by cjx on 2018/12/17
 */
public class FooterLoadRecyclerView extends RecyclerView {

    FooterLoadListener footerLoadListener;
    boolean footerLoadState = false;

    public FooterLoadRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public FooterLoadRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setFooterLoadListener(FooterLoadListener listener) {
        footerLoadListener = listener;
    }

    public void setFooterLoadState(boolean state) {
        footerLoadState = state;
    }

    private void init() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE && !footerLoadState && footerLoadListener != null && isSlideToBottom()) {
                    footerLoadState = true;
                    footerLoadListener.loadMore(FooterLoadRecyclerView.this);
                }
            }
        });
    }

    /**
     * 判断是否滚动到底部
     */
    private boolean isSlideToBottom() {
        return this.computeHorizontalScrollExtent() + this.computeHorizontalScrollOffset() >= this.computeHorizontalScrollRange() &&
                this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset() >= this.computeVerticalScrollRange();
    }

    public interface FooterLoadListener {
        void loadMore(RecyclerView view);
    }
}
