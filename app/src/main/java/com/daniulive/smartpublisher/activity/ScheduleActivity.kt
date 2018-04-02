package com.daniulive.smartpublisher.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ListView
import  com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.adapter.BaseCardAdapter
import com.daniulive.smartpublisher.common.JSStringCallback
import com.daniulive.smartpublisher.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.MediaType
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by flny on 2018/1/15.
 */
class ScheduleActivity : BaseActivity() {
    private lateinit var calendarView: CalendarView
    var taskList=ArrayList<BaseCard>()
    lateinit  var listView: ListView
    @SuppressLint("SimpleDateFormat")
    override fun initView(savedInstanceState: Bundle?) {
        listView =  findViewById(R.id.lv_task)
        calendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            initTask(year.toString()+"-"+(month+1)+"-"+dayOfMonth)

        }
        val df = SimpleDateFormat("yyyy-MM-dd")
        initTask( df.format(Date(System.currentTimeMillis())))
    }

    override fun initData() {

    }
    fun initTask(str: String){
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/task/list")
                .content("{\"status\":null,\"taskDate\":\""+ str+"\"}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .execute(TaskListCallback())
    }
    inner class TaskListCallback : JSStringCallback() {
        override fun onResponse(response: String, id: Int) {
            System.out.println("成功了——————————————————————————")
            val gson = Gson()

            val jsonType = object : TypeToken<ApiResult<ArrayList<Task>>>() {

            }.type
            var result = gson.fromJson<ApiResult<ArrayList<Task>>>(response, jsonType)
            taskList.clear()
            for (i in result.data!!.indices){
                taskList.add(BaseCard(result.data!![i].id,R.mipmap.ic_bell, result.data!![i].title, "截止时间: "+ result.data!![i].deadline))
            }
            listView.adapter = BaseCardAdapter(taskList, baseContext, Intent(myContext, TaskInfoActivity::class.java))
        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }
    override fun initTitle(): String {
        return "我的日程"
    }

    override fun initContentView(): Int {
        return R.layout.activity_schedule
    }
}