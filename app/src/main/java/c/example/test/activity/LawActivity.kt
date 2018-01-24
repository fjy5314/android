package c.example.test.activity

import android.content.Intent
import android.os.Bundle
import android.text.Selection
import android.util.Log
import  c.example.test.R
import c.example.test.model.ApiResult
import c.example.test.model.Law
import com.google.gson.Gson
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Request
import android.view.View
import android.widget.*
import java.util.Arrays.asList
import org.angmarch.views.NiceSpinner
import org.intellij.lang.annotations.JdkConstants
import java.util.*


/**
 * Created by flny on 2018/1/15.
 */
class LawActivity : BaseActivity() {
    private lateinit var lawList:ArrayList<Law>
    private lateinit var spinner: Spinner
    private lateinit  var adapter: ArrayAdapter<*>
    override fun initView(savedInstanceState: Bundle?) {
//        getLaw()
        initLawList()
        initSpinner()
    }
    fun getLaw(){
        OkHttpUtils
                .postString()
                .url("http://114.115.129.48:1650/api/device/currentList")
                .content("{\"siteTaxonomy\":{\"taxonomyId\": 1}}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(MyStringCallback())
    }
    fun initLawList (){
        val lawListView = findViewById<ListView>(R.id.lv_law)
        val formLaw: Array<String> = arrayOf("type", "title")
        val toLaw: IntArray = intArrayOf(R.id.tv_law_type, R.id.tv_law_title)
        val simAdapter: SimpleAdapter = SimpleAdapter(this, formatData(), R.layout.item_law_list_view, formLaw, toLaw)
        lawListView.setAdapter(simAdapter)
    }
    fun  initSpinner(){
        val niceSpinner = findViewById<View>(R.id.nice_spinner) as NiceSpinner
        val dataset = LinkedList(asList("xxx","xxxxxx"))
         niceSpinner.attachDataSource<String>(dataset)

        niceSpinner.addOnItemClickListener {_, _, position, _ ->
         //TODO

        }

    }
    fun formatData() : ArrayList<Map<String, Any>>{
        var dataList = ArrayList<Map<String, Any>>()
        var map: Map<String, Any>? = null
            map = HashMap<String, Any>()
            map.put("title", "一级救援指南")
            map.put("type", "水灾")
            dataList.add(map)
            dataList.add(map)
            dataList.add(map)
//        for (i in lawList.indices) {
//            map = HashMap<String, Any>()
//            map.put("title", lawList[i].name)
//            map.put("type", lawList[i].category.name)
//            dataList.add(map)
//        }

        return dataList
    }

    inner class MyStringCallback : StringCallback() {
        override fun onError(call: Call, e: java.lang.Exception?, id: Int) {
            System.out.println("出错了——————————————————————————")
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
            val result = gson.fromJson<ApiResult<ArrayList<Law>>>(response, ApiResult::class.java)
            lawList=result.data
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