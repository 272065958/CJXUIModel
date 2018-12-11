package com.example.cjx.uimodeldemo;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.model.cjx.base.fragment.BaseFragment;

public class ThirdFragment extends BaseFragment {

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View view = new View(getContext());
        view.setBackgroundColor(Color.BLACK);
        return view;
    }
}
