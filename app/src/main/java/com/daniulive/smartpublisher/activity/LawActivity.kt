package com.daniulive.smartpublisher.activity

import android.os.Bundle
import  com.daniulive.smartpublisher.R
import com.google.gson.Gson
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Request
import android.view.View
import android.widget.*
import com.daniulive.smartpublisher.model.*
import com.google.gson.reflect.TypeToken
import java.util.Arrays.asList
import org.angmarch.views.NiceSpinner
import java.util.*


/**
 * Created by flny on 2018/1/15.
 */
class LawActivity : BaseActivity() {
    private lateinit var lawList: ArrayList<Law>
    lateinit var lawListView: ListView
    lateinit var simAdapter: SimpleAdapter
    private lateinit var spinner: Spinner
    var dataList=ArrayList<Map<String, String>>()
    override fun initView(savedInstanceState: Bundle?) {
         lawListView = findViewById(R.id.lv_law)
        val formLaw: Array<String> = arrayOf("type", "title")
        val toLaw: IntArray = intArrayOf(R.id.tv_law_type, R.id.tv_law_title)
         simAdapter = SimpleAdapter(this, dataList, R.layout.item_law_list_view, formLaw, toLaw)
        lawListView.adapter = simAdapter
        initSpinner()
    }

    override fun initData() {
        getLawList("{\"oneKeySearch\":null,\"type\":null}")
    }
    fun initLawList (){

    }
    fun  initSpinner(){
        val niceSpinner = findViewById<View>(R.id.nice_spinner) as NiceSpinner
        val dataset = LinkedList(asList("全部类型","水灾"))
         niceSpinner.attachDataSource<String>(dataset)

        niceSpinner.addOnItemClickListener {_, _, position, _ ->
         //TODO
            Toast.makeText(this, "taskListView" + position, Toast.LENGTH_SHORT).show();
            when(position){
                0->    getLawList("{\"oneKeySearch\":null,\"type\":null}")
                1->    getLawList("{\"oneKeySearch\":\"Herr\",\"type\":null}")
            }

        }

    }

    fun getLawList(content :String ){
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL +"/biz/article/list")
                .content(content)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept","application/json ")
                .addHeader("Content-Type","application/json")
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

            val jsonType = object : TypeToken<ApiResult<ArrayList<Law>>>() {

            }.type
            var result = gson.fromJson<ApiResult<ArrayList<Law>>>(response, jsonType)
            dataList.clear()
        for (i in result.data!!.indices) {
            var map = HashMap<String, String>()
            map.put("title", result.data!![i].title!!)
            map.put("type", result.data!![i].typeName!!)
            dataList.add(map)
        }
            simAdapter.notifyDataSetChanged()
            initLawList()
        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }

    override fun initTitle(): String {
        return "法律法规"
    }

    override fun initContentView(): Int {
        return R.layout.activity_law
    }
}