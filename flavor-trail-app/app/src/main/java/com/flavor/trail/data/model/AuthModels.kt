package com.flavor.trail.data.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("nickname") val nickname: String? = null
)

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("userInfo") val userInfo: UserInfo
)

data class UserInfo(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("gender") val gender: Int
)

data class UpdateProfileRequest(
    @SerializedName("nickname") val nickname: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("gender") val gender: Int? = null
)