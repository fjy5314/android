package com.daniulive.smartpublisher.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.*
import  com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.adapter.BaseCardKeyAdapter
import com.daniulive.smartpublisher.adapter.TaskPagerAdapter
import com.daniulive.smartpublisher.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by flny on 2018/1/15.
 */
class MessageActivity : BaseActivity() {
    private var buttonList = ArrayList<Button>()
    private lateinit var taskViewPager: ViewPager
    private var viewList = ArrayList<View>()
    private var informList = ArrayList<BaseCard>()
    private var alarmList = ArrayList<BaseCard>()
    private var reportList = ArrayList<BaseCard>()
    lateinit var informListAdapt: BaseCardKeyAdapter
    lateinit var alarmListAdapt:BaseCardKeyAdapter
    lateinit var reportListAdapt:BaseCardKeyAdapter
    lateinit var taskPagerAdapter: TaskPagerAdapter

    override fun initView(savedInstanceState: Bundle?) {
        val messageButton = findViewById<ImageButton>(R.id.ib_message_button)
        messageButton.visibility = View.INVISIBLE
        val mark = findViewById<TextView>(R.id.tv_mark)
        mark.visibility = View.INVISIBLE
        initButtonList()
        initInform()
        initAlarm()
        initReport()
        informListAdapt= BaseCardKeyAdapter(informList, baseContext, true,  Intent(this, MessageInfoActivity::class.java))
        initListView(informListAdapt)
        alarmListAdapt= BaseCardKeyAdapter(alarmList, baseContext, true,  Intent(this, MessageInfoActivity::class.java))
        initListView(alarmListAdapt)
        reportListAdapt= BaseCardKeyAdapter(reportList, baseContext, true,  Intent(this, MessageInfoActivity::class.java))
        initListView(reportListAdapt)
        taskPagerAdapter= TaskPagerAdapter(viewList)
        taskViewPager.adapter = taskPagerAdapter

        taskViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                for ((index, button) in buttonList.withIndex()) {
                    if (index == position) {
                        buttonList[index].setTextColor(resources.getColor(R.color.colorWhite))
                        buttonList[index].setBackgroundColor(resources.getColor(R.color.colorSkyBlue))
                    } else {
                        buttonList[index].setTextColor(resources.getColor(R.color.colorSkyBlue))
                        buttonList[index].setBackgroundColor(resources.getColor(R.color.colorWhite))
                    }
                }
            }
        })
    }

    private fun initButtonList() {
        taskViewPager = findViewById(R.id.vp_base)
        findViewById<Button>(R.id.btn_message_inform).setBackgroundColor(resources.getColor(R.color.colorSkyBlue))
        findViewById<Button>(R.id.btn_message_inform).setTextColor(resources.getColor(R.color.colorWhite))
        buttonList.add(findViewById<Button>(R.id.btn_message_inform))
        buttonList.add(findViewById<Button>(R.id.btn_message_alarm))
        buttonList.add(findViewById<Button>(R.id.btn_message_report))
        for ((index, button) in buttonList.withIndex()) {
            button.setOnClickListener { view ->
                Toast.makeText(baseContext, index.toString(), Toast.LENGTH_SHORT).show()
                taskViewPager.setCurrentItem(index, false)
                buttonList.forEach {
                    it.setTextColor(resources.getColor(R.color.colorSkyBlue))
                    it.setBackgroundColor(resources.getColor(R.color.colorWhite))
                }
                (view as Button).setTextColor(resources.getColor(R.color.colorWhite))
                view.setBackgroundColor(resources.getColor(R.color.colorSkyBlue))
            }
        }
    }

    private fun initInform() {
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/notify/list")
                .content("{ \"type\":40,\"receiveUserId\":null }")
                .mediaType(MediaType.parse("application/json; charset=utf-8,"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .connTimeOut(200000)
                .execute(InformCallback())
    }
    inner class InformCallback : StringCallback() {

        override fun onError(call: Call, e: java.lang.Exception?, id: Int) {
            Log.i("InformCallbackErr ---", "Exception: " +e)
        }
        override fun onBefore(request: Request, id: Int) {
        }
        override fun onAfter(id: Int) {
        }
        override fun onResponse(response: String, id: Int) {
            Log.i("InformCallbackSuccess", "success: " )
            val gson: Gson = Gson()
            val jsonType = object : TypeToken<ApiResult<ArrayList<Message>>>() {}.type
            var result = gson.fromJson<ApiResult<ArrayList<Message>>>(response, jsonType)
            informList.clear()
            System.out.println("数据")
            formatList(informList, result.data!!)
//            taskPagerAdapter.notifyDataSetChanged()
            informListAdapt.notifyDataSetInvalidated()
        }

        override fun inProgress(progress: Float, total: Long, id: Int) {

        }
    }
    private fun initAlarm() {
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/notify/list")
                .content("{ \"type\":41,\"receiveUserId\":null }")
                .mediaType(MediaType.parse("application/json; charset=utf-8,"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .connTimeOut(200000)
                .execute(AlarmCallback())
    }
    inner class AlarmCallback : StringCallback() {

        override fun onError(call: Call, e: java.lang.Exception?, id: Int) {
            System.out.println(e)
        }
        override fun onBefore(request: Request, id: Int) {
        }
        override fun onAfter(id: Int) {
        }
        override fun onResponse(response: String, id: Int) {
            val gson: Gson = Gson()
            val jsonType = object : TypeToken<ApiResult<ArrayList<Message>>>() {}.type
            var result = gson.fromJson<ApiResult<ArrayList<Message>>>(response, jsonType)
            alarmList.clear()
            formatList(alarmList, result.data!!)
//            taskPagerAdapter.notifyDataSetChanged()
            alarmListAdapt.notifyDataSetInvalidated()
        }

        override fun inProgress(progress: Float, total: Long, id: Int) {

        }
    }

    private fun initReport() {
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/notify/list")
                .content("{ \"type\":42,\"receiveUserId\":null }")
                .mediaType(MediaType.parse("application/json; charset=utf-8,"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .connTimeOut(200000)
                .execute(ReportCallback())
    }
    inner class ReportCallback : StringCallback() {

        override fun onError(call: Call, e: java.lang.Exception?, id: Int) {
            System.out.println(e)
        }
        override fun onResponse(response: String, id: Int) {
            val gson: Gson = Gson()
            val jsonType = object : TypeToken<ApiResult<ArrayList<Message>>>() {}.type
            var result = gson.fromJson<ApiResult<ArrayList<Message>>>(response, jsonType)
            reportList.clear()
            formatList(reportList, result.data!!)
//            taskPagerAdapter.notifyDataSetChanged()
            reportListAdapt.notifyDataSetInvalidated()
        }

    }

    private fun initListView(adapt: BaseCardKeyAdapter) {
        var listView: ListView = ListView(this)
        listView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        listView.adapter = adapt
        viewList.add(listView)
    }


    fun formatList(list: ArrayList<BaseCard>,res :ArrayList<Message>){
        var format= SimpleDateFormat("yyyy-MM-dd")

        for(i in res.indices){
            var date= format.parse(res[i].notifyTime)
            var  cal :Calendar=Calendar.getInstance()
            cal.time = date
            var key = if(res[i].isRead == 0)  "未读消息" else (cal.get(Calendar.MONTH)+1).toString()+"月"
            var time=cal.get(Calendar.DAY_OF_MONTH).toString()+"/"+(cal.get(Calendar.MONTH)+1)
            list.add(BaseCard(res[i].id,R.mipmap.ic_bell,res[i].title,time,key,res[i].brief))
        }
    }
    override fun initTitle(): String {
        return "消息中心"
    }

    override fun initContentView(): Int {
        return R.layout.activity_message
    }
}