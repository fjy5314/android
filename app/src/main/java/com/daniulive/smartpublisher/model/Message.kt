package com.daniulive.smartpublisher.model
import com.google.gson.annotations.SerializedName


/**
 * Created by flny on 2018/2/1.
 */

data class Message(
		@SerializedName("id") var id: Int?,
		@SerializedName("title") var title: String?,
		@SerializedName("brief") var brief: String?,
		@SerializedName("content") var content: String?,
		@SerializedName("typeId") var typeId: String?,
		@SerializedName("notifyTime") var notifyTime: String?,
		@SerializedName("sendUserId") var sendUserId: String?,
		@SerializedName("notifyWay") var notifyWay: String?,
		@SerializedName("deleted_at") var deletedAt: Any?,
		@SerializedName("created_at") var createdAt: String?,
		@SerializedName("updated_at") var updatedAt: String?,
		@SerializedName("user") var user: User?,
		@SerializedName("notify_detail") var notifyDetail: NotifyDetail?,
		@SerializedName("taxonomy") var taxonomy: Taxonomy?,
		@SerializedName("isRead") var isRead: Int?
)



