package com.daniulive.smartpublisher.service

/**
 * Created by flny on 2018/3/30.
 */
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.daniulive.smartpublisher.common.GPSPoint
import com.daniulive.smartpublisher.model.JSConfig
import com.daniulive.smartpublisher.common.JSDB
import com.daniulive.smartpublisher.greendao.PointDao
import com.daniulive.smartpublisher.model.Point
import java.util.*

/**
 * Created by flny on 2018/3/30.
 */
class PositionService : Service() {
    lateinit var mLocationClient: GPSPoint
    private val myListener = JSLocationListener()
    lateinit var pointDao : PointDao

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    //仅执行一次
    override fun onCreate() {
        pointDao = JSDB().getPoint(applicationContext)
        mLocationClient = GPSPoint(applicationContext)
        mLocationClient.getPoint(myListener,6000)
        super.onCreate()
    }

    override fun onDestroy() {
        mLocationClient.stop()
        super.onDestroy()
    }

    inner class JSLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            val latitude = location.latitude    //获取纬度信息
            val longitude  = location.longitude    //获取经度信息
            val radius  = location.radius    //获取定位精度，默认值为0.0f
            val coorType = location.coorType
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            val errorCode = location.locType
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            pointDao.insert(Point(null, JSConfig.userId,latitude,longitude, Date()))
        }
    }


}