package com.daniulive.smartpublisher.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import  com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.adapter.BaseCardKeyAdapter
import com.daniulive.smartpublisher.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Request
import org.angmarch.views.NiceSpinner
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by flny on 2018/1/15.
 */
class EventRecordActivity : BaseActivity() {
    private var eventRecordList = ArrayList<BaseCard>()
    lateinit var addBtn: ImageButton
    lateinit var listView: ListView
    lateinit var eventAdapt: BaseCardKeyAdapter
    var index: Int = 0
    override fun initView(savedInstanceState: Bundle?) {
        addBtn = findViewById(R.id.tv_event_record_add)
        addBtn.setOnClickListener {
            var intent = Intent(this, EventInfoActivity::class.java)
            intent.putExtra("id", -1)
            startActivity(intent)
        }
        listView = findViewById(R.id.lv_task)
        eventAdapt = BaseCardKeyAdapter(eventRecordList, baseContext, false, Intent(this, EventInfoActivity::class.java))
        listView.adapter = eventAdapt
        initSpinner()
    }

    fun initSpinner() {
        val niceSpinner = findViewById<View>(R.id.nice_spinner) as NiceSpinner
        val dataset = LinkedList(Arrays.asList("全部群組", "火险", "水淹", "故障", "其他"))
        niceSpinner.attachDataSource<String>(dataset)

        niceSpinner.addOnItemClickListener { _, _, position, _ ->
            //TODO
            index = position
            initParam()
        }

    }

    override fun initData() {
        getEventList("{\"oneKeySearch\":null,\"type\":null,\"startTime\":null,\"endTime\":null}")
    }

    fun initParam() {
        var type = if (index == 0) null else index
        getEventList("{\"oneKeySearch\":null,\"type\":" + type + ",\"startTime\":null,\"endTime\":null}")
    }

    fun getEventList(str: String) {

        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL + "/biz/event/list")
                .content(str)
                .mediaType(MediaType.parse("application/json; charset=utf-8,"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept", "application/json ")
                .addHeader("Content-Type", "application/json")
                .build()
                .execute(MyStringCallback())
    }

    inner class MyStringCallback : StringCallback() {
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

            val jsonType = object : TypeToken<ApiResult<ArrayList<EventRecord>>>() {

            }.type
            var result = gson.fromJson<ApiResult<ArrayList<EventRecord>>>(response, jsonType)
            eventRecordList.clear()
            var xx = SimpleDateFormat("yyyy-MM-dd")
            for (i in result.data!!.indices) {
                var date = xx.parse(result.data!![i].occurTime)
                eventRecordList.add(BaseCard(result.data!![i].id, R.mipmap.ic_bell, result.data!![i].title, "记录时间: " + result.data!![i].occurTime, (date.year + 1900).toString() + "/" + (date.month + 1).toString()))
            }
            eventAdapt.notifyDataSetInvalidated()
        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }


    override fun initTitle(): String {
        return "事件记录"
    }

    override fun initContentView(): Int {
        return R.layout.activity_event_record
    }
}