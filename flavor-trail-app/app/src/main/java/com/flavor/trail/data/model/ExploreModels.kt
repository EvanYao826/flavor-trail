package com.flavor.trail.data.model

import com.google.gson.annotations.SerializedName

data class ExploreProgress(
    @SerializedName("provinceId") val provinceId: Long,
    @SerializedName("provinceName") val provinceName: String,
    @SerializedName("isExplored") val isExplored: Boolean,
    @SerializedName("foodViewedCount") val foodViewedCount: Int
)

data class ExploreStats(
    @SerializedName("exploredCount") val exploredCount: Int,
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("percentage") val percentage: Float
)