package com.model.cjx.delegate;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.model.cjx.R;

import java.util.ArrayList;

/**
 * create by cjx on 2018/12/18
 */
public abstract class DelegateTreeSelectView<T> extends DelegateRecyclerView<T> implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private int selectColor;
    private int normalColor;
    private ArrayList<ArrayList<T>> trees;

    public DelegateTreeSelectView(Context context, int normalColor, int selectColor) {
        super(context);
        this.normalColor = normalColor;
        this.selectColor = selectColor;
        trees = new ArrayList<>();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (trees.size() > position) {
            displayList(trees.get(position), false);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public ViewGroup createParentView() {
        ViewGroup viewGroup = super.createParentView();

        tabLayout = new TabLayout(context);
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setSelectedTabIndicatorColor(selectColor);
        tabLayout.setTabTextColors(normalColor, selectColor);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ViewGroup.LayoutParams lp = getTabLayoutLayoutParams();
        setTopMargin(lp.height);
        viewGroup.addView(tabLayout, lp);
        return viewGroup;
    }

    @Override
    public void displayList(ArrayList<T> list, boolean isLastPage) {
        if (list != null) {
            trees.add(list);
        }
        super.displayList(list, isLastPage);
    }

    /**
     * 添加一个tab
     */
    public void addTag(String title) {
        tabLayout.addTab(tabLayout.newTab().setText(title), true);
    }

    /**
     * 刷新树结构
     */
    public void refreshTree() {
        int currentPosition = tabLayout.getSelectedTabPosition();
        int tabCount = tabLayout.getTabCount();
        if (tabCount > currentPosition + 1) {
            for (int i = tabCount - 1; i > currentPosition; i--) {
                tabLayout.removeTabAt(i);
                trees.remove(i);
            }
        }
        reloadData();
    }

    /**
     * 获取当前分支完整路径
     */
    public StringBuffer getTreePath(String separation) {
        StringBuffer tree = new StringBuffer();
        int count = tabLayout.getTabCount();
        for (int i = 1; i < count; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (i > 1) {
                tree.append(separation);
            }
            tree.append(tab.getText());
        }
        return tree;
    }

    protected ViewGroup.LayoutParams getTabLayoutLayoutParams() {
        int tabLayoutHeight = context.getResources().getDimensionPixelOffset(R.dimen.button_height);
        return new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tabLayoutHeight);
    }
}
