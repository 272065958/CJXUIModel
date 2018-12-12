package com.example.cjx.uimodeldemo;

import android.os.Bundle;

import com.model.cjx.base.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MainFragment()).commit();
    }


}
