package com.model.cjx.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;
import android.widget.Toast;

import com.model.cjx.R;
import com.model.cjx.base.activity.BaseActivity;

/**
 * create by cjx on 2018/12/17
 */
public class TagDialog extends Dialog {

    Object tag;
    SparseArray<Object> tags;

    public TagDialog(Context context) {
        super(context, R.style.loading_dialog);
    }

    public void setTag(int key, Object tag) {
        if (tags == null) {
            tags = new SparseArray<>();
        }
        tags.put(key, tag);
    }

    public Object getTag(int key) {
        return tags == null ? null : tags.get(key);
    }

    public void showToast(String message) {
        if (getContext() instanceof BaseActivity) {
            ((BaseActivity) getContext()).showToast(message);
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
    }
}
