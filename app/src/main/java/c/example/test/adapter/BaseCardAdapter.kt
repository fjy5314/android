package c.example.test.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import  c.example.test.R
import c.example.test.model.BaseCard

/**
 * Created by flny on 2018/1/19.
 */
class BaseCardAdapter(var list: ArrayList<BaseCard>, var context: Context) : BaseAdapter() {
    class ViewHolder(viewItem: View) {
        val baseCardPic : ImageView =viewItem.findViewById(R.id.iv_base_card_no_key_pic)
        val baseTitle : TextView =viewItem.findViewById(R.id.tv_base_card_no_key_title)
        val baseTime : TextView =viewItem.findViewById(R.id.tv_base_card_no_key_time)
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