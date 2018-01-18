package c.example.test.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import c.example.test.R
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
        val imageButton = findViewById<ImageButton>(R.id.imageButton)
        imageButton.setImageDrawable(resources.getDrawable(R.mipmap.ic_user))
        initMenu()
        initTodayTask()
    }
    fun initMenu(){
        val serviceGrid = findViewById<GridView>(R.id.gv_menu)
        val form: Array<String> = arrayOf("crad_text")
        val int_array: IntArray = intArrayOf(R.id.crad_text)
        val sim_adapter: SimpleAdapter = SimpleAdapter(this, getMenu(), R.layout.layout_card, form, int_array)
        serviceGrid.setAdapter(sim_adapter)
        serviceGrid!!.setOnItemClickListener { _, _, position, _ ->
            when (position){
                0 -> startActivity(Intent(this, MapActivity::class.java))
                1 -> startActivity(Intent(this, TaskActivity::class.java))
                2 -> startActivity(Intent(this, ReportActivity::class.java))
                3 -> startActivity(Intent(this, EventRecordActivity::class.java))
                4 -> startActivity(Intent(this, ScheduleActivity::class.java))
                5 -> startActivity(Intent(this, LawActivity::class.java))
                6 -> startActivity(Intent(this, CommunicationListActivity::class.java))
            }
        }
    }

    fun initTodayTask(){
        val taskListView = findViewById<ListView>(R.id.lv_task)
        val form_task: Array<String> = arrayOf("ib_task_type", "tv_task_title")
        val to_task: IntArray = intArrayOf(R.id.ib_task_type, R.id.tv_task_title)
        val sim_adapter_task: SimpleAdapter = SimpleAdapter(this, formatlist_task(), R.layout.layout_listview_item, form_task, to_task)
        taskListView.setAdapter(sim_adapter_task)

        taskListView!!.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, "taskListView" + position, Toast.LENGTH_SHORT).show();
            startActivity(Intent(this, MapActivity::class.java))
        }
    }

    fun formatlist_task(): ArrayList<Map<String, Any>> {
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
}

