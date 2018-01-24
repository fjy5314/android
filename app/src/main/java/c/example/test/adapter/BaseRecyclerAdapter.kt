package c.example.test.adapter

import android.content.Context
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * Created by flny on 2018/1/24.
 */
abstract class BaseRecyclerAdapter<M, VH : BaseHolder> : RecyclerView.Adapter<BaseHolder> {

    protected var headerViewxxx: View? = null
    protected var footerViewxxx: View? = null

    private var dataList: MutableList<M>? = null

    private var mContext: Context? = null
    protected var onItemClickListenerxxxx: OnItemClickListener? = null

    val extraHeaderFooterItemCount: Int
        get() {
            var extraCount = dataList!!.size
            if (headerViewxxx != null) {
                extraCount++
            }
            if (footerViewxxx != null) {
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
        this.onItemClickListenerxxxx = onItemClickListener
    }

    fun getmContext(): Context? {
        return mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return if (viewType == VIEW_HEADER) {
            BaseHolder(headerViewxxx!!)
        } else if (viewType == VIEW_FOOTER) {
            BaseHolder(footerViewxxx!!)
        } else {
            createCustomViewHolder(parent, viewType)
        }
    }

    abstract fun createCustomViewHolder(parent: ViewGroup, viewType: Int): VH

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (getItemViewType(position) == VIEW_HEADER || getItemViewType(position) == VIEW_FOOTER) {
            return
        }
        val itemType = holder.itemViewType
        when (itemType) {
            0-> return
            else -> {
                bindCustomViewHolder(holder as VH, position)
                if (onItemClickListenerxxxx != null) {
                    holder.itemView.setOnClickListener { v -> onItemClickListenerxxxx!!.onItemClick(v, position) }
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView!!.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == BaseRecyclerAdapter.VIEW_HEADER || getItemViewType(position) == BaseRecyclerAdapter.VIEW_FOOTER)
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

    abstract fun bindCustomViewHolder(holder: VH, position: Int)

    override fun getItemCount(): Int {
        var count = dataList!!.size
        if (headerViewxxx != null)
            count++
        if (footerViewxxx != null)
            count++
        return count
    }

    protected abstract fun getCustomViewType(position: Int): Int

    fun getItem(position: Int): M? {
        if (headerViewxxx != null && position == 0 || position >= extraHeaderFooterItemCount) {
            return null
        }
        return if (headerViewxxx == null) dataList!![position] else dataList!![position - 1]
    }

    override fun getItemViewType(position: Int): Int {
        return if (headerViewxxx != null && position == 0) {
            VIEW_HEADER
        } else if (footerViewxxx != null && position == extraHeaderFooterItemCount - 1) {
            VIEW_FOOTER
        } else {
            getCustomViewType(position)
        }
    }

    fun isHeader(position: Int): Boolean {
        return hasHeader() && position == 0
    }

    fun hasHeader(): Boolean {
        return headerViewxxx != null
    }


    fun setHeaderView(headerView: View) {
        this.headerViewxxx = headerView
        notifyItemInserted(0)
    }

    fun setFooterView(footerView: View) {
        this.footerViewxxx = footerView
        notifyDataSetChanged()
    }

    fun removeHeaderView() {
        if (headerViewxxx != null) {
            headerViewxxx = null
            notifyDataSetChanged()
        }
    }

    fun removeFooterView() {
        if (footerViewxxx != null) {
            footerViewxxx = null
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
