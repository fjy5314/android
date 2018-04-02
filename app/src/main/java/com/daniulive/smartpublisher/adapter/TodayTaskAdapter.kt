package com.daniulive.smartpublisher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.model.Task
import java.util.*


/**
 * Created by flny on 2018/1/19.
 */
class TodayTaskAdapter(var list: ArrayList<Task>, var context: Context, val intent: Intent,var boolean: Boolean) : BaseAdapter() {

    class ViewHolder(viewItem: View) {
        val pic : ImageView =viewItem.findViewById(R.id.iv_today_task_type)
        val title : TextView =viewItem.findViewById(R.id.tv_today_task_title)
        val layout : LinearLayout =viewItem.findViewById(R.id.ll_today_task)
        val time: TextView=viewItem.findViewById(R.id.tv_today_task_time)
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val viewHolder: ViewHolder?
        val view: View
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_today_task, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val item = getItem(position)
        viewHolder.title.text=item.title
        var  ary=item.startTime!!.split(" ")
        viewHolder.time.text=ary[1]

        if(item.type==1){
            viewHolder.pic.setImageResource(R.mipmap.ic_check)
            viewHolder.pic.setBackgroundResource(R.drawable.ic_circle_blue)
        }else if(item.type==2){
            viewHolder.pic.setImageResource(R.mipmap.ic_inspection)
            viewHolder.pic.setBackgroundResource(R.drawable.ic_circle_blue)
        }else{
            viewHolder.pic.setImageResource(R.mipmap.ic_bell)
            viewHolder.pic.setBackgroundResource(R.drawable.ic_circle_orange)
        }

        if(item.status==0){
            viewHolder.pic.setBackgroundResource(R.drawable.ic_circle_gray)
            viewHolder.title.setTextColor(context.resources.getColor(R.color.colorGray))
            viewHolder.title.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        }else{
                if(boolean){
                    viewHolder.title.setTextColor(context.resources.getColor(R.color.colorWhite))
                }else{
                    viewHolder.title.setTextColor(context.resources.getColor(R.color.colorFont1))
                }


            viewHolder.title.paint.flags = 0
        }
        view.setOnClickListener{
            intent.putExtra("id",item.id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        return view
    }


    override fun getCount(): Int {
       return list.size
    }

    override fun getItem(position: Int): Task {
         return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}