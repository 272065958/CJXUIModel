package com.model.cjx.component;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.model.cjx.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * create by cjx on 2018/12/18
 */
public class AdverPlayView<VH extends AdverPlayView.BaseAdBean> extends FrameLayout implements View.OnClickListener {

    private int playTime = 5;
    /**
     * 翻页控件
     */
    private MyViewPager viewPager;
    /**
     * 翻页控件适配器
     */
    private AdRepeatAdapter viewAdapter;
    /**
     * 页码指示控件
     */
    private PagePointView pointView;
    /**
     * 翻页回调
     */
    private AdRepeatListener pageListener;
    private OnAdActionListener adListener;


    private ImageView firstView; // 显示第一张图片的容器
    private ImageView lastView;  // 显示最后一张图片的容器
    /**
     * 翻页控件的view数组
     */
    private ArrayList<ImageView> views;

    /**
     * 自动翻页的计时器
     */
    private Timer timer;
    private MyTimerTask timerTask;

    public AdverPlayView(Context context) {
        super(context);
    }

    public AdverPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {

    }

    public void setDataSource(ArrayList<VH> list, int selectColor, int normalColor) {
        int count = list == null ? 0 : list.size();
        if (count == 0) {
            return;
        }
        if (viewPager == null) {
            setupView(getContext());
        }
        int pointSize = getContext().getResources().getDimensionPixelSize(R.dimen.auto_margin);
        pointView.initPoint(pointSize, pointSize, count, selectColor, normalColor);

        if (count > 1) {
            VH lastBean = list.get(count - 1);
            VH firstBean = list.get(0);
            list.add(0, lastBean);
            list.add(firstBean);

            if (firstView == null) {
                firstView = createImageView();
            }
        }

        initViews(count);

        if (viewAdapter == null) {
            viewAdapter = new AdRepeatAdapter(getContext(), list, this.views, firstView, lastView);
            viewPager.setAdapter(viewAdapter);
            // 翻页监听
            pageListener = new AdRepeatListener(viewPager, pointView, list.size());
            viewPager.addOnPageChangeListener(pageListener);
        } else {
            viewAdapter.notifyDataSetChanged(list, this.views, firstView, lastView);
            pageListener.updateCount(list.size());
        }

        if (count == 1) {
            viewPager.setCurrentItem(0);
            // 停止自动滚动计时
            stopScroll();
        } else {
            viewPager.setCurrentItem(1);
            // 开始自动滚动计时
            startScroll();
        }
        pointView.setPosition(0);
    }

    public void setOnAdActionListener(OnAdActionListener listener) {
        adListener = listener;
    }

    /**
     * 开始自动滚动
     */
    public void startScroll() {
        if (timer != null) {
            stopScroll();
        }
        timer = new Timer(true);
        timerTask = new MyTimerTask(viewPager);
        timer.schedule(timerTask, 0, 1000);
    }

    public void stopScroll() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.isPause = true;
            timerTask.cancel();
            timerTask = null;
        }
    }

    private void setupView(Context context) {
        viewPager = new MyViewPager(context);
        pointView = new PagePointView(context);

        addView(viewPager, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        lp.bottomMargin = context.getResources().getDimensionPixelOffset(R.dimen.auto_margin);
        addView(pointView, lp);

        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (timerTask != null) {
                            timerTask.isPause = true;
                        }
                        break;
                    default:
                        if (timerTask != null) {
                            timerTask.isPause = false;
                        }
                        break;
                }
                return false;
            }
        });
        viewPager.setTag(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });
    }

    /**
     * 创建一个广告view
     */
    private ImageView createImageView() {
        ImageView view = new ImageView(getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setBackgroundColor(Color.WHITE);
        view.setOnClickListener(this);
        return view;
    }

    /**
     * 初始化缓存view数组
     */
    private void initViews(int size) {
        // 当只有2个广告时, 只需额外两个缓存view, 大于2个广告时, 需要三个缓存view
        int viewCount = size > 2 ? 3 : size;

        if (views == null) {
            views = new ArrayList<>(viewCount);
        }
        for (int i = 0; i < viewCount; i++) {
            views.add(createImageView());
        }
    }

    private class MyViewPager extends ViewPager {

        private float mDownMotionX = 0f;
        private float mDownMotionY = 0f;

        MyViewPager(Context context) {
            super(context);
        }

        @Override
        public boolean performClick() {
            return super.performClick();
        }

        /**
         * 拦截父类触摸， 预防广告的左右滚动和父类上下滚动混乱冲突
         */
        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownMotionX = ev.getX();
                    mDownMotionY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float xDiff = Math.abs(ev.getX() - mDownMotionX);
                    float yDiff = Math.abs(ev.getY() - mDownMotionY);
                    if (xDiff > yDiff) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    super.performClick();
                    break;
            }
            return super.onTouchEvent(ev);
        }
    }

    private class MyTimerTask extends TimerTask {

        private ViewPager viewPager;
        int second = 0;
        boolean isPause;

        MyTimerTask(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        @Override
        public void run() {
            while (isPause) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            second++;
            if (second == playTime) {
                second = 0;
                viewPager.post((Runnable) viewPager.getTag());
            }
        }
    }

    /**
     * 循环思想: 用3个view实现无限循环
     * 为性能考虑, 此处因为多定义了第一个位置的view和最后一个位置的view 所以最多有5个view同时存在
     * 当显示第一个或最后一个广告时, 始终用第一个位置的view和最后一个位置的view来显示
     * (此方式不适用于当原广告长度只有2个时的情况)
     * 当原广告只有两页的时候,情况特殊处理, 只需要两个循环用的view和第一位置view和最后一个位置view就可以实现循环
     */
    private class AdRepeatAdapter extends PagerAdapter {
        private ArrayList<ImageView> views;
        private ImageView firstView;
        private ImageView lastView;

        private ArrayList<VH> list; // 原本列表头插入最后一个元素, 列表尾插入第一个元素后的新列表
        private int size = 0; // 原本列表基础上加上多2

        AdRepeatAdapter(Context context, ArrayList<VH> list, ArrayList<ImageView> views, ImageView firstView, ImageView lastView) {
            init(list, views, firstView, lastView);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view;
            if (size == 1) {
                view = lastView;
            } else if (size == 4) {
                if (position == 0) {
                    view = firstView;
                } else if (position == 3) {
                    view = lastView;
                } else {
                    view = views.get(position - 1);
                }
            } else {
                if (position == 0 || position == size - 2) {
                    view = firstView;
                } else if (position == 1 || position == size - 1) {
                    view = lastView;
                } else {
                    view = views.get((position - 2) % 3);
                }
            }
            // 添加前先从父类剥离出来, 这套实现方式的剥离代码不放在desroyItem中, 因为在滑到最后一个和第一个时, 重复利用会出问题
            container.removeView(view);
            container.addView(view);
            VH bean = list.get(position);
            if (adListener != null) {
                adListener.onImageLoad(view, bean);
            }
            return view;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public int getCount() {
            return size;
        }

        void notifyDataSetChanged(ArrayList<VH> list, ArrayList<ImageView> views, ImageView firstView, ImageView lastView) {
            init(list, views, firstView, lastView);
            super.notifyDataSetChanged();
        }

        private void init(ArrayList<VH> list, ArrayList<ImageView> views, ImageView firstView, ImageView lastView) {
            this.views = views;
            this.list = list;
            this.firstView = firstView;
            this.lastView = lastView;
            size = list == null ? 0 : list.size();
        }
    }

    private class AdRepeatListener implements ViewPager.OnPageChangeListener {

        private int pageCount;
        private ViewPager viewPager;
        private PagePointView pointView;

        public AdRepeatListener(ViewPager viewPager, PagePointView pointView, int count) {
            this.viewPager = viewPager;
            this.pointView = pointView;
            this.pageCount = count;
        }

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            int index;
            if (i == 0) {
                index = pageCount - 3;
            } else if (i == pageCount - 1) {
                index = 0;
            } else {
                index = i - 1;
            }
            pointView.setPosition(index);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem == pageCount - 1) {
                    viewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    viewPager.setCurrentItem(pageCount - 2, false);
                }
            }
        }

        void updateCount(int count) {
            pageCount = count;
        }
    }

    public abstract class BaseAdBean {
        abstract String getImage();
    }

    public interface OnAdActionListener {
        void onClick(AdverPlayView.BaseAdBean ad);

        void onImageLoad(ImageView imageView, AdverPlayView.BaseAdBean ad);
    }
}
