package com.daniulive.smartpublisher.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import  com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.adapter.BaseCardAdapter
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
class ReportActivity : BaseActivity() {
    private var buttonList = ArrayList<Button>()
    private lateinit var taskViewPager: ViewPager
    private var viewList = ArrayList<View>()
    private var todoList = ArrayList<BaseCard>()
    private var historyList = ArrayList<BaseCard>()
    init {
        initTodoList()
        initHisList()
    }
    override fun initView(savedInstanceState: Bundle?) {

        taskViewPager = findViewById<ViewPager>(R.id.vp_task)
        val btn1: Button=findViewById(R.id.btn_todo_task)
        btn1.text="待提交"
        val btn2: Button=findViewById(R.id.btn_history_task)
        btn2.text="历史报告"
        buttonList.add(btn1)
        buttonList.add(btn2)
        for ((index, button) in buttonList.withIndex()) {
            button.setOnClickListener { view ->
                Toast.makeText(baseContext, index.toString(), Toast.LENGTH_SHORT).show()
                taskViewPager.setCurrentItem(index, false)
                buttonList.forEach {
                    it.setTextColor(resources.getColor(R.color.colorGray))
                }
                (view as Button).setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }


        taskViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                for ((index, button) in buttonList.withIndex()) {
                    if (index == position) {
                        buttonList[index].setTextColor(resources.getColor(R.color.colorPrimary))
                    }else{
                        buttonList[index].setTextColor(resources.getColor(R.color.colorGray))
                    }
                }
            }
        })


    }

    fun initHisList( ){
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/report/list")
                .content("{\"status\":1,\"oneKeySearch\":null,\"type\":null,\"author\":null}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .execute(HisListCallback())
    }
    inner class HisListCallback : StringCallback() {
        override fun onError(call: Call, e: java.lang.Exception?, id: Int) {
            System.out.println("出错了——————————————————————————")
            System.out.println(e)
        }

        override fun onBefore(request: Request, id: Int) {
            System.out.println("D打印request——————————————————————————")
            System.out.println(request)

        }

        override fun onAfter(id: Int) {
        }

        override fun onResponse(response: String, id: Int) {
            System.out.println("成功了——————————————————————————")
            val gson: Gson = Gson()

            val jsonType = object : TypeToken<ApiResult<java.util.ArrayList<Report>>>() {

            }.type
            var result = gson.fromJson<ApiResult<java.util.ArrayList<Report>>>(response, jsonType)
            var xx=SimpleDateFormat("yyyy-MM-dd")
           for (i in result.data!!.indices){
                var date= xx.parse(result.data!![i].deadline)
                historyList.add(BaseCard(result.data!![i].id,R.mipmap.ic_bell, result.data!![i].title, "提交时间: "+ result.data!![i].deadline,(date.year+1900).toString()+"/"+(date.month+1).toString()))
            }
            initTodoListView()
            initHisListView()
            taskViewPager.adapter = TaskPagerAdapter(viewList)
        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }

    fun initTodoList( ){
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/report/list")
                .content("{\"status\":0,\"oneKeySearch\":null,\"type\":null,\"author\":null}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .execute(TodoListCallback())
    }
    inner class TodoListCallback : StringCallback() {
        override fun onError(call: Call, e: java.lang.Exception?, id: Int) {
            System.out.println("出错了——————————————————————————")
            System.out.println(e)
        }

        override fun onBefore(request: Request, id: Int) {
            System.out.println("D打印request——————————————————————————")
            System.out.println(request)

        }

        override fun onAfter(id: Int) {
        }

        override fun onResponse(response: String, id: Int) {
            System.out.println("成功了——————————————————————————")
            val gson: Gson = Gson()

            val jsonType = object : TypeToken<ApiResult<java.util.ArrayList<Report>>>() {

            }.type
            var result = gson.fromJson<ApiResult<java.util.ArrayList<Report>>>(response, jsonType)
            for (i in result.data!!.indices){
                todoList.add(BaseCard(result.data!![i].id,R.mipmap.ic_bell, result.data!![i].title, "截止时间: "+ result.data!![i].deadline))
            }

        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }
    fun initTodoListView() {
        var xxx: ListView = ListView(this)
        xxx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        xxx.adapter = BaseCardAdapter(todoList, baseContext,Intent(this,ReportInfoActivity::class.java))
        viewList.add(xxx)
    }
    fun initHisListView() {
        var xxx: ListView = ListView(this)
        xxx.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        xxx.adapter = BaseCardKeyAdapter(historyList, baseContext,false,Intent(this,ReportInfoActivity::class.java))
        viewList.add(xxx)
    }


    override fun initTitle(): String {
        return "我的报告"
    }

    override fun initContentView(): Int {
        return R.layout.activity_task
    }
}