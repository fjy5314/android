package com.daniulive.smartpublisher.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup


/**
 * Created by flny on 2018/1/24.
 */
abstract class BaseRecyclerAdapter<M> : RecyclerView.Adapter<BaseHolder> {

    protected var mHeaderView: View? = null
    protected var mFooterView: View? = null

    private var dataList: MutableList<M>? = null

    private var mContext: Context? = null
    private var onItemClickListener: OnItemClickListener? = null

    private val extraHeaderFooterItemCount: Int
        get() {
            var extraCount = dataList!!.size
            if (mHeaderView != null) {
                extraCount++
            }
            if (mFooterView != null) {
                extraCount++
            }
            return extraCount
        }


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onHeaderClick()
        fun onFooterClick()
    }

    constructor(context: Context) {
        mContext = context
        this.dataList = ArrayList()
    }

    constructor(context: Context, dataList: List<M>) {
        mContext = context
        this.dataList = ArrayList()
        this.dataList!!.addAll(dataList)
    }

    fun fillDataList(datas: List<M>) {
        dataList!!.clear()
        if (dataList!!.addAll(datas)) {
            notifyDataSetChanged()
            //            notifyItemInserted(getExtraHeaderFooterItemCount());
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun getmContext(): Context? {
        return mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return when (viewType) {
            VIEW_HEADER -> BaseHolder(mHeaderView!!)
            VIEW_FOOTER -> BaseHolder(mFooterView!!)
            else -> createCustomViewHolder(parent, viewType)
        }
    }

    abstract fun createCustomViewHolder(parent: ViewGroup, viewType: Int): BaseHolder

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (getItemViewType(position) == VIEW_HEADER || getItemViewType(position) == VIEW_FOOTER) {
            return
        }
        bindCustomViewHolder(holder, position)
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener { v -> onItemClickListener!!.onItemClick(v, position) }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView!!.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == VIEW_HEADER || getItemViewType(position) == VIEW_FOOTER)
                        manager.spanCount
                    else
                        1
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: BaseHolder?) {
        super.onViewAttachedToWindow(holder)
        val lp = holder!!.itemView.layoutParams
        if (lp != null
                && lp is StaggeredGridLayoutManager.LayoutParams
                && holder.layoutPosition == 0) {
            lp.isFullSpan = true
        }
    }

    abstract fun bindCustomViewHolder(holder: BaseHolder, position: Int)

    override fun getItemCount(): Int {
        var count = dataList!!.size
        if (mHeaderView != null)
            count++
        if (mFooterView != null)
            count++
        return count
    }

    protected abstract fun getCustomViewType(position: Int): Int

    fun getItem(position: Int): M? {
        if (mHeaderView != null && position == 0 || position >= extraHeaderFooterItemCount) {
            return null
        }
        return if (mHeaderView == null) dataList!![position] else dataList!![position - 1]
    }

    override fun getItemViewType(position: Int): Int {
        return if (mHeaderView != null && position == 0) {
            VIEW_HEADER
        } else if (mFooterView != null && position == extraHeaderFooterItemCount - 1) {
            VIEW_FOOTER
        } else {
            getCustomViewType(position)
        }
    }

    fun isHeader(position: Int): Boolean {
        return hasHeader() && position == 0
    }

    fun hasHeader(): Boolean {
        return mHeaderView != null
    }


    fun setHeaderView(headerView: View) {
        this.mHeaderView = headerView
        notifyItemInserted(0)
    }

    fun setFooterView(footerView: View) {
        this.mFooterView = footerView
        notifyDataSetChanged()
    }

    fun removeHeaderView() {
        if (mHeaderView != null) {
            mHeaderView = null
            notifyDataSetChanged()
        }
    }

    fun removeFooterView() {
        if (mFooterView != null) {
            mFooterView = null
            notifyDataSetChanged()
        }
    }

    fun moveToPosition() {

    }

    companion object {

        val VIEW_HEADER = 1023
        val VIEW_FOOTER = 1024
    }
}
