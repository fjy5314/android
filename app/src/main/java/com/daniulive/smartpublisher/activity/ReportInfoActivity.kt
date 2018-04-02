package com.daniulive.smartpublisher.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
class ReportInfoActivity : BaseActivity() {
    lateinit var report :Report
    lateinit var desc:EditText
    lateinit var title:EditText
    lateinit var startTime:EditText
    lateinit var endTime:EditText
    lateinit var btn:Button
    override fun initView(savedInstanceState: Bundle?) {
        desc=findViewById(R.id.ed_report_info_desc)
        title=findViewById(R.id.ed_report_info_title)
        startTime=findViewById(R.id.ed_report_info_start_time)
        endTime=findViewById(R.id.ed_report_info_end_time)
        btn=findViewById(R.id.btn_report_info_btn)
        val intent = intent
        val str = intent.getIntExtra("id",-1)
        if(str!=-1){
            getReport(str)
            initEdit()
        }
        btn.setOnClickListener {
            addReport()
        }
    }
    fun initEdit(){
        desc.isFocusable = false
        desc.isFocusableInTouchMode = false
        title.isFocusable = false
        title.isFocusableInTouchMode = false
        startTime.isFocusable = false
        startTime.isFocusableInTouchMode = false
        endTime.isFocusable = false
        endTime.isFocusableInTouchMode = false
        btn.visibility= View.GONE
    }
    fun getReport(str:Int) {

        var content="{\"id\":\""+str+"\"}"
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/report/getById")
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
        override fun onResponse(response: String, id: Int) {
            System.out.println("成功了——————————————————————————")
            val gson: Gson = Gson()

            val jsonType = object : TypeToken<ApiResult<Report>>() {

            }.type
            var result = gson.fromJson<ApiResult<Report>>(response, jsonType)
            report= result.data!!
            title.hint =report.title
            startTime.hint=report.startTime
            endTime.hint=report.deadline
            desc.hint=report.description

        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }


    fun addReport(){
        startActivity(Intent(myContext,MainActivity::class.java))
    }
    override fun initTitle(): String {
        return "报告详情"
    }

    override fun initContentView(): Int {
        return R.layout.activity_report_info
    }
}