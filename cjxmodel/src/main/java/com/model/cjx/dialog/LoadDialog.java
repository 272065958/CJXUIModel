package com.model.cjx.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.model.cjx.BaseApplication;
import com.model.cjx.R;

/**
 * create by cjx on 2018/12/17
 */
public class LoadDialog extends Dialog {
    private TextView tipView = null;

    public LoadDialog(Context context, String tip) {
        super(context, R.style.loading_dialog);
        setContentView(R.layout.dialog_load);
        setCanceledOnTouchOutside(false);

        if (tip != null) {
            setTip(tip);
        }
    }

    public void setTip(String tip) {
        if (tipView == null) {
            Context context = getContext();
            tipView = new TextView(context);
            if (((Activity) context).getApplication() instanceof BaseApplication) {
                tipView.setTextColor(BaseApplication.instance.getColorPrimary());
            } else {
                tipView.setTextColor(ContextCompat.getColor(context, R.color.cjx_colorPrimary));
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = context.getResources().getDimensionPixelOffset(R.dimen.text_margin);

            ((ViewGroup) findViewById(R.id.dialog_load)).addView(tipView, lp);
        }
        if (TextUtils.isEmpty(tip)) {
            tipView.setVisibility(View.GONE);
        } else {
            tipView.setVisibility(View.VISIBLE);
            tipView.setText(tip);
        }
    }
}
