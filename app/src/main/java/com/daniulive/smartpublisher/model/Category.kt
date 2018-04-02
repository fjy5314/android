package com.daniulive.smartpublisher.model

import com.google.gson.annotations.SerializedName

/**
 * Created by flny on 2018/1/16.
 */
data class Category(
        @SerializedName("id") var id: Int?,
        @SerializedName("parentId") var parentId: String?,
        @SerializedName("parentIds") var parentIds: String?,
        @SerializedName("name") var name: String?,
        @SerializedName("image") var image: String?,
        @SerializedName("siteId") var siteId: String?,
        @SerializedName("officeId") var officeId: String?,
        @SerializedName("module") var module: String?,
        @SerializedName("href") var href: String?,
        @SerializedName("target") var target: String?,
        @SerializedName("description") var description: String?,
        @SerializedName("keywords") var keywords: String?,
        @SerializedName("sort") var sort: String?,
        @SerializedName("inMenu") var inMenu: String?,
        @SerializedName("inList") var inList: String?,
        @SerializedName("showModes") var showModes: String?,
        @SerializedName("allowComment") var allowComment: String?,
        @SerializedName("isAudit") var isAudit: String?,
        @SerializedName("customListView") var customListView: String?,
        @SerializedName("customContentView") var customContentView: String?,
        @SerializedName("viewConfig") var viewConfig: String?,
        @SerializedName("remark") var remark: String?,
        @SerializedName("createBy") var createBy: String?,
        @SerializedName("updateBy") var updateBy: String?,
        @SerializedName("deleted_at") var deletedAt: Any?,
        @SerializedName("created_at") var createdAt: String?,
        @SerializedName("updated_at") var updatedAt: String?
)