package com.daniulive.smartpublisher.model
import com.google.gson.annotations.SerializedName


/**
 * Created by flny on 2018/2/1.
 */

data class EventRecord(
		@SerializedName("id") var id: Int?,
		@SerializedName("title") var title: String?,
		@SerializedName("type") var type: Int?,
		@SerializedName("contents") var contents: String?,
		@SerializedName("occurTime") var occurTime: String?,
		@SerializedName("picUrl") var picUrl: String?,
		@SerializedName("remark") var remark: String?,
		@SerializedName("author") var author: String?,
        @SerializedName("deleted_at") var deletedAt: Any?,
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?,
        @SerializedName("user") var user: User?

)

