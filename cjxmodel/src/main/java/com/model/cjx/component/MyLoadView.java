package com.model.cjx.component;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.model.cjx.BaseApplication;
import com.model.cjx.R;

/**
 * create by cjx on 2018/12/17
 */
public class MyLoadView extends ProgressBar {
    public MyLoadView(Context context) {
        super(context);
        init();
    }

    public MyLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 适配android版本的加载view
     */
    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (((Activity) getContext()).getApplication() instanceof BaseApplication) {
                setIndeterminateTintList(ColorStateList.valueOf(BaseApplication.instance.getColorPrimaryDark()));
            } else {
                setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.cjx_colorPrimaryDark)));
            }

            setIndeterminateTintMode(PorterDuff.Mode.SRC_ATOP);
        } else {
            if (((Activity) getContext()).getApplication() instanceof BaseApplication) {
                setIndeterminateDrawable(BaseApplication.instance.getLoadDrawable());
            } else {
                setIndeterminateDrawable(ContextCompat.getDrawable(getContext(), R.drawable.loading));
            }
        }
    }
}
