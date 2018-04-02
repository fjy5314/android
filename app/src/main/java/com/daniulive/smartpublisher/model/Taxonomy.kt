package com.daniulive.smartpublisher.model

import com.google.gson.annotations.SerializedName

/**
 * Created by flny on 2018/1/30.
 */
data class Taxonomy(
        @SerializedName("id") var id: Int?,
        @SerializedName("name") var name: String?,
        @SerializedName("parent_id") var parentId: String?,
        @SerializedName("type") var type: String?,
        @SerializedName("deleted_at") var deletedAt: Any?,
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?
)