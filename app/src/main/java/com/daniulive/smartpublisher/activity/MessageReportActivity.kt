package com.daniulive.smartpublisher.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import  com.daniulive.smartpublisher.R

/**
 * Created by flny on 2018/1/15.
 */
class MessageReportActivity : BaseActivity() {
    lateinit var btn :Button
    override fun initView(savedInstanceState: Bundle?) {

        btn = findViewById(R.id.btn_message_submit)
        btn.setOnClickListener {
            startActivity(Intent(myContext,MainActivity::class.java))
        }
    }
    fun modifyDetail(){

    }
    override fun initData() {
        val messageButton = findViewById<ImageButton>(R.id.ib_message_button)
        messageButton.visibility = View.INVISIBLE
        val mark = findViewById<TextView>(R.id.tv_mark)
        mark.visibility = View.INVISIBLE
    }

    override fun initTitle(): String {
        return "消息回执"
    }

    override fun initContentView(): Int {
        return R.layout.activity_message_report
    }
}