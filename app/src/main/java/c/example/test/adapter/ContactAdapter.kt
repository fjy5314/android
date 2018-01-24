package c.example.test.adapter


import android.widget.TextView
import android.content.Context
import android.text.TextUtils
import android.view.View
import c.example.test.model.Friend
import android.view.ViewGroup
import c.example.test.R


/**
 * Created by flny on 2018/1/24.
 */
class ContactAdapter : BaseRecyclerAdapter<Friend, BaseHolder> {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, dataList: List<Friend>) : super(context, dataList) {}

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return BaseHolder(parent, R.layout.item_contact)
    }

    override fun bindCustomViewHolder(holder: BaseHolder, position: Int) {
        val friend = getItem(position)
        (holder.getView(R.id.it_contact_name) as TextView).setText(friend!!.name)
        if (position == 0) {
            holder.getView(R.id.stick_container).setVisibility(View.VISIBLE)
            (holder.getView(R.id.header) as TextView).setText(friend!!.getFirstPinyin())
        } else {
            if (!TextUtils.equals(friend.getFirstPinyin(), getItem(position - 1)!!.getFirstPinyin())) {
                holder.getView(R.id.stick_container).setVisibility(View.VISIBLE)
                (holder.getView(R.id.header) as TextView).setText(friend.getFirstPinyin())
            } else {
                holder.getView(R.id.stick_container).setVisibility(View.GONE)
            }
        }
        holder.itemView.setContentDescription(friend.getFirstPinyin())
    }

    override protected fun getCustomViewType(position: Int): Int {
        return 0
    }
}
