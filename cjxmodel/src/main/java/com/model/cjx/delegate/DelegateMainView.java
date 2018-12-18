package com.model.cjx.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.model.cjx.BaseApplication;
import com.model.cjx.R;
import com.model.cjx.dialog.LoadDialog;

/**
 * create by cjx on 2018/12/13
 */
public class DelegateMainView {
    /**
     * 提交数据的提示对话框
     */
    private LoadDialog loadDialog;

    private int toolbarBg = 0;
    private int backRes = 0;

    public DelegateMainView() {
    }

    /**
     * 设置标题栏的背景色和返回按钮
     * @param toolbarBg 标题栏背景id
     * @param backRes   返回按钮id
     */
    public void setToolbarBg(int toolbarBg, int backRes) {
        this.toolbarBg = toolbarBg;
        this.backRes = backRes;
    }

    /**
     * 设置toolbar
     *
     * @param showBack     是否显示返回按钮
     * @param backListener 返回按钮监听
     * @param title        标题的资源
     */
    public void setToolbar(final AppCompatActivity activity, Toolbar toolbar, String title,
                              boolean showBack, View.OnClickListener backListener) {
        if (backRes == 0 && toolbarBg == 0) {
            if (activity.getApplication() instanceof BaseApplication) {
                backRes = BaseApplication.instance.getBackRes();
                toolbarBg = BaseApplication.instance.getToolbarBg();
            } else {
                backRes = R.drawable.white_back;
                toolbarBg = ContextCompat.getColor(activity, R.color.cjx_colorPrimary);
            }
        }

        toolbar.setBackgroundResource(toolbarBg);
        toolbar.setTitle(title);
        // 设置返回按钮
        if (showBack) {
            toolbar.setNavigationIcon(backRes);
            activity.setSupportActionBar(toolbar);
            if (backListener == null) {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            } else {
                toolbar.setNavigationOnClickListener(backListener);
            }
        } else {
            activity.setSupportActionBar(toolbar);
        }
    }

    /**
     * 显示加载对话框
     */
    public void showLoadDialog(Context context, DialogInterface.OnCancelListener listener) {
        if (loadDialog == null) {
            loadDialog = new LoadDialog(context, null);
        } else if (((Activity) loadDialog.getContext()).isFinishing()) { // 可能当前fragment的上下文已经被销毁
            loadDialog = new LoadDialog(context, null);
        }
        if (listener != null) {
            loadDialog.setOnCancelListener(listener);
        }
        try {
            loadDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissLoadDialog() {
        if (loadDialog != null && loadDialog.isShowing()) {
            loadDialog.dismiss();
        }
    }

    /**
     * 显示提示信息
     */
    public void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 开启一个fragment界面, 不带动画效果
     */
    public void startFragmentWithoutAnim(FragmentManager manager, int id, Fragment tagFragment, Fragment hideFragment, String backStack) {
        FragmentTransaction beginTransaction = manager.beginTransaction();
        fragmentTransaction(beginTransaction, id, tagFragment, hideFragment, backStack);
    }

    /**
     * 开启一个fragment界面
     */
    public void startFragment(FragmentManager manager, int id, Fragment tagFragment, Fragment hideFragment, String backStack) {
        FragmentTransaction beginTransaction = manager.beginTransaction();
        beginTransaction.setCustomAnimations(
                R.anim.open_slide_in, R.anim.open_slide_out,
                R.anim.close_slide_in, R.anim.close_slide_out
        );
        fragmentTransaction(beginTransaction, id, tagFragment, hideFragment, backStack);
    }

    /**
     * 重置指定id上的fragment
     */
    public void replaceFragment(FragmentManager manager, int id, Fragment fragment) {
        manager.beginTransaction().replace(id, fragment).commit();
    }

    /**
     * 提交切换fragment的事物
     * hideFragment为null时, 可能在下次切换的时候可以看到当前fragment的view, 因为切换动画是透明渐变
     */
    private void fragmentTransaction(FragmentTransaction beginTransaction, int id, Fragment tagFragment, Fragment hideFragment, String backStack) {
        if (hideFragment != null) {
            beginTransaction.hide(hideFragment);
        }
        if (tagFragment.isAdded()) {
            beginTransaction.show(tagFragment);
        } else {
            beginTransaction.add(id, tagFragment);
        }
        if (backStack != null) {
            // 类似startActivity
            beginTransaction.addToBackStack(backStack);
        }

        beginTransaction.commit();
    }

}
