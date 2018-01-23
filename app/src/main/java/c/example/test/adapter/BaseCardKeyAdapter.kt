package c.example.test.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import  c.example.test.R
import c.example.test.model.BaseCard
import java.util.*

/**
 * Created by flny on 2018/1/19.
 */
class BaseCardKeyAdapter(var list: ArrayList<BaseCard>, var context: Context, var boolean: Boolean, val intent: Intent) : BaseAdapter() {
    class ViewHolder(viewItem: View) {
        val key : TextView =viewItem.findViewById(R.id.tv_card_have_key_key)
        val pic : ImageView =viewItem.findViewById(R.id.iv_card_have_key_type_pic)
        val title : TextView =viewItem.findViewById(R.id.tv_card_have_key_title)
        val time : TextView =viewItem.findViewById(R.id.tv_card_have_key_time)
        val specialTime : TextView =viewItem.findViewById(R.id.tv_card_have_key_special_time)
        val infoItem: RelativeLayout=viewItem.findViewById(R.id.rl_card_have_key_info)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val viewHolder: ViewHolder?
        val view: View
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_card_have_key, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val item = getItem(position)
        viewHolder.pic.setImageResource(item.type)
        viewHolder.title.text=item.title
        viewHolder.key.text=item.key
      if(position!=0){
          val preItem = getItem(position-1)
          if(preItem.key==item.key){
              viewHolder.key.setVisibility(View.GONE)
          }

      }
        if(boolean){
            viewHolder.specialTime.setVisibility(View.VISIBLE)
            viewHolder.specialTime.text=item.endTime
            viewHolder.time.text=item.content
        }else{
            viewHolder.time.text=item.endTime
        }
        viewHolder.infoItem.setOnClickListener{
            intent.putExtra("id",item.key)
            startActivity(context,intent,null)
        }
        return view
    }


    override fun getCount(): Int {
       return list.size
    }

    override fun getItem(position: Int): BaseCard {
         return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}