package com.model.cjx.component;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.model.cjx.R;

/**
 * create by cjx on 2018/12/17
 */
public class EmptyView extends LinearLayout {

    ImageView iconView = null;
    TextView clickTipView = null;
    TextView titleView;

    public EmptyView(Context context) {
        super(context);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        titleView = new TextView(getContext());
        titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.cjx_text_main_color));
        addView(titleView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        int padding = getResources().getDimensionPixelOffset(R.dimen.double_fab_margin);
        setPadding(padding, padding, padding, padding);
    }

    /**
     * 设置界面显示数据
     */
    public void setEmptyData(String emptyTitle, int emptyIcon) {
        titleView.setText(emptyTitle);
        if (emptyIcon > 0) {
            if (iconView == null) {
                iconView = new ImageView(getContext());
                int size = getResources().getDimensionPixelSize(R.dimen.empty_icon);
                LayoutParams lp = new LayoutParams(size, size);
                addView(iconView, 0, lp);
            } else if (iconView.getVisibility() == View.GONE) {
                iconView.setVisibility(View.VISIBLE);
            }
            iconView.setImageResource(emptyIcon);
        } else if (iconView != null) {
            iconView.setImageDrawable(null);
            iconView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置点击事件
     */
    public void setClickListener(OnClickListener l, String tip) {
        if (clickTipView == null) {
            clickTipView = new TextView(getContext());
            clickTipView.setTextColor(ContextCompat.getColor(getContext(), R.color.cjx_text_main_color));
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = getResources().getDimensionPixelOffset(R.dimen.text_margin);
            addView(clickTipView, lp);
        }
        if (TextUtils.isEmpty(tip)) {
            clickTipView.setText("可以点我刷新 (｡･ω･｡)");
        } else {
            clickTipView.setText(tip);
        }
        super.setOnClickListener(l);
    }
}
