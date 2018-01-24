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
class ContactAdapter(context: Context, dataList: List<Friend>) : BaseRecyclerAdapter<Friend>(context, dataList) {

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return BaseHolder(parent, R.layout.item_contact)
    }

    override fun bindCustomViewHolder(holder: BaseHolder, position: Int) {
        val friend = getItem(position)
        (holder.getView(R.id.it_contact_name) as TextView).text = friend!!.name
        (holder.getView(R.id.it_contact_organization) as TextView).text = friend.organization
        (holder.getView(R.id.tv_contact_phone) as TextView).text = friend.phoneNumber
        holder.itemView.contentDescription = friend.getFirstPinyin()

        if (position == 0) {
            holder.getView(R.id.stick_container).visibility = View.VISIBLE
            (holder.getView(R.id.header) as TextView).text = friend.getFirstPinyin()
        } else {
            if (!TextUtils.equals(friend.getFirstPinyin(), getItem(position - 1)!!.getFirstPinyin())) {
                holder.getView(R.id.stick_container).visibility = View.VISIBLE
                (holder.getView(R.id.header) as TextView).text = friend.getFirstPinyin()
            } else {
                holder.getView(R.id.stick_container).visibility = View.GONE
            }
        }
    }

    override protected fun getCustomViewType(position: Int): Int {
        return 0
    }
}
