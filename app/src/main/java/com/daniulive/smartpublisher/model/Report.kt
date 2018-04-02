package com.daniulive.smartpublisher.model
import com.google.gson.annotations.SerializedName


/**
 * Created by flny on 2018/1/19.
 */

data class Report(
		@SerializedName("id") var id: Int?,
		@SerializedName("title") var title: String?,
		@SerializedName("type") var type: String?,
		@SerializedName("status") var status: String?,
		@SerializedName("startTime") var startTime: String?,
		@SerializedName("deadline") var deadline: String?,
		@SerializedName("remark") var remark: String?,
		@SerializedName("description") var description: String?,
		@SerializedName("keywords") var keywords: String?,
		@SerializedName("content") var content: String?,
		@SerializedName("author") var author: String?,
		@SerializedName("userId") var userId: String?,
		@SerializedName("articleId") var articleId: String?,
		@SerializedName("deleted_at") var deletedAt: Any?,
		@SerializedName("created_at") var createdAt: String?,
		@SerializedName("updated_at") var updatedAt: String?,
		@SerializedName("user") var user: User?
)


