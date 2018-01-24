package c.example.test.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import c.example.test.R
import c.example.test.adapter.ContactAdapter
import c.example.test.common.PinYinComparator
import c.example.test.model.Friend
import c.example.test.widget.CircleTextView
import c.example.test.widget.PinYinSlideView
import java.util.*

/**
 * Created by flny on 2018/1/15.
 */
class ContactListActivity : BaseActivity() {
    lateinit var circleCircleTextView: CircleTextView
    lateinit var contactRecyclerView: RecyclerView
    lateinit var header: TextView
    lateinit var contactAdapter: ContactAdapter
    private var friendList = ArrayList<Friend>()
    private lateinit var manager: LinearLayoutManager
    var move: Boolean = false
    var mIndex: Int = 0
    override fun initView(savedInstanceState: Bundle?) {
        circleCircleTextView = findViewById(R.id.circleText)
        val pinYinSlideView: PinYinSlideView = findViewById(R.id.pinYinSlideView)
        pinYinSlideView.setOnShowTextListener(object : PinYinSlideView.OnShowTextListener {
            override fun showText(text: String) {
                circleCircleTextView.setText(text)
                circleCircleTextView.visibility = View.VISIBLE
                if (text != "↑") {
                    var position = 0
                    var hasPinyin = false
                    for (i in 0 until friendList.size) {
                        val friend = friendList[i]
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
        })
        contactRecyclerView = findViewById(R.id.recycler)
        header = findViewById(R.id.header)
        manager = LinearLayoutManager(this)
        contactRecyclerView.layoutManager = manager
        contactAdapter = ContactAdapter(this, friendList)
        contactRecyclerView.adapter = contactAdapter
        contactRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        contactRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        if (n >= 0 && n < contactRecyclerView.childCount) {
                            val top = contactRecyclerView.getChildAt(n).top
                            contactRecyclerView.scrollBy(0, top)
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

    override fun initData() {
        friendList.add(Friend("张三", "空间的市场", "1762812638"))
        friendList.add(Friend("李四", "organization2", "bbb"))
        friendList.add(Friend("上次", "organization2", "bbb"))
        friendList.add(Friend("为借口", "organization2", "bbb"))
        friendList.add(Friend("违反", "organization2", "bbb"))
        friendList.add(Friend("帮忙", "organization2", "bbb"))
        friendList.add(Friend("李四", "organization2", "bbb"))
        friendList.add(Friend("李四", "organization2", "bbb"))
        friendList.add(Friend("李四", "organization2", "bbb"))
        friendList.add(Friend("李四", "organization2", "bbb"))
        friendList.add(Friend("fnegjy", "organization3", "sds"))
        friendList.add(Friend("fnegjy", "organization3", "sds"))
        friendList.add(Friend("fnegjy", "organization3", "sds"))
        friendList.add(Friend("fnegjy", "organization3", "sds"))
        friendList.add(Friend("fnegjy", "organization3", "sds"))

        Collections.sort(friendList, PinYinComparator())
    }


    private fun scrollToPosition(position: Int) {
        if (position >= 0 && position <= friendList.size - 1) {
            val firstItem = manager.findFirstVisibleItemPosition()
            val lastItem = manager.findLastVisibleItemPosition()
            if (position <= firstItem) {
                contactRecyclerView.scrollToPosition(position)
            } else if (position <= lastItem) {
                val top = contactRecyclerView.getChildAt(position - firstItem).top
                contactRecyclerView.scrollBy(0, top)
            } else {
                contactRecyclerView.scrollToPosition(position)
                mIndex = position
                move = true
            }
        }
    }

    override fun initTitle(): String {
        return "通讯录"
    }

    override fun initContentView(): Int {
        return R.layout.activity_contact
    }
}