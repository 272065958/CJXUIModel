package com.model.cjx.base.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.model.cjx.R;
import com.model.cjx.delegate.DelegateMainView;

/**
 * create by cjx on 2018/12/14
 */
public abstract class BaseFragment extends Fragment {
    private DelegateMainView delegateMainView;
    private View contentView;
    private Toolbar toolbar;
    Fragment resultFragment;
    int requestCode;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        delegateMainView = new DelegateMainView();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = createView(inflater, container);
        return contentView;
    }

    /**
     * 设置toolbar
     *
     * @param title    标题的资源id
     * @param showBack 是否显示返回按钮
     * @param listener 返回按钮监听
     */
    protected void setToolbar(String title, boolean showBack, View.OnClickListener listener) {
        toolbar = contentView.findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        if (getActivity() instanceof AppCompatActivity) {
            delegateMainView.setToolbar((AppCompatActivity) getActivity(), toolbar, title, showBack, listener);
        }
    }

    /**
     * 创建工具栏选项
     *
     * @param menu   工具栏
     * @param menuId 选项资源id
     */
    protected void createMenu(Menu menu, int menuId) {
        if (toolbar == null) {
            return;
        }
        toolbar.getMenu().clear();
        toolbar.inflateMenu(menuId);
    }

    /**
     * 显示加载对话框
     *
     * @param listener 取消对话框时的回调
     */
    protected void showLoadDialog(DialogInterface.OnCancelListener listener) {
        delegateMainView.showLoadDialog(getContext(), listener);
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
    protected void showToast(String message) {
        delegateMainView.showToast(message, getContext());
    }

    /**
     * 开启一个同级fragment界面, 不带动画效果
     *
     * @param id           当前显示fragment界面上的id
     * @param tagFragment  要显示的fragment
     * @param hideFragment 要隐藏的fragment
     * @param backStack    添加返回功能的标签
     */
    protected void startFragmentWithoutAnim(int id, Fragment tagFragment, Fragment hideFragment, String backStack) {
        AppCompatActivity activity = getAppCompatActivity();
        if (activity == null) {
            return;
        }
        delegateMainView.startFragmentWithoutAnim(activity.getSupportFragmentManager(), id, tagFragment, hideFragment, backStack);
    }

    /**
     * 开启一个同级fragment界面, 自带动画效果
     *
     * @param id           当前显示fragment界面上的id
     * @param tagFragment  要显示的fragment
     * @param hideFragment 要隐藏的fragment
     * @param backStack    添加返回功能的标签
     */
    protected void startFragment(int id, Fragment tagFragment, Fragment hideFragment, String backStack) {
        AppCompatActivity activity = getAppCompatActivity();
        if (activity == null) {
            return;
        }
        delegateMainView.startFragment(activity.getSupportFragmentManager(), id, tagFragment, hideFragment, backStack);
    }

    /**
     * 重置指定id上的同级fragment
     *
     * @param id          当前显示fragment界面上的id
     * @param tagFragment 要显示的fragment
     */
    protected void replaceFragment(int id, Fragment tagFragment) {
        AppCompatActivity activity = getAppCompatActivity();
        if (activity == null) {
            return;
        }
        delegateMainView.replaceFragment(activity.getSupportFragmentManager(), id, tagFragment);
    }

    /**
     * 开启一个子的fragment界面, 不带动画效果
     *
     * @param id           当前fragment界面上的id
     * @param tagFragment  要显示的fragment
     * @param hideFragment 要隐藏的fragment
     * @param backStack    添加返回功能的标签
     */
    protected void startCustomFragmentWithoutAnim(int id, Fragment tagFragment, Fragment hideFragment, String backStack) {
        delegateMainView.startFragmentWithoutAnim(getChildFragmentManager(), id, tagFragment, hideFragment, backStack);
    }

    /**
     * 开启一个子的fragment界面, 自带动画效果
     *
     * @param id           当前fragment界面上的id
     * @param tagFragment  要显示的fragment
     * @param hideFragment 要隐藏的fragment
     * @param backStack    添加返回功能的标签
     */
    protected void startCustomFragment(int id, Fragment tagFragment, Fragment hideFragment, String backStack) {
        delegateMainView.startFragment(getChildFragmentManager(), id, tagFragment, hideFragment, backStack);
    }

    /**
     * 重置指定id上的子的fragment
     *
     * @param id          当前fragment界面上的id
     * @param tagFragment 要显示的fragment
     */
    protected void replaceCustomFragment(int id, Fragment tagFragment) {
        delegateMainView.replaceFragment(getChildFragmentManager(), id, tagFragment);
    }

    /**
     * 关闭fragment
     *
     * @param backStack 要关闭的fragment标签
     */
    protected void finish(String backStack) {
        AppCompatActivity activity = getAppCompatActivity();
        if (activity == null) {
            return;
        }
        if (backStack != null) {
            activity.getSupportFragmentManager().popBackStack(backStack, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            activity.getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * 实现类似Activity的startActivityForResult的功能
     *
     * @param id           当前fragment界面上的id
     * @param tagFragment  要显示的fragment
     * @param hideFragment 要隐藏的fragment
     * @param requestCode  请求回调标识码
     * @param backStack    添加返回功能的标签
     */
    protected void startFragmentForResult(int id, BaseFragment tagFragment, BaseFragment hideFragment, int requestCode, String backStack) {
        tagFragment.resultFragment = this;
        tagFragment.requestCode = requestCode;
        startFragment(id, tagFragment, hideFragment, backStack);
    }

    private AppCompatActivity getAppCompatActivity() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing() && activity instanceof AppCompatActivity) {
            return (AppCompatActivity) activity;
        } else {
            return null;
        }
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup viewGroup);
}
