package com.daniulive.smartpublisher.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.baidu.location.d.j.J
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.common.JSStringCallback
import com.daniulive.smartpublisher.common.SPUtil
import com.daniulive.smartpublisher.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType

/**
 * Created by flny on 2018/1/15.
 */
class LoginActivity : BaseActivity() {
    lateinit var btn: Button
    lateinit var passInput: EditText
    lateinit var nameInput: EditText
    lateinit var errText: TextView
    val spUtil = SPUtil
    override fun initView(savedInstanceState: Bundle?) {
        passInput = findViewById(R.id.et_login_pass)
        nameInput = findViewById(R.id.et_login_name)
        errText = findViewById(R.id.tv_login_error)
        btn = findViewById(R.id.btn_login)
        btn.setOnClickListener {
            validate()
        }
    }

    override fun initData() {
        println(spUtil[myContext, "token", "noToken"])
        val testToken=spUtil[myContext, "token", "noToken"] as String
        if ("noToken" != testToken) {
            validateToken(testToken)
        }
    }
    fun validateToken(testToken: String){
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL + "/validateToken")
                .content("{\"token\":\"$testToken\"}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Accept", "application/json ")
                .addHeader("Content-Type", "application/json")
                .build()
                .execute(validateTokenCallback())
    }
    inner class validateTokenCallback():JSStringCallback(){
        override fun onResponse(response: String?, id: Int) {
            val gson = Gson()
            val jsonType = object : TypeToken<ApiResult<Boolean>>() {}.type
            var result = gson.fromJson<ApiResult<Boolean>>(response, jsonType)
            if (result.data!!){
               val  testToken=spUtil[myContext, "token", "noToken"] as String
                JSConfig.token="Bearer "+testToken
                JSConfig.userId=spUtil[myContext, "userId", "null"] as String
                startActivity(Intent(myContext, MainActivity::class.java))
            }
        }

    }
    private fun validate(){
        if( nameInput.text!=null&&passInput.text!=null){
            errText.visibility=View.GONE
            login(nameInput.text.toString(),passInput.text.toString())
        }else{
            errText.visibility=View.VISIBLE
        }
    }

    private fun login(name:String, pass:String) {
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL + "/login")
                .content("{\"name\":\"$name\",\"password\": \"$pass\"}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Accept", "application/json ")
                .addHeader("Content-Type", "application/json")
                .build()
                .execute(MyStringCallback())
    }

    inner class MyStringCallback : JSStringCallback() {
        override fun onResponse(response: String, id: Int) {
            val gson= Gson()
            val jsonType = object : TypeToken<ApiResult<User>>() {}.type
            val result = gson.fromJson<ApiResult<User>>(response, jsonType)
            if (result.retCode !="0000") {
                errText.visibility=View.VISIBLE
            } else {
                spUtil.put(myContext,"token",result.data!!.apiToken!!)
                spUtil.put(myContext,"userId", result.data!!.id.toString())
                JSConfig.token="Bearer " + result.data!!.apiToken!!
                JSConfig.userId= result.data!!.id.toString()
                startActivity(Intent(myContext, MainActivity::class.java))
            }
        }
    }
    override fun onStop() {
        super.onStop()
        finish()
    }
    override fun initTitle(): String {
        return "登录"
    }

    override fun initContentView(): Int {
        return R.layout.activity_login
    }

}