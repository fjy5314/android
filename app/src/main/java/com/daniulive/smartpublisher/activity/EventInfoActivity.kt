package com.daniulive.smartpublisher.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.model.ApiResult
import com.daniulive.smartpublisher.model.JSConfig
import com.daniulive.smartpublisher.model.EventRecord
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
class EventInfoActivity : BaseActivity() {
    lateinit var eventRecord : EventRecord
    lateinit var desc: EditText
    lateinit var title: EditText
    lateinit var startTime: EditText
    lateinit var type: EditText
    lateinit var btn: Button
    override fun initView(savedInstanceState: Bundle?) {
        desc=findViewById(R.id.ed_event_info_desc)
        title=findViewById(R.id.ed_event_info_title)
        startTime=findViewById(R.id.ed_event_info_start_time)
        type=findViewById(R.id.ed_event_info_type)
        btn=findViewById(R.id.btn_event_info_btn)
        val intent = intent
        val str = intent.getIntExtra("id",-1)
        if(str!=-1){
            getEvent(str)
            initEdit()
        }
        btn.setOnClickListener {
            addEvent()
        }
    }
    fun initEdit(){
        desc.setFocusable(false)
        desc.setFocusableInTouchMode(false)
        title.setFocusable(false)
        title.setFocusableInTouchMode(false)
        startTime.setFocusable(false)
        startTime.setFocusableInTouchMode(false)
        type.setFocusable(false)
        type.setFocusableInTouchMode(false)
        btn.visibility= View.GONE
    }
    fun getEvent(str:Int) {

        var content="{\"id\":\""+str+"\"}"
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/event/getById")
                .content(content)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
                .build()
                .execute(EventCallback())
    }
    inner class EventCallback : StringCallback() {
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

            val jsonType = object : TypeToken<ApiResult<EventRecord>>() {

            }.type
            var result = gson.fromJson<ApiResult<EventRecord>>(response, jsonType)
            eventRecord= result.data!!
            title.hint =eventRecord.title
            startTime.hint=eventRecord.occurTime
            type.hint=eventRecord.type.toString()
            desc.hint=eventRecord.contents

        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }

    fun addEvent(){
        startActivity(Intent(myContext,MainActivity::class.java))
    }
    override fun initTitle(): String {
        return "事件记录详情"
    }

    override fun initContentView(): Int {
        return R.layout.activity_event_info
    }
}