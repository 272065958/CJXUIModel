package com.example.cjx.uimodeldemo;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.model.cjx.base.activity.BaseMainTabActivity;
import com.model.cjx.base.bean.TabBean;
import com.model.cjx.delegate.DelegateBottomTabView;

public class MainActivity extends BaseMainTabActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MainFragment()).commit();
//    }


    @NonNull
    @Override
    protected DelegateBottomTabView getDelegateBottomTabView() {
        return new DelegateBottomTabView(getSupportFragmentManager()) {
            @Override
            public TabBean[] getTabs() {
                return new TabBean[]{
                        new TabBean(R.mipmap.ic_launcher_round, "first"),
                        new TabBean(R.mipmap.ic_launcher_round, "second"),
                        new TabBean(R.mipmap.ic_launcher_round, "third"),
                };
            }

            @Override
            public Fragment getFragment(int position) {
                Fragment fragment;
                switch (position) {
                    case 0:
                        fragment = new FirstFragment();
                        break;
                    case 1:
                        fragment = new SecondFragment();
                        break;
                    case 2:
                    default:
                        fragment = new ThirdFragment();
                        break;
                }
                return fragment;
            }
        };
    }
}
