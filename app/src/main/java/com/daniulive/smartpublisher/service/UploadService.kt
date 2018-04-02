package com.daniulive.smartpublisher.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.daniulive.smartpublisher.common.JSDB
import com.daniulive.smartpublisher.greendao.PointDao
import com.rabbitmq.client.ConnectionFactory
import java.util.*

class UploadService : Service(){
    var conx = ConnectionFactory()
    lateinit var pointDao : PointDao
    var mTimer : Timer = Timer(true) //定时器
    var  mTimerTask: TimerTask = object : TimerTask() {

        override fun run() {
            var point = pointDao.queryBuilder().build().list()
            if(point!=null){
//                PushPoint(point.userID,point.latitude,point.longitude)
//                pointDao.deleteByKey(point.id)
            }
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        conx.host = "114.115.129.48"
        conx.port = 5672
        conx.username = "video_push"
        conx.password = "trosent@2018151951420"
        conx.virtualHost = "/video_push"
        pointDao = JSDB().getPoint(applicationContext)
        mTimer.scheduleAtFixedRate(mTimerTask,0,2000)
        super.onCreate()
    }

    fun PushPoint(userId:String ,latitude: Double, longitude: Double) {
        var msg = "{\"userId\":\"$userId\",\"lng\":\"$longitude\",\"lat\":\"$latitude\"}"
        object : Thread() {
            override fun run() {
                var connection = conx.newConnection()
                var channel = connection.createChannel()
//                val declareOk = channel.exchangeDeclare("video.push.direct", "direct", false)
                channel.basicPublish("video.push.direct", "soldier.location", null, msg.toByteArray())
                connection.close()
            }
        }.start()

    }
}