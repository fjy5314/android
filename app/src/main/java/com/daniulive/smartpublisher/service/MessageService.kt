package com.daniulive.smartpublisher.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.daniulive.smartpublisher.common.JSConsumerImpl
import com.rabbitmq.client.ConnectionFactory

/**
 * Created by flny on 2018/3/28.
 */
class  MessageService :Service(){
    var conx = ConnectionFactory()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        conx.host = "114.115.129.48"
        conx.port = 5672
        conx.username = "video_push"
        conx.password = "trosent@2018151951420"
        conx.virtualHost = "/video_push"
        getMessage()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun getMessage(){
        object : Thread() {
            override fun run() {
                val connection = conx.newConnection() //建立链接
                val channel = connection.createChannel()
                //val declareOk = channel?.exchangeDeclare("amq.direct", "direct", true)
                val queueDeclare = channel.queueDeclare("queue.msg", false, false, false, null)
                val assignedQueueName = queueDeclare.queue
                channel.queueBind(assignedQueueName, "amq.direct", "msg_center")
                channel.basicConsume(assignedQueueName, false, JSConsumerImpl())
                println("consumer listening")
            }
        }.start()
    }

}