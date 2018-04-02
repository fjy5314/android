package com.daniulive.smartpublisher.model
import com.google.gson.annotations.SerializedName


/**
 * Created by flny on 2018/1/16.
 */


data class Task(
		@SerializedName("id") var id: Int?,
		@SerializedName("title") var title: String?,
		@SerializedName("type") var type: Int?,
		@SerializedName("lng") var lng: Any?,
		@SerializedName("lat") var lat: Any?,
		@SerializedName("status") var status: Int?,
		@SerializedName("description") var description: String?,
		@SerializedName("remark") var remark: String?,
		@SerializedName("startTime") var startTime: String?,
		@SerializedName("deadline") var deadline: String?,
		@SerializedName("finishTime") var finishTime: String?,
		@SerializedName("parentId") var parentId: String?,
		@SerializedName("areaId") var areaId: String?,
		@SerializedName("userId") var userId: String?,
		@SerializedName("deleted_at") var deletedAt: Any?,
		@SerializedName("created_at") var createdAt: String?,
		@SerializedName("updated_at") var updatedAt: String?,
		@SerializedName("user") var user: User?,
		@SerializedName("area") var area: List<Area?>?,
		@SerializedName("car_used") var carUsed: Any?,
		@SerializedName("resource_repair") var resourceRepair: Any?,
		@SerializedName("areaName") var areaName: String?,
		@SerializedName("userName") var userName: String?
)
