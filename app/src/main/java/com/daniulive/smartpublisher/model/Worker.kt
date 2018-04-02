package com.daniulive.smartpublisher.model

import com.google.gson.annotations.SerializedName

/**
 * Created by flny on 2018/1/30.
 */


data class Worker(
		@SerializedName("id") var id: Int?=0,
		@SerializedName("name") var name: String?="",
		@SerializedName("job") var job: String?="",
		@SerializedName("typeId") var typeId: String?="",
		@SerializedName("mobile1") var mobile1: String?="",
		@SerializedName("mobile2") var mobile2: String?="",
		@SerializedName("phone") var phone: String?="",
		@SerializedName("address") var address: String?="",
		@SerializedName("QQ") var qQ: String?="",
		@SerializedName("wechat") var wechat: String?="",
		@SerializedName("taxonomy") var taxonomy: Taxonomy?=null
)







