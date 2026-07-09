package com.flavor.trail.data.api

import com.flavor.trail.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<LoginResponse>>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @GET("api/user/profile")
    suspend fun getProfile(): Response<ApiResponse<UserInfo>>

    @PUT("api/user/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ApiResponse<Void>>

    @GET("api/provinces")
    suspend fun getProvinces(): Response<ApiResponse<List<Province>>>

    @GET("api/foods/province/{provinceId}")
    suspend fun getProvinceFoods(
        @Path("provinceId") provinceId: Long,
        @Query("pageNum") pageNum: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): Response<ApiResponse<List<Food>>>

    @GET("api/foods/{id}")
    suspend fun getFoodDetail(@Path("id") id: Long): Response<ApiResponse<Food>>

    @GET("api/foods/search")
    suspend fun searchFoods(@Query("keyword") keyword: String): Response<ApiResponse<List<Food>>>

    @POST("api/foods/{id}/collect")
    suspend fun toggleCollect(@Path("id") id: Long): Response<ApiResponse<CollectResult>>

    @POST("api/foods/{id}/view")
    suspend fun recordView(@Path("id") id: Long): Response<ApiResponse<Void>>

    @GET("api/foods/ranking")
    suspend fun getRanking(@Query("type") type: String = "collect"): Response<ApiResponse<List<Food>>>

    @GET("api/explore/progress")
    suspend fun getExploreProgress(): Response<ApiResponse<List<ExploreProgress>>>

    @GET("api/explore/stats")
    suspend fun getExploreStats(): Response<ApiResponse<ExploreStats>>

    @POST("api/chat/sessions")
    suspend fun createSession(@Body request: CreateSessionRequest): Response<ApiResponse<ChatSession>>

    @GET("api/chat/sessions")
    suspend fun getSessions(
        @Query("pageNum") pageNum: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): Response<ApiResponse<List<ChatSession>>>

    @GET("api/chat/sessions/{id}/messages")
    suspend fun getMessages(@Path("id") sessionId: Long): Response<ApiResponse<List<ChatMessage>>>

    @POST("api/chat/sessions/{id}/send")
    suspend fun sendMessage(
        @Path("id") sessionId: Long,
        @Body request: SendMessageRequest
    ): Response<retrofit2.ResponseBody>

    @DELETE("api/chat/sessions/{id}")
    suspend fun deleteSession(@Path("id") sessionId: Long): Response<ApiResponse<Void>>
}