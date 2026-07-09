package com.flavor.trail.data.model

import com.google.gson.annotations.SerializedName

data class Province(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String,
    @SerializedName("description") val description: String?,
    @SerializedName("sortOrder") val sortOrder: Int,
    @SerializedName("isExplored") val isExplored: Boolean
)

data class Food(
    @SerializedName("id") val id: Long,
    @SerializedName("provinceId") val provinceId: Long,
    @SerializedName("provinceName") val provinceName: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("ingredients") val ingredients: List<String>,
    @SerializedName("steps") val steps: List<String>,
    @SerializedName("coverUrl") val coverUrl: String?,
    @SerializedName("videoUrl") val videoUrl: String?,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("viewCount") val viewCount: Int,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("collectCount") val collectCount: Int,
    @SerializedName("avgRating") val avgRating: Float,
    @SerializedName("isCollected") val isCollected: Boolean
)

data class CollectResult(
    @SerializedName("isCollected") val isCollected: Boolean,
    @SerializedName("collectCount") val collectCount: Int
)