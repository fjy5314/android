package com.daniulive.smartpublisher.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import com.daniulive.smartpublisher.R

/**
 * Created by flny on 2018/2/8.
 */
class  MenuSimpleAdapter(var context: Context, var data: MutableList<out MutableMap<String, *>>?, var resource: Int, var  from: Array<out String>?, var to: IntArray?) : SimpleAdapter(context, data, resource, from, to){


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v: View
        if (convertView == null) {
            v = View.inflate(context,resource, null)
        } else {
            v = convertView
        }
        return super.getView(position, convertView, parent)
    }
}