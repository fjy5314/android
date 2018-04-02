package com.daniulive.smartpublisher.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.common.CrashHandler

/**
 * Created by flny on 2018/1/15.
 */
abstract class BaseActivity : AppCompatActivity() {
    lateinit var myContext:Context
    lateinit var titleTextView: TextView
    lateinit var messageButton: ImageButton
    lateinit var leftButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myContext=this
        val crashHandler=CrashHandler
        crashHandler.init(myContext)
        initMap()
        setContentView(initContentView())
        titleTextView = findViewById(R.id.tv_title)
        leftButton = findViewById(R.id.ib_back)
        titleTextView.text = initTitle()
        messageButton = findViewById(R.id.ib_message_button)
        messageButton.setOnClickListener {
            Toast.makeText(this, "taskListView", Toast.LENGTH_SHORT).show();
            startActivity(Intent(this, MessageActivity::class.java))

        }
        leftButton.setOnClickListener {
            onClickLeftButton()
        }
        initData()
        initView(savedInstanceState)
    }
    abstract fun initContentView(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun initTitle(): String
    open fun initMap(){}
    open fun initData(){}
    open fun onClickLeftButton() {
        finish()
    }
}