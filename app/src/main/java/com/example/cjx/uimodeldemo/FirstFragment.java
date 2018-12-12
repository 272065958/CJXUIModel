package com.example.cjx.uimodeldemo;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.model.cjx.base.fragment.BaseLoadFragment;

public class FirstFragment extends BaseLoadFragment {

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View view = super.createView(inflater, container);
        view.setBackgroundColor(Color.CYAN);
        return view;
    }

    @Override
    public View createContentView() {
        TextView textView = new TextView(getContext());
        textView.setText("加载完成");
        textView.setBackgroundColor(Color.YELLOW);
        return textView;
    }

    @Override
    public void loadData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endLoad();
                        displayContentView();
                    }
                });
            }
        }.start();
    }
}
