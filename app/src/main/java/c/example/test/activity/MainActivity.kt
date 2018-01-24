package c.example.test.activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import c.example.test.R
import c.example.test.model.ApiResult
import c.example.test.model.BaseCard
import com.google.gson.Gson
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Request
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity() {

    override fun initContentView(): Int {
        return R.layout.activity_main
    }

    override fun initTitle(): String {
        return "单兵执法系统"
    }

    override fun initView(savedInstanceState: Bundle?) {
        leftButton.setImageDrawable(resources.getDrawable(R.mipmap.ic_user))
        initMenu()
        initTodayTask()

        findViewById<ImageButton>(R.id.ib_shoot_button).setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
        show()
    }

    override fun onClickLeftButton() {
        val popup = PopupMenu(this, leftButton)
        popup.menuInflater.inflate(R.menu.menu_user, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            // TODO

            return@setOnMenuItemClickListener true
        }
        popup.show()

    }


    fun show() {
        OkHttpUtils
                .postString()
                .url("http://114.115.129.48:1650/api/device/currentList")
                .content("{\"siteTaxonomy\":{\"taxonomyId\": 1}}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(MyStringCallback())
    }

    fun initMenu() {
        val serviceGrid = findViewById<GridView>(R.id.gv_menu)
        val form: Array<String> = arrayOf("crad_text")
        val int_array: IntArray = intArrayOf(R.id.crad_text)
        val sim_adapter: SimpleAdapter = SimpleAdapter(this, getMenu(), R.layout.layout_card, form, int_array)
        serviceGrid.setAdapter(sim_adapter)
        serviceGrid!!.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> startActivity(Intent(this, MapActivity::class.java))
                1 -> startActivity(Intent(this, TaskActivity::class.java))
                2 -> startActivity(Intent(this, ReportActivity::class.java))
                3 -> startActivity(Intent(this, EventRecordActivity::class.java))
                4 -> startActivity(Intent(this, ScheduleActivity::class.java))
                5 -> startActivity(Intent(this, LawActivity::class.java))
                6 -> startActivity(Intent(this, ContactListActivity::class.java))
            }
        }
    }

    fun initTodayTask() {
        val taskListView = findViewById<ListView>(R.id.lv_task)
        val formTask: Array<String> = arrayOf("ib_task_type", "tv_task_title")
        val toTask: IntArray = intArrayOf(R.id.ib_task_type, R.id.tv_task_title)
        val simAdapterTask: SimpleAdapter = SimpleAdapter(this, formatlistTask(), R.layout.layout_listview_item, formTask, toTask)
        taskListView.setAdapter(simAdapterTask)

        taskListView!!.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, "taskListView" + position, Toast.LENGTH_SHORT).show();
            startActivity(Intent(this, MapActivity::class.java))
        }
    }

    fun formatlistTask(): ArrayList<Map<String, Any>> {
        var data_list = ArrayList<Map<String, Any>>()
        var map: Map<String, Any>? = null
        val titles = arrayOf("地图导航", "我的任务", "工作报告", "事件记录", "我的日程", "法律法规", "通讯录")

        for (i in titles.indices) {
            map = HashMap<String, Any>()
            map.put("tv_task_title", titles[i])
            map.put("ib_task_type", R.mipmap.ic_bell)
            data_list.add(map)
        }

        return data_list
    }

    fun getMenu(): ArrayList<Map<String, Any>> {
        var data_list = ArrayList<Map<String, Any>>()
        var map: Map<String, Any>? = null
        val titles = arrayOf("地图导航", "我的任务", "工作报告", "事件记录", "我的日程", "法律法规", "通讯录")
        for (i in titles.indices) {
            map = HashMap<String, Any>()
            map.put("crad_text", titles[i])
            data_list.add(map)
        }

        return data_list
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
            val result = gson.fromJson<ApiResult<BaseCard>>(response, ApiResult::class.java)
            System.out.println(result)
            when (id) {
                100 -> Toast.makeText(this@MainActivity, "http", Toast.LENGTH_SHORT).show()
                101 -> Toast.makeText(this@MainActivity, "https", Toast.LENGTH_SHORT).show()
            }
        }

        override fun inProgress(progress: Float, total: Long, id: Int) {
        }
    }


}

