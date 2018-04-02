package com.daniulive.smartpublisher.common

import com.google.gson.Gson
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.Request
import java.lang.Exception

/**
 * Created by flny on 2018/2/26.
 */
abstract class JSStringCallback :StringCallback(){
    override fun onError(call: Call?, e: Exception, id: Int) {
          errToDo(e)
    }

    override fun onBefore(request: Request, id: Int) {
        onbeforeTodo(request)
    }


    override fun onAfter(id: Int) {
    }
    open fun onbeforeTodo(request: Request){}

    open fun errToDo(e: Exception){}
}