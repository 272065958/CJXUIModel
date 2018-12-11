package com.model.cjx.base.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by cjx on 17-12-29.
 * recyclerView 的设配器基类
 */
abstract class BaseRecyclerAdapter<T>(var list: ArrayList<T>?, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * 当前列表长度
     */
    protected open var count: Int = 0

    val inflater: LayoutInflater = LayoutInflater.from(context)

    init {
        initCount(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = bindViewHolder(createView(parent, inflater, viewType), viewType)

    override fun getItemCount(): Int = count

    /**
     * 刷新列表
     */
    fun notifyDataSetChanged(list: ArrayList<T>?) {
        this@BaseRecyclerAdapter.list = list
        initCount(list)
        notifyDataSetChanged()
    }

    /**
     * 计算当前列表数量
     */
    fun initCount(list: ArrayList<T>?) {
        count = list?.size ?: 0
    }

    /**
     * 移除指定位置的一个item
     */
    fun itemRemoved(position: Int) {
        try {
            list?.removeAt(position)
            itemRemovedWidthList(position, list!!)
        }catch (e: IndexOutOfBoundsException){

        }
    }

    /**
     * 重新计算数据长度, 刷新界面
     */
    fun itemRemovedWidthList(position: Int, list: ArrayList<T>) {
        initCount(list)
        notifyItemRemoved(position)
    }

    /**
     * 创建一个itemview
     */
    abstract fun createView(viewGroup: ViewGroup?, inflater: LayoutInflater, viewType: Int): View

    /**
     * 绑定数据到view上
     */
    abstract fun bindViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

}