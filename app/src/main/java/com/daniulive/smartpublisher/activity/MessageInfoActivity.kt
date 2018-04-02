package com.daniulive.smartpublisher.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import  com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.model.ApiResult
import com.daniulive.smartpublisher.model.JSConfig
import com.daniulive.smartpublisher.model.Message
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
class MessageInfoActivity : BaseActivity() {
    var id: Int = -1
    lateinit var btn: Button
    lateinit var title: TextView
    lateinit var time: TextView
    lateinit var pic: ImageView
    lateinit var content:TextView
    
    override fun initView(savedInstanceState: Bundle?) {
        time = findViewById(R.id.tv_message_info_time)
        title = findViewById(R.id.tv_message_info_title)
        pic=findViewById(R.id.iv_messsage_info_pic)
        content=findViewById(R.id.tv_message_info_content)
        btn = findViewById(R.id.btn_message_submit)
        getMessage()
    }

    override fun initData() {
        val messageButton = findViewById<ImageButton>(R.id.ib_message_button)
        messageButton.visibility = View.INVISIBLE
        val mark = findViewById<TextView>(R.id.tv_mark)
        mark.visibility = View.INVISIBLE
    }

    fun getMessage() {
        id = intent.getIntExtra("id", -1)
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL + "/biz/notify/getById")
                .content("{\"id\":" + id + "}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept", "application/json ")
                .addHeader("Content-Type", "application/json")
                .build()
                .execute(MessageInfoCallback())
    }

    inner class MessageInfoCallback : StringCallback() {
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

            val jsonType = object : TypeToken<ApiResult<Message>>() {

            }.type
            var result = gson.fromJson<ApiResult<Message>>(response, jsonType)
            time.text= result.data!!.notifyTime
            title.text= result.data!!.title
            content.text= result.data!!.content
            btn.setOnClickListener {
                val intent = Intent(myContext, MessageReportActivity::class.java)
                intent.putExtra("id", result.data!!.notifyDetail!!.id)
                startActivity(intent)
            }
        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }

    override fun initTitle(): String {
        return "消息详情"
    }

    override fun initContentView(): Int {
        return R.layout.activity_message_info
    }
}