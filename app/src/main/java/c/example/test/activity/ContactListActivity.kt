package c.example.test.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import  c.example.test.R
import android.support.v7.widget.LinearLayoutManager
import c.example.test.model.Friend
import c.example.test.adapter.ContactAdapter
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.View
import c.example.test.widget.CircleTextView
import c.example.test.widget.PinYinSlideView
/**
 * Created by flny on 2018/1/15.
 */
class ContactListActivity : BaseActivity() {
   lateinit var circleText: CircleTextView
    lateinit var contactList: RecyclerView
    lateinit var header: TextView
    lateinit var contactAdapter: ContactAdapter
    lateinit  var friends: List<Friend>
    lateinit var manager: LinearLayoutManager
    var move: Boolean = false
    var mIndex: Int = 0
    override fun initView(savedInstanceState: Bundle?) {
        circleText = findViewById<View>(R.id.circleText) as CircleTextView
        val pinYinSlideView = findViewById<View>(R.id.pinYinSlideView) as PinYinSlideView
        pinYinSlideView.setOnShowTextListener(object : PinYinSlideView.OnShowTextListener {
            override fun showText(text: String) {
                circleText.setText(text)
                if (text != null) {
                    if (text != "↑") {
                        var position = 0
                        var hasPinyin = false
                        for (i in 0 until friends.size) {
                            val friend = friends.get(i)
                            if (friend.getFirstPinyin() == text) {
                                position = i
                                hasPinyin = true
                                break
                            }
                        }
                        if (hasPinyin) {
                            this@ContactListActivity.scrollToPosition(position)
                        }
                    } else {
                        this@ContactListActivity.scrollToPosition(0)
                    }
                }

            }
        })
        contactList = findViewById<View>(R.id.recycler) as RecyclerView
        header = findViewById<View>(R.id.header) as TextView
        manager = LinearLayoutManager(this)
        contactList.setLayoutManager(manager)
        contactAdapter = ContactAdapter(this, friends)
        contactList.setAdapter(contactAdapter)
        contactList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        contactList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView!!.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val firstItem = layoutManager.findFirstVisibleItemPosition()
                    //                    View itemView=recyclerView.getChildAt(firstItem);
                    //                    if (itemView!=null&&itemView.getContentDescription()!=null){
                    //                        header.setText(itemView.getContentDescription());
                    //                    }
                    val friend = contactAdapter.getItem(firstItem)
                    header.setText(friend!!.getFirstPinyin())
                    if (move) {
                        move = false
                        val n = mIndex - firstItem
                        if (n >= 0 && n < contactList.getChildCount()) {
                            val top = contactList.getChildAt(n).top
                            contactList.scrollBy(0, top)
                        }
                    }
                    //itemView.getId();
                    //                    header.setText(itemView.getContentDescription());
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }


    private fun scrollToPosition(position: Int) {
        if (position >= 0 && position <= friends.size - 1) {
            val firstItem = manager.findFirstVisibleItemPosition()
            val lastItem = manager.findLastVisibleItemPosition()
            if (position <= firstItem) {
                contactList.scrollToPosition(position)
            } else if (position <= lastItem) {
                val top = contactList.getChildAt(position - firstItem).getTop()
                contactList.scrollBy(0, top)
            } else {
                contactList.scrollToPosition(position)
                mIndex = position
                move = true
            }
        }
    }
    override fun initTitle(): String {
        return "通讯录"
    }

    override fun initContentView(): Int {
        return R.layout.activity_map
    }
}