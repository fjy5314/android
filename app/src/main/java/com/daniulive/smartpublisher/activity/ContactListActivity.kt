package com.daniulive.smartpublisher.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import okhttp3.Call
import android.view.View
import android.widget.TextView
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.adapter.ContactAdapter
import com.daniulive.smartpublisher.common.PinYinComparator
import com.daniulive.smartpublisher.model.*
import com.daniulive.smartpublisher.widget.CircleTextView
import com.daniulive.smartpublisher.widget.PinYinSlideView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.MediaType
import okhttp3.Request
import java.util.*
import kotlin.collections.ArrayList
import com.daniulive.smartpublisher.model.ApiResult
import org.angmarch.views.NiceSpinner


/**
 * Created by flny on 2018/1/15.
 */
class ContactListActivity : BaseActivity() {
    lateinit var circleCircleTextView: CircleTextView
    lateinit var contactRecyclerView: RecyclerView
    lateinit var header: TextView
    lateinit var contactAdapter: ContactAdapter
     var friendList =ArrayList<Friend>()
    private lateinit var manager: LinearLayoutManager
    var move: Boolean = false
    var mIndex: Int = 0
    override fun initView(savedInstanceState: Bundle?) {
        contactRecyclerView = findViewById(R.id.recycler)
        header = findViewById(R.id.header)
        circleCircleTextView = findViewById(R.id.circleText)
        manager = LinearLayoutManager(myContext)
        contactRecyclerView.layoutManager = manager
        contactAdapter = ContactAdapter(myContext, friendList)
        System.out.println("获取数据")

        contactRecyclerView.adapter = contactAdapter

        initSpinner()
    }
    fun  initSpinner(){
        val niceSpinner = findViewById<View>(R.id.nice_spinner) as NiceSpinner
        val dataset = LinkedList(Arrays.asList("全部群組", "分组一", "分组二"))
        niceSpinner.attachDataSource<String>(dataset)

        niceSpinner.addOnItemClickListener {_, _, position, _ ->
            //TODO
            when(position){
                0 -> getContact("{\"oneKeySearch\":null,\"job\":null}")
                1 -> getContact("{\"oneKeySearch\":\"电力局\",\"job\":null}")
            }
        }

    }

    override fun initData() {
        getContact("{\"oneKeySearch\":null,\"job\":null}")

    }
     fun getContact(content: String) {
        //
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/contact/list")
                .content(content)
                .mediaType(MediaType.parse("application/json; charset=utf-8,"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .connTimeOut(200000)
                .execute(MyStringCallback())
    }
    inner class MyStringCallback : StringCallback() {

        override fun onError(call: Call, e: java.lang.Exception?, id: Int) {
            System.out.println("出错了——————————————————————————")
            System.out.println(e)
        }
        override fun onResponse(response: String, id: Int) {
            System.out.println("成功了——————————————————————————")

            val gson: Gson = Gson()

            val jsonType = object : TypeToken<ApiResult<ArrayList<Worker>>>() {

            }.type
            var result = gson.fromJson<ApiResult<ArrayList<Worker>>>(response, jsonType)
            friendList.clear()

            for(i in result.data!!.indices){
                friendList.add(Friend(result.data!![i].id, result.data!![i].name!!, result.data!![i].phone, result.data!![i].job))
            }
            System.out.println("获取数据")
            Collections.sort(friendList, PinYinComparator())

            contactAdapter.fillDataList(friendList)
            contactRecyclerView.addItemDecoration(DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL))
            contactRecyclerView.addOnScrollListener(contactRecyclerListener())

            initPinYinSlideView()
        }
    }
    fun initPinYinSlideView(){
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
                        if (friend.getFirstPinyin().toUpperCase() == text) {
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
    inner class  contactRecyclerListener :RecyclerView.OnScrollListener() {
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
                header.text = friend!!.getFirstPinyin()
                header.setBackgroundResource(R.color.brown)
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
    }

    override fun initTitle(): String {
        return "通讯录"
    }

    override fun initContentView(): Int {
        return R.layout.activity_contact
    }
}