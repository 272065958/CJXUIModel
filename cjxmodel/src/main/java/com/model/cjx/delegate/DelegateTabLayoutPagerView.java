package com.model.cjx.delegate;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.model.cjx.R;

import java.util.ArrayList;

/**
 * create by cjx on 2018/12/18
 */
public abstract class DelegateTabLayoutPagerView implements TabLayout.OnTabSelectedListener {

    /**
     * 顶部tab控件
     */
    private TabLayout tabLayout;
    /**
     * 翻页控件
     */
    private ViewPager viewPager;

    private int selectColor;
    private int normalColor;

    public DelegateTabLayoutPagerView(int normalColor, int selectColor) {
        this.selectColor = selectColor;
        this.normalColor = normalColor;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public View createView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_tablayout_pager, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        tabLayout.addOnTabSelectedListener(this);
        initTabTheme();

        return view;
    }

    /**
     * 设置tab的主题色
     */
    protected void initTabTheme() {
        tabLayout.setSelectedTabIndicatorColor(selectColor);
        tabLayout.setTabTextColors(normalColor, selectColor);
    }

    /**
     * 绑定翻页控件
     */
    public void setupView(FragmentManager fragmentManager) {
        viewPager.setAdapter(new ViewsAdapter(fragmentManager, getTitles()));
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 获取tab的文字数组
     */
    abstract ArrayList<String> getTitles();

    /**
     * 获取对应的fragment
     */
    abstract Fragment getFragment(int position);

    class ViewsAdapter extends FragmentPagerAdapter {
        private ArrayList<String> titles;

        public ViewsAdapter(FragmentManager fm, ArrayList<String> titles) {
            super(fm);
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
