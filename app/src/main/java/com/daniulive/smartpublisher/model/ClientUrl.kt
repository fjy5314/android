package com.daniulive.smartpublisher.model

import com.google.gson.annotations.SerializedName

/**
 * Created by flny on 2018/3/30.
 */
data class ClientUrl(
        @SerializedName("clientRTMPAdress")  var videoUrl :String,
        @SerializedName("centerRTMPAdress") var audioUrl :String
)