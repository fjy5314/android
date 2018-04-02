package com.daniulive.smartpublisher.model

import com.google.gson.annotations.SerializedName

/**
 * Created by flny on 2018/1/16.
 */

data class Area(
        @SerializedName("id") var id: Int?,
        @SerializedName("name") var name: String?,
        @SerializedName("sort") var sort: String?,
        @SerializedName("code") var code: String?,
        @SerializedName("workerId") var workerId: String?,
        @SerializedName("parentIds") var parentIds: String?,
        @SerializedName("parentId") var parentId: String?,
        @SerializedName("createBy") var createBy: String?,
        @SerializedName("updateBy") var updateBy: String?,
        @SerializedName("status") var status: Int?,
        @SerializedName("tag") var tag: Int?,
        @SerializedName("remark") var remark: String?,
        @SerializedName("deleted_at") var deletedAt: Any?,
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?
)