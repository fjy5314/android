package com.daniulive.smartpublisher.model

import com.google.gson.annotations.SerializedName

/**
 * Created by flny on 2018/1/19.
 */

data class User(
        @SerializedName("id") var id: String?,
        @SerializedName("name") var name: String?,
        @SerializedName("code") var code: Any?,
        @SerializedName("status") var status: Any?,
        @SerializedName("phone") var phone: String?,
        @SerializedName("officeId") var officeId: Any?,
        @SerializedName("photo") var photo: Any?,
        @SerializedName("email") var email: String?,
        @SerializedName("deleted_at") var deletedAt: String?,
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?,
        @SerializedName("api_token") var apiToken: String?
)