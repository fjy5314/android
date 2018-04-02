package com.daniulive.smartpublisher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.model.Task
import java.util.ArrayList

/**
 * Created by flny on 2018/1/19.
 */
class ToDoTaskAdapter(var list: ArrayList<Task>, var context: Context, val intent: Intent) : BaseAdapter() {

    class ViewHolder(viewItem: View) {
        val pic : ImageView =viewItem.findViewById(R.id.iv_to_do_task_pic)
        val title : TextView =viewItem.findViewById(R.id.tv_to_do_task_title)
        val time: TextView =viewItem.findViewById(R.id.tv_to_do_task_time)
        val content: TextView =viewItem.findViewById(R.id.tv_to_do_task_content)
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val viewHolder: ViewHolder?
        val view: View
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_to_do_task, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val item = getItem(position)
        viewHolder.title.text=item.title
        var  ary=item.startTime!!.split(" ")
        viewHolder.time.text="截止时间: "+item.deadline
        viewHolder.content.text=item.description
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
         return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}