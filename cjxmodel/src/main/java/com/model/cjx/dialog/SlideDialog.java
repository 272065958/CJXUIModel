package com.model.cjx.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import com.model.cjx.R;

/**
 * create by cjx on 2018/12/17
 */
public class SlideDialog extends TagDialog {
    public SlideDialog(Context context) {
        super(context);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setWindowAnimations(R.style.slide_dialog);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
