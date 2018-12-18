package com.model.cjx.base.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.model.cjx.BaseApplication;
import com.model.cjx.R;
import com.model.cjx.delegate.DelegateMainView;

/**
 * create by cjx on 2018/12/13
 */
public abstract class BaseActivity extends AppCompatActivity {

    private DelegateMainView delegateMainView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        delegateMainView = new DelegateMainView();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        setStatusBar();
        // 设置沉浸式属性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setFitsSystemWindows(true);
        }
        super.setContentView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        setStatusBar();
        super.setContentView(layoutResID);
    }

    /**
     * 设置toolbar
     *
     * @param title    标题的资源id
     * @param showBack 是否显示返回按钮
     * @param listener 返回按钮监听
     */
    protected void setToolbar(String title, boolean showBack, View.OnClickListener listener) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            delegateMainView.setToolbar(this, toolbar, title, showBack, listener);
        }
    }

    /**
     * 显示加载对话框
     *
     * @param listener 取消对话框时的回调
     */
    protected void showLoadDialog(DialogInterface.OnCancelListener listener) {
        delegateMainView.showLoadDialog(this, listener);
    }

    /**
     * 隐藏加载对话框
     */
    protected void dismissLoadDialog() {
        delegateMainView.dismissLoadDialog();
    }

    /**
     * 提示消息
     *
     * @param message 提示的信息
     */
    public void showToast(String message) {
        delegateMainView.showToast(message, this);
    }

    /**
     * 开启一个fragment界面, 不带动画效果
     *
     * @param id           显示的控件id
     * @param tagFragment  要显示的fragment
     * @param hideFragment 要隐藏的fragment
     * @param backStack    添加返回功能的标签
     */
    protected void startFragmentWithoutAnim(int id, Fragment tagFragment, Fragment hideFragment, String backStack) {
        delegateMainView.startFragmentWithoutAnim(getSupportFragmentManager(), id, tagFragment, hideFragment, backStack);
    }

    /**
     * 开启一个fragment界面, 自带动画效果
     *
     * @param id           显示的控件id
     * @param tagFragment  要显示的fragment
     * @param hideFragment 要隐藏的fragment
     * @param backStack    添加返回功能的标签
     */
    protected void startFragment(int id, Fragment tagFragment, Fragment hideFragment, String backStack) {
        delegateMainView.startFragment(getSupportFragmentManager(), id, tagFragment, hideFragment, backStack);
    }

    /**
     * 重置指定id上的fragment
     *
     * @param id          显示的控件id
     * @param tagFragment 要显示的fragment
     */
    protected void replaceFragment(int id, Fragment tagFragment) {
        delegateMainView.replaceFragment(getSupportFragmentManager(), id, tagFragment);
    }

    /**
     * 设置顶部状态栏主题
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getApplication() instanceof BaseApplication ?
                    ((BaseApplication) getApplication()).getStatusBarColor() : Color.BLACK
            );
        }
    }
}
