package com.daniulive.smartpublisher.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import com.daniulive.smartpublisher.R
import com.daniulive.smartpublisher.adapter.TodayTaskAdapter
import com.daniulive.smartpublisher.model.JSConfig
import com.daniulive.smartpublisher.model.Task
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.MediaType
import java.text.SimpleDateFormat
import java.util.*
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import com.baidu.mapapi.SDKInitializer
import com.daniulive.smartpublisher.common.JSStringCallback
import com.daniulive.smartpublisher.common.SPUtil
import com.daniulive.smartpublisher.model.ApiResult
import com.daniulive.smartpublisher.service.MessageService
import com.daniulive.smartpublisher.service.PositionService
import com.daniulive.smartpublisher.service.UploadService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rabbitmq.client.*
import okhttp3.Request


class MainActivity : BaseActivity() {
    var taskList = ArrayList<Task>()
    lateinit var listView: ListView
    lateinit var todoTask: TextView
    lateinit var doTask: TextView
    lateinit var serviceGrid: GridView
    lateinit var navigationView: NavigationView
    lateinit var messageService :Intent //消息service
    lateinit var intentOne :Intent  //消息定位
    lateinit var intentUpload :Intent  //消息定位

    override fun initView(savedInstanceState: Bundle?) {
        leftButton.setImageDrawable(resources.getDrawable(R.mipmap.ic_user))
        initMenu()
        todoTask = findViewById(R.id.tv_today_task_num1)
        doTask = findViewById(R.id.tv_today_task_num2)
        navigationView = findViewById(R.id.navigation_view)
        navigationView.itemIconTintList = null
        //获取头布局文件
        navigationView.getHeaderView(0)
        //设置菜单监听
        navigationView.setNavigationItemSelectedListener { item ->
            //在这里处理item的点击事件
            when (item.itemId) {
                R.id.item_main -> Log.i("clickItem", item.itemId.toString())
                R.id.item_set -> Log.i("clickItem", item.itemId.toString())
                R.id.item_logout -> startActivity(Intent(this,LoginActivity::class.java))
            }
            true
        }
        findViewById<ImageButton>(R.id.ib_shoot_button).setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
        messageService= Intent(this,MessageService::class.java)
        startService(messageService)
        intentOne = Intent(this, PositionService::class.java)
        startService(intentOne)
        intentUpload = Intent(this, UploadService::class.java)
        startService(intentUpload)
        showTodayTasks()
    }


    override fun onClickLeftButton() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.openDrawer(Gravity.LEFT)
    }


//初始化菜单
    fun initMenu() {
        serviceGrid = findViewById(R.id.gv_menu)
        val form: Array<String> = arrayOf("tv_menu_text", "iv_menu_icon")
        val intArray: IntArray = intArrayOf(R.id.tv_menu_text, R.id.iv_menu_icon)
        val simAdapter: SimpleAdapter = SimpleAdapter(this, getMenu(), R.layout.layout_card, form, intArray)
        serviceGrid.adapter = simAdapter
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


    fun getMenu(): ArrayList<Map<String, Any>> {
        var dataList = ArrayList<Map<String, Any>>()
        var map: Map<String, Any>? = null
        var type = intArrayOf(R.mipmap.ic_menu_map, R.mipmap.ic_menu_task, R.mipmap.ic_menu_report, R.mipmap.ic_menu_event, R.mipmap.ic_menu_schedule, R.mipmap.ic_menu_law, R.mipmap.ic_menu_contact)
        val titles = arrayOf("地图导航", "我的任务", "工作报告", "事件记录", "我的日程", "法律法规", "通讯录")

        for (i in titles.indices) {
            map = HashMap<String, Any>()
            map.put("tv_menu_text", titles[i])
            map.put("iv_menu_icon", type[i])
            dataList.add(map)
        }

        return dataList
    }
// 今日任务
    @SuppressLint("SimpleDateFormat")
    fun showTodayTasks() {
        val df = SimpleDateFormat("yyyy-MM-dd")
        OkHttpUtils
                .postString()
                .url(JSConfig.BASE_URL + "/biz/task/list")
                .content("{\"status\":null,\"taskDate\":\"" + df.format(Date(System.currentTimeMillis())) + "\"}")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Authorization", JSConfig.token)
                .addHeader("Accept", "application/json ")
                .addHeader("Content-Type", "application/json")
                .build()
                .execute(MyStringCallback())
    }

    inner class MyStringCallback : JSStringCallback() {
        override fun onResponse(response: String, id: Int) {
            val gson: Gson = Gson()
            val jsonType = object : TypeToken<ApiResult<ArrayList<Task>>>() {}.type
            var result = gson.fromJson<ApiResult<ArrayList<Task>>>(response, jsonType)
            taskList.clear()
            taskList = result.data!!
            var num1 = 0
            var num2 = 0
            for (i in taskList.indices) {
                if (taskList[i].status == 0) {
                    num2 += 1
                } else {
                    num1 += 1
                }
            }
            todoTask.text = ("待完成" + num1)
            doTask.text = ("已完成" + num2)
            listView = findViewById(R.id.lv_task)

            listView.adapter = TodayTaskAdapter(taskList, baseContext, Intent(myContext, TaskInfoActivity::class.java), true)

        }
    }
    override fun initMap() {
        SDKInitializer.initialize(this.applicationContext)
    }
    override fun initContentView(): Int {
        return R.layout.activity_main
    }

    override fun initTitle(): String {
        return "单兵执法系统"
    }

    override fun onDestroy() {
        stopService(messageService)
        super.onDestroy()
    }
    //动态申请摄像头 录音权限 GPS权限
    override fun onResume() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            //动态申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
                    1)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
        }
        super.onResume()

    }

    //申请权限回调处理
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        doNext(requestCode, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    //具体的操作
    fun doNext(requestCode: Int, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();
                Toast.makeText(this, """请在应用管理中打开相机以及录像访问权限！""", Toast.LENGTH_LONG).show();
                finish()
            }
        }
    }
}

