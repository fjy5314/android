package com.daniulive.smartpublisher.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import  com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.model.BaseCard

/**
 * Created by flny on 2018/1/19.
 */
class BaseCardAdapter(var list: ArrayList<BaseCard>, var context: Context,val intent: Intent) : BaseAdapter() {
    class ViewHolder(viewItem: View) {
        val baseCardPic : ImageView =viewItem.findViewById(R.id.iv_base_card_no_key_pic)
        val baseTitle : TextView =viewItem.findViewById(R.id.tv_base_card_no_key_title)
        val baseTime : TextView =viewItem.findViewById(R.id.tv_base_card_no_key_time)
        val infoItem :LinearLayout=viewItem.findViewById(R.id.ll_base_card_no_key)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val viewHolder: ViewHolder?
        val view: View
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_card_no_key, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val item = getItem(position)
        viewHolder.baseCardPic.setImageResource(item.type)
        viewHolder.baseTitle.text=item.title
        viewHolder.baseTime.text=item.endTime
        viewHolder.infoItem.setOnClickListener{
            intent.putExtra("id",item.id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        return view
    }


    override fun getCount(): Int {
       return list.size
    }

    override fun getItem(position: Int): BaseCard {
         return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}