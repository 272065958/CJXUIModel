package com.model.cjx.component

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.model.cjx.R
import java.util.*

/**
 * Created by cjx on 18-3-16.
 *
 */

class AdPlayView<VH : AdPlayView.BaseAdBean>(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs), View.OnClickListener {

    /**
     * 翻页控件
     */
    private var viewPager: MyViewPager
    /**
     * 翻页控件适配器
     */
    private var viewAdapter: AdRepeatAdapter<VH>? = null
    /**
     * 页码指示控件
     */
    private var pointView: PagePointView
    /**
     * 翻页回调
     */
    private var pageListener: AdRepeatListener? = null
    private var adListener: OnAdActionListener? = null


    private var firstView: ImageView? = null // 显示第一张图片的容器
    private var lastView: ImageView // 显示最后一张图片的容器
    /**
     * 翻页控件的view数组
     */
    private var views: ArrayList<ImageView>? = null

    /**
     * 自动翻页的计时器
     */
    private var timer: Timer? = null
    private var timerTask: MyTimerTask? = null

    init {
        viewPager = MyViewPager(context)
        pointView = PagePointView(context)
        var lp = LayoutParams(0, 0)
        lp.leftToLeft = LayoutParams.PARENT_ID
        lp.rightToRight = LayoutParams.PARENT_ID
        lp.bottomToBottom = LayoutParams.PARENT_ID
        lp.topToTop = LayoutParams.PARENT_ID

        viewPager.layoutParams = lp

        lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.leftToLeft = LayoutParams.PARENT_ID
        lp.rightToRight = LayoutParams.PARENT_ID
        lp.bottomToBottom = LayoutParams.PARENT_ID
        lp.bottomMargin = resources.getDimensionPixelOffset(R.dimen.auto_margin)
        pointView.layoutParams = lp

        lastView = createImageView()
    }

    override fun onClick(v: View?) {

    }

    /**
     * 设置翻页的数据源
     */
    fun setDataSource(list: ArrayList<VH>?, selectColor: Int, unSelectColor: Int = Color.DKGRAY) {
        val count = list?.size ?: 0
        if (count == 0) {
            // 清空广告 关闭计时器
            return
        }

        // 第一次调用, 需要添加翻页控件到当前容器上
        if (viewPager.parent == null) {
            addView(viewPager)
            viewPager.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        setPause(true)
                    }
                    MotionEvent.ACTION_UP -> {
                        setPause(false)
                    }
                    else -> {
                        setPause(false)
                    }
                }
                false
            }
            viewPager.tag = Runnable {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            }
            addView(pointView)

        }

        // 重画当前页码
        val pointSize = resources.getDimensionPixelSize(R.dimen.auto_margin)
        pointView.initPoint(pointSize, pointSize, count, selectColor, unSelectColor)

        if (count > 1) {
            val lastBean = list!![count - 1]
            val firstBean = list[0]
            list.add(0, lastBean)
            list.add(firstBean)

            if (firstView == null) {
                firstView = createImageView()
            }
        }

        initViews(count)

        if (viewAdapter == null) {
            viewAdapter = AdRepeatAdapter(context, list, this.views!!, firstView, lastView)
            viewPager.adapter = viewAdapter
            // 翻页监听
            pageListener = AdRepeatListener(viewPager, pointView, list!!.size)
            viewPager.addOnPageChangeListener(pageListener!!)
        } else {
            viewAdapter!!.notifyDataSetChanged(list, this.views!!, firstView, lastView)
            pageListener!!.updateCount(list!!.size)
        }

        if (count == 1) {
            viewPager.currentItem = 0
            // 停止自动滚动计时
            stopScroll()
        } else {
            viewPager.currentItem = 1
            // 开始自动滚动计时
            startScroll()
        }
        pointView.setPosition(0)
    }

    /**
     * 开始自动滚动
     */
    fun startScroll() {
        if (timer != null) {
            stopScroll()
        }
        timer = Timer(true)
        timerTask = MyTimerTask(viewPager)
        timer!!.schedule(timerTask, 0, 1000)
    }

    fun stopScroll() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        if (timerTask != null) {
            timerTask?.isPause = false
            timerTask?.cancel()
            timerTask = null
        }
    }

    fun setOnAdActionListener(listener: OnAdActionListener) {
        adListener = listener
    }

    /**
     * 初始化缓存view数组
     */
    private fun initViews(size: Int) {
        // 当只有2个广告时, 只需额外两个缓存view, 大于2个广告时, 需要三个缓存view
        val viewCount = when (size) {
            1 -> 1
            2 -> 2
            else -> 3
        }

        if (views == null) {
            views = ArrayList()
        }
        val viewArray = views!!
        val viewsSize = viewArray.size
        when (viewsSize) {
            0 -> {
                for (i in 0 until viewCount) {
                    viewArray.add(createImageView())
                }
            }
            2 -> {
                if (viewCount == 3) {
                    viewArray.add(createImageView())
                }
            }
            3 -> {
                if (viewCount == 2) {
                    viewArray.removeAt(2)
                }
            }
        }
    }

    /**
     * 创建一个广告view
     */
    private fun createImageView(): ImageView {
        val view = ImageView(context)
        view.scaleType = ImageView.ScaleType.CENTER_CROP
        view.setBackgroundColor(Color.WHITE)
        view.setOnClickListener(this)
        return view
    }

    /**
     * 自动滚动暂停开关
     */
    private fun setPause(isPause: Boolean) {
        timerTask?.initPause(isPause)
    }

    private class MyViewPager(context: Context) : ViewPager(context) {

        private var mDownMotionX = 0f
        private var mDownMotionY = 0f

        override fun performClick(): Boolean {
            return super.performClick()
        }

        /**
         * 拦截父类触摸， 预防广告的左右滚动和父类上下滚动混乱冲突
         */
        override fun onTouchEvent(ev: MotionEvent): Boolean {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDownMotionX = ev.x
                    mDownMotionY = ev.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val xDiff = Math.abs(ev.x - mDownMotionX)
                    val yDiff = Math.abs(ev.y - mDownMotionY)
                    if (xDiff > yDiff) {
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
                MotionEvent.ACTION_UP -> super.performClick()
            }
            return super.onTouchEvent(ev)
        }

    }

    private class MyTimerTask(val viewPager: ViewPager) : TimerTask() {

        var second = 0
        var isPause = false

        fun initPause(p: Boolean) {
            isPause = p
            second = 0
        }

        override fun run() {
            while (isPause) {
                Thread.sleep(1000)
            }
            second++
            if (second == 5) {
                second = 0
                viewPager.post(viewPager.tag as? Runnable ?: return)
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
    private inner class AdRepeatAdapter<VH : BaseAdBean>(val context: Context, list: ArrayList<VH>?, views: ArrayList<ImageView>, firstView: ImageView?, lastView: ImageView) : PagerAdapter() {
        private var views: ArrayList<ImageView>? = null
        private var firstView: ImageView? = null
        private var lastView: ImageView? = null

        private var list: ArrayList<VH>? = null // 原本列表头插入最后一个元素, 列表尾插入第一个元素后的新列表
        private var size = 0 // 原本列表基础上加上多2

        init {
            init(list, views, firstView, lastView)
        }

        private fun init(list: ArrayList<VH>?, views: ArrayList<ImageView>, firstView: ImageView?, lastView: ImageView) {
            this.views = views
            this.list = list
            this.firstView = firstView
            this.lastView = lastView
            size = list?.size ?: 0
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = when (size) {
                1 -> lastView
                4 -> when (position) {
                    0 -> firstView
                    3 -> lastView
                    else -> views!![position - 1]
                }
                else -> when (position) {
                    0, size - 2 -> firstView
                    1, size - 1 -> lastView
                    else -> views!![(position - 2) % 3]
                }
            }
            // 添加前先从父类剥离出来, 这套实现方式的剥离代码不放在desroyItem中, 因为在滑到最后一个和第一个时, 重复利用会出问题
            container.removeView(view)
            container.addView(view)
            val bean = list?.get(position)
            if (adListener != null) {
                adListener!!.onImageLoad(view!!, bean!!)
            }
            return view!!
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = size

        fun notifyDataSetChanged(list: ArrayList<VH>?, views: ArrayList<ImageView>, firstView: ImageView?, lastView: ImageView) {
            init(list, views, firstView, lastView)
            super.notifyDataSetChanged()
        }

    }

    private class AdRepeatListener(private val viewPager: ViewPager, private val pointView: PagePointView, count: Int) : ViewPager.OnPageChangeListener {

        private var pageCount = 0

        init {
            pageCount = count
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_IDLE) { // 动画停止
                if (viewPager.currentItem == pageCount - 1) { // 停在最后一个, 就将界面刷到第二个
                    viewPager.setCurrentItem(1, false)
                } else if (viewPager.currentItem == 0) { // 停在第一个, 就刷到倒数第二个
                    viewPager.setCurrentItem(pageCount - 2, false)
                }
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

        override fun onPageSelected(position: Int) {
            val index = when (position) {
                0 -> {
                    pageCount - 3
                }
                pageCount - 1 -> {
                    0
                }
                else -> {
                    position - 1
                }
            }
            pointView.setPosition(index)
        }

        fun updateCount(count: Int) {
            pageCount = count
        }
    }

    abstract class BaseAdBean {
        abstract fun getImage(): String?
    }

    interface OnAdActionListener {
        fun onClick(ad: BaseAdBean)
        fun onImageLoad(imageView: ImageView, ad: BaseAdBean)
    }
}