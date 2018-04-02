package com.daniulive.smartpublisher.model
import com.google.gson.annotations.SerializedName


/**
 * Created by flny on 2018/1/16.
 */


data class Law(
		@SerializedName("id") var id: Int?,
		@SerializedName("title") var title: String?,
		@SerializedName("link") var link: String?,
		@SerializedName("keywords") var keywords: String?,
		@SerializedName("description") var description: String?,
		@SerializedName("image") var image: String?,
		@SerializedName("remark") var remark: String?,
		@SerializedName("typeName") var typeName: String?,
		@SerializedName("typeId") var typeId: Int?,
		@SerializedName("content") var content: String?
)

