package com.daniulive.smartpublisher.model

import com.google.gson.annotations.SerializedName

/**
 * Created by flny on 2018/2/1.
 */
data class NotifyDetail(
        @SerializedName("id") var id: Int?,
        @SerializedName("receiveUserId") var receiveUserId: String?,
        @SerializedName("notifyId") var notifyId: String?,
        @SerializedName("notifyStatus") var notifyStatus: String?,
        @SerializedName("receiveTime") var receiveTime: String?,
        @SerializedName("isRead") var isRead: String?,
        @SerializedName("deleted_at") var deletedAt: Any?,
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?
)
