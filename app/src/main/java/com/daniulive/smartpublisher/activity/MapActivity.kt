package com.daniulive.smartpublisher.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.baidu.location.*
import com.baidu.mapapi.SDKInitializer
import  com.daniulive.smartpublisher.R
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.daniulive.smartpublisher.adapter.TodayTaskAdapter
import com.daniulive.smartpublisher.common.GPSPoint
import com.daniulive.smartpublisher.common.JSStringCallback
import com.daniulive.smartpublisher.model.ApiResult
import com.daniulive.smartpublisher.model.JSConfig
import com.daniulive.smartpublisher.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by flny on 2018/1/15.
 */
class MapActivity : BaseActivity() {
    lateinit var listBtn: ImageButton
    lateinit var mMapView: MapView
    lateinit var mBaiduMap: BaiduMap
    lateinit var showTask: LinearLayout
    var taskList = ArrayList<Task>()
    lateinit var listView: ListView
    lateinit var todoTask: TextView
    lateinit var doTask: TextView
    var isShow = false
    var positionOverlay :Overlay?=null //图层
    lateinit var mGPSPoint:GPSPoint //获取GPS点位
    var isToCenter=false //是否到达中心
    val myListener = MyLocationListener() //获取GPS点的回调
    override fun initView(savedInstanceState: Bundle?) {
        mMapView = findViewById(R.id.bmapView)
        showTask = findViewById(R.id.ll_map_show)
        listBtn = findViewById(R.id.ib_today_task_list)
        todoTask =findViewById(R.id.tv_today_task_num1)
        doTask =findViewById(R.id.tv_today_task_num2)
        mBaiduMap = mMapView.map
        mBaiduMap.mapType = BaiduMap.MAP_TYPE_NORMAL
        listBtn.setOnClickListener {
            isShow=!isShow
            if (isShow) {
                listBtn.setImageResource(R.mipmap.ic_list_selected)
                mMapView.visibility = View.GONE
                showTask.visibility = View.VISIBLE
            } else {
                listBtn.setImageResource(R.mipmap.ic_list_unselect)
                mMapView.visibility = View.VISIBLE
                showTask.visibility = View.GONE
            }
        }
        mGPSPoint=GPSPoint(applicationContext)
        mGPSPoint.getPoint(myListener,0)
        getTask()
    }

    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            val latitude = location.latitude    //获取纬度信息
            val longitude = location.longitude    //获取经度信息
            val radius = location.radius    //获取定位精度，默认值为0.0f
            val coorType = location.coorType
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            //定义Maker坐标点
            val errorCode = location.locType
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            val point = LatLng(latitude, longitude)
            makeMarker(point)
            if(!isToCenter){
                isToCenter=!isToCenter
                moveToCenter(point)

            }

        }
    }


    @SuppressLint("SimpleDateFormat")
    fun getTask() {
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
            System.out.println("成功了——————————————————————————")
            val gson: Gson = Gson()
            val jsonType = object : TypeToken<ApiResult<ArrayList<Task>>>() {

            }.type
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

            listView.adapter = TodayTaskAdapter(taskList, baseContext, Intent(myContext, TaskInfoActivity::class.java),false)

        }
    }


    fun makeMarker(point: LatLng){
        if(positionOverlay!=null){
            positionOverlay!!.remove()
        }

        //构建Marker图标
        val bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_position)
        //构建MarkerOption，用于在地图上添加Marker
        val option = MarkerOptions()
                .position(point)
                .icon(bitmap)
        //在地图上添加Marker，并显示
        positionOverlay=mBaiduMap.addOverlay(option)
    }

    fun  moveToCenter(point: LatLng){
        //定义地图状态
        var  mMapStatus : MapStatus = MapStatus.Builder()
                .target(point)
                .zoom(18F)
                .build()
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        var  mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus)
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate)
    }


    override fun initMap() {
        SDKInitializer.initialize(this.applicationContext)
    }

    override fun initTitle(): String {
        return "地图导航"
    }

    override fun initContentView(): Int {
        return R.layout.activity_map
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy()
        mGPSPoint.stop()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause()
    }
}
