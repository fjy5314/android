package com.daniulive.smartpublisher.adapter

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by flny on 2018/1/24.
 */
class BaseHolder : RecyclerView.ViewHolder {

    private var viewArray: SparseArray<View>

    //    private View itemView;
    constructor(itemView: View) : super(itemView) {
        viewArray = SparseArray()
        //        this.itemView=itemView;
    }

    constructor(parent: ViewGroup, @LayoutRes resId: Int) : super(LayoutInflater.from(parent.context).inflate(resId, parent, false)) {
        viewArray = SparseArray()
    }

    fun getView(@IdRes viewId: Int): View {
        var view: View? = viewArray.get(viewId)
        if (view == null) {
            view = itemView.findViewById<View>(viewId)
            viewArray.put(viewId, view)
        }
        return view!!
    }

    fun <M> refreshData(dataList: List<M>, position: Int) {

    }

}
