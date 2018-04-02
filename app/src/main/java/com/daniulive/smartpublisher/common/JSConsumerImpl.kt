package com.daniulive.smartpublisher.common

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Consumer
import com.rabbitmq.client.Envelope
import com.rabbitmq.client.ShutdownSignalException

/**
 * Created by flny on 2018/3/28.
 */
open class  JSConsumerImpl : Consumer{
    override fun handleCancelOk(consumerTag: String) {
        println("handleCancelOk- - - - - - - - --  -- - - -- ")
        println(consumerTag)
    }
    //        接收到的数据
    override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {
        println("handleDelivery- - - - - - - - --  -- - - --")
        println(consumerTag)
        var ss=body.toString(Charsets.UTF_8)
        println("got message: " + ss)

    }

    override fun handleCancel(consumerTag: String) {
        println("handleCancel- - - - - - - - --  -- - - --")
        println(consumerTag)

    }

    override fun handleShutdownSignal(consumerTag: String, sig: ShutdownSignalException) {
        println("handleShutdownSignal- - - - - - - - --  -- - - --")
        println(consumerTag)

    }

    override fun handleConsumeOk(consumerTag: String) {
        println("handleConsumeOk- - - - - - - - --  -- - - --")
        println(consumerTag)

    }

    override fun handleRecoverOk(consumerTag: String?) {
        println("handleRecoverOk- - - - - - - - --  -- - - --")
        println(consumerTag)

    }

}