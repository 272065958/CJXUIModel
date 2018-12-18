package com.example.cjx.uimodeldemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.model.cjx.base.adapter.BaseRecyclerAdapter;
import com.model.cjx.base.fragment.BaseRecyclerFragment;

import java.util.ArrayList;

public class SecondFragment extends BaseRecyclerFragment<String> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOpenLoadMore(true);
        setOpenRefresh(true);
    }

    @NonNull
    @Override
    protected View createView(@NonNull LayoutInflater inflater, ViewGroup container) {
        View view = super.createView(inflater, container);
        view.setBackgroundColor(Color.RED);
        return view;
    }

    @NonNull
    @Override
    public BaseRecyclerAdapter<String> getMyBaseAdapter(ArrayList<String> list) {
        return new TestAdapter(list, getContext());
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
                        ArrayList<String> list = new ArrayList<>();
                        list.add("测试一");
                        list.add("测试二");
                        list.add("测试三");
                        list.add("测试四");
                        list.add("测试五");
                        list.add("测试六");
                        list.add("测试七");
                        list.add("测试八");
                        list.add("测试九");
                        list.add("测试市");
                        list.add("测试1");
                        list.add("测试2");
                        list.add("测试3");
                        endLoad();
                        displayList(list, false, null, 0);
                    }
                });
            }
        }.start();
    }

    class TestAdapter extends BaseRecyclerAdapter<String> {

        TestAdapter(ArrayList<String> list, Context context) {
            super(list, context);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            MyViewHolder ho = (MyViewHolder) viewHolder;
            String string = getList().get(i);
            ho.textView.setText(string);
        }

        @NonNull
        @Override
        public View createView(ViewGroup viewGroup, @NonNull LayoutInflater inflater, int viewType) {
            return inflater.inflate(R.layout.item_test, parentView, false);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder bindViewHolder(@NonNull View view, int viewType) {
            return new MyViewHolder(view);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            MyViewHolder(View view) {
                super(view);
                textView = view.findViewById(R.id.text);
            }

        }
    }
}
