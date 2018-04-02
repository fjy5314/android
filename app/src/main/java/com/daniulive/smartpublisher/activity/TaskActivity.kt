package com.daniulive.smartpublisher.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.adapter.BaseCardAdapter
import com.daniulive.smartpublisher.adapter.BaseCardKeyAdapter
import com.daniulive.smartpublisher.adapter.TaskPagerAdapter
import com.daniulive.smartpublisher.adapter.ToDoTaskAdapter
import com.daniulive.smartpublisher.common.JSStringCallback
import com.daniulive.smartpublisher.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Request
import java.text.SimpleDateFormat

/**
 * Created by flny on 2018/1/15.
 */
class TaskActivity : BaseActivity() {

    private var buttonList = ArrayList<Button>()

    private lateinit var taskViewPager: ViewPager
    private var viewList = ArrayList<View>()

    private lateinit var todoTaskListView: ListView
    private var todoTaskList = ArrayList<Task>()
    lateinit var todoAdapt: ToDoTaskAdapter
    private lateinit var historyTaskListView: ListView
    private var historyTaskList = ArrayList<BaseCard>()
    lateinit var historyAdapt: BaseCardKeyAdapter
    val picAry= intArrayOf(R.mipmap.ic_bell,R.mipmap.ic_check,R.mipmap.ic_inspection,R.mipmap.ic_bell)
    override fun initView(savedInstanceState: Bundle?) {
        taskViewPager = findViewById(R.id.vp_task)
        buttonList.add(findViewById(R.id.btn_todo_task))
        buttonList.add(findViewById(R.id.btn_history_task))
        for ((index, button) in buttonList.withIndex()) {
            button.setOnClickListener { view ->
                taskViewPager.setCurrentItem(index, false)
                buttonList.forEach {
                    it.setTextColor(resources.getColor(R.color.colorGray))
                }
                (view as Button).setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }

        initTodoTaskListView()
        initHistoryTaskListView()
        taskViewPager.adapter = TaskPagerAdapter(viewList)

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

    override fun initData() {
        initTodoList()
        initHisList()
    }
    fun initHisList( ){
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/task/list")
                .content("{\"status\":1,\"taskDate\":null}")
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

        @SuppressLint("SimpleDateFormat")
        override fun onResponse(response: String, id: Int) {
            System.out.println("成功了——————————————————————————")
            val gson: Gson = Gson()

            val jsonType = object : TypeToken<ApiResult<java.util.ArrayList<Task>>>() {

            }.type
            var result = gson.fromJson<ApiResult<java.util.ArrayList<Task>>>(response, jsonType)
            historyTaskList.clear()
            for (i in result.data!!.indices){
                var date= SimpleDateFormat("yyyy-MM-dd").parse(result.data!![i].startTime)
                historyTaskList.add(BaseCard(result.data!![i].id,picAry[result.data!![i].type!!], result.data!![i].title, "提交时间: "+ result.data!![i].finishTime,(date.year+1900).toString()+"/"+(date.month+1).toString()))
            }
            historyAdapt.notifyDataSetInvalidated()

        }

    }

    fun initTodoList( ){
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/task/list")
                .content("{\"status\":0,\"taskDate\":null}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .execute(TodoListCallback())
    }
    inner class TodoListCallback : JSStringCallback() {
        override fun onResponse(response: String, id: Int) {
            System.out.println("成功了——————————————————————————")
            val gson: Gson = Gson()

            val jsonType = object : TypeToken<ApiResult<java.util.ArrayList<Task>>>() {

            }.type
            var result = gson.fromJson<ApiResult<java.util.ArrayList<Task>>>(response, jsonType)
            todoTaskList.clear()
            for (i in result.data!!.indices){
//                todoTaskList.add(BaseCard(result.data[i].id,picAry[result.data[i].type!!], result.data[i].title, "截止时间: "+result.data[i].deadline))
                todoTaskList.add(result.data!![i])
            }

           todoAdapt.notifyDataSetInvalidated()
        }
    }


    fun initTodoTaskListView() {
        todoTaskListView = ListView(this)
        todoTaskListView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        todoAdapt= ToDoTaskAdapter(todoTaskList, myContext, Intent(this,TaskInfoActivity::class.java))
        todoTaskListView.adapter=todoAdapt
        viewList.add(todoTaskListView)
    }

    fun initHistoryTaskListView() {
        historyTaskListView = ListView(this)
        historyTaskListView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        historyAdapt= BaseCardKeyAdapter(historyTaskList, baseContext, false,  Intent(this,TaskInfoActivity::class.java))
        historyTaskListView.adapter=historyAdapt
        viewList.add(historyTaskListView)
    }

    override fun initContentView(): Int {
        return R.layout.activity_task
    }

    override fun initTitle(): String {
        return "我的任务"
    }
}