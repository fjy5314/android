package com.daniulive.smartpublisher.model

import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

class ApiResult<T>(
        @SerializedName("retCode") var retCode: String?,
        @SerializedName("retMsg") var retMsg: String?,
        @SerializedName("data") var data: T?

)
