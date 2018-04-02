package com.daniulive.smartpublisher.model

/**
 * Created by flny on 2018/1/19.
 */
data class BaseCard(
        var id: Int?,
        var type: Int,
        var title: String?=null,
        var endTime: String?=null,
        var key: String?=null,
        var content: String?=null

)