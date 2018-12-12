package com.model.cjx.delegate;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.model.cjx.R;
import com.model.cjx.base.bean.TabBean;

/**
 * create by cjx on 2018/12/12
 */
public abstract class DelegateBottomTabView {
    /**
     * 上次选中位置
     */
    private int prevIndex = -1;
    /**
     * 用来替换界面的容器id
     */
    private int mainViewId = 0;
    /**
     * 上次展示的界面
     */
    private Fragment prevFragment = null;
    /**
     * 底部tab点击事件
     */
    private View.OnClickListener tabClickListener;
    /**
     * 存储底部icon的集合
     */
    private ImageView[] iconViews;
    /**
     * 存储底部text的集合
     */
    private TextView[] titleViews;
    /**
     * 存储底部消息提示的集合
     */
    private TextView[] badgeViews = null;
    /**
     * 底部的tab
     */
    private TabBean[] tabs;
    private Fragment[] fragments;

    private FragmentManager manager;
    private Context context;

    private int textSelectColor = Color.GRAY;
    private int textNormalColor = Color.BLACK;

    public DelegateBottomTabView(FragmentManager manager, Context context) {
        this.manager = manager;
        this.context = context;
    }

    public DelegateBottomTabView setTextColor(int textNormalColor, int textSelectColor) {
        this.textNormalColor = textNormalColor;
        this.textSelectColor = textSelectColor;
        return this;
    }

    /**
     * 初始化底部tab
     */
    public void initTabContent(int mainViewId, ViewGroup tabContentView) {
        this.mainViewId = mainViewId;
        this.tabs = getTabs();
        int size = tabs.length;
        if (size == 0) {
            return;
        }
        initData(size);
        for (int i = 0; i < size; i++) {
            tabContentView.addView(createItemTabView(i, tabs[i]));
        }
        selectPosition(0);
    }

    /**
     * 设置指定下标的提示消息数
     */
    public void setBadge(int position, int count) {
        if (titleViews == null) {
            return;
        }
        TextView badgeView = createBadgeView(position);
        if (count < 1) {
            badgeView.setVisibility(View.GONE);
        } else {
            badgeView.setVisibility(View.VISIBLE);
            badgeView.setText(String.valueOf(count));
        }
    }

    /**
     * 获取指定下标的提示消息数的控件
     */
    protected TextView createBadgeView(int position) {
        if (badgeViews == null) {
            badgeViews = new TextView[titleViews.length];
        }
        TextView badgeView;
        if (badgeViews[position] ==null){
            TextView view = getBadgeView(context);
            badgeViews[position] = view;
            ((ViewGroup)iconViews[position].getParent()).addView(view);
            badgeView = view;
        } else{
            badgeView = badgeViews[position];
        }
        return badgeView;
    }

    /**
     * 获取一个底部tab的view
     *
     * @param position 所在的位置
     * @param tab      tab的内容
     * @return 返回一个tabview
     */
    protected View createItemTabView(int position, TabBean tab) {
        View v = View.inflate(context, R.layout.item_main_tab, null);
        v.setTag(position);
        v.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        ImageView iconView = v.findViewById(R.id.main_tab_icon);
        iconView.setImageResource(tab.getIcon());
        iconViews[position] = iconView;
        TextView titleView = v.findViewById(R.id.main_tab_text);
        titleView.setText(tab.getTitle());
        titleViews[position] = titleView;
        v.setOnClickListener(tabClickListener);
        return v;
    }

    /**
     * 设置选中位置tab样式
     *
     * @param currentTab 选中位置
     */
    protected void setCurrentTab(int currentTab) {
        if (prevIndex != -1) {
            iconViews[prevIndex].setSelected(false);
            titleViews[prevIndex].setTextColor(textNormalColor);
        }
        iconViews[currentTab].setSelected(true);
        titleViews[currentTab].setTextColor(textSelectColor);
        prevIndex = currentTab;
    }

    /**
     * 初始化数组
     */
    private void initData(int count) {
        tabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition((int) v.getTag());
            }
        };
        iconViews = new ImageView[count];
        titleViews = new TextView[count];
        fragments = new Fragment[count];
    }

    /**
     * 设置当前选中位置
     *
     * @param tabPosition 选中位置
     */
    private void selectPosition(int tabPosition) {
        if (prevIndex == tabPosition) {
            return;
        }
        Fragment fragment = fragments[tabPosition] == null ?
                getFragment(tabPosition) : fragments[tabPosition];
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (prevFragment != null) {
            fragmentTransaction.hide(prevFragment);
        }
        if (tabs[tabPosition].getAttach()) {
            fragmentTransaction.show(fragment);
        } else {
            fragments[tabPosition] = fragment;
            fragmentTransaction.add(mainViewId, fragment);
            tabs[tabPosition].setAttach(true);
        }
        prevFragment = fragment;
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
        setCurrentTab(tabPosition);
    }

    /**
     * 创建一个标签view
     */
    private TextView getBadgeView(Context context) {
        TextView textView = new TextView(context);
        Resources resources = context.getResources();
        int size = resources.getDimensionPixelSize(R.dimen.fab_margin);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.leftMargin = resources.getDimensionPixelOffset(R.dimen.bigger_margin);
        textView.setLayoutParams(lp);
        textView.setBackgroundResource(R.drawable.notice_bg);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(11f);
        textView.setTextColor(Color.WHITE);
        textView.setVisibility(View.INVISIBLE);
        return textView;
    }



    /**
     * 获取底部tab的数组
     */
    abstract TabBean[] getTabs();

    /**
     * 获取指定位置的view
     */
    abstract Fragment getFragment(int position);
}
