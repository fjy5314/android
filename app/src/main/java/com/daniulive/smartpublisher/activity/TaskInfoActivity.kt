package com.daniulive.smartpublisher.activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Request



/**
 * Created by flny on 2018/1/15.
 */
class TaskInfoActivity : BaseActivity() {
    lateinit var task:Task
    lateinit var title:TextView
    lateinit var starTime:TextView
    lateinit var endTime:TextView
    lateinit var desc:TextView


    override fun initView(savedInstanceState: Bundle?) {
        title=findViewById(R.id.tv_task_info_title)
        starTime=findViewById(R.id.tv_task_info_star_time)
        endTime=findViewById(R.id.tv_task_info_end_time)
        desc=findViewById(R.id.tv_task_info_desc)
        getTask()
        var btn =findViewById<Button>(R.id.btn_task_info_btn)
        btn.setOnClickListener{
            val newIntent=Intent(this,ReportInfoActivity::class.java)
            newIntent.putExtra("id",-1)
            startActivity(newIntent)
        }
    }

     fun getTask() {
        val intent = intent
        val str = intent.getIntExtra("id",0)
        var content="{\"id\":\""+str+"\"}"
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/task/getById")
                .content(content)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .execute(TaskCallback())
    }
    inner class TaskCallback : StringCallback() {
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

            val jsonType = object : TypeToken<ApiResult<Task>>() {

            }.type
            var result = gson.fromJson<ApiResult<Task>>(response, jsonType)
            task= result.data!!
            title.text=task.title
            starTime.text=task.startTime
            endTime.text=task.deadline
            desc.text=task.description

        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }
    override fun initContentView(): Int {
        return R.layout.activity_task_info
    }

    override fun initTitle(): String {
        return "任务详情"
    }



}