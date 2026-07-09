package com.flavor.trail.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CreateSessionRequest(
    @SerializedName("type") val type: String = "general"
)

data class SendMessageRequest(
    @SerializedName("content") val content: String
)

data class ChatSession(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String,
    @SerializedName("updatedAt") val updatedAt: LocalDateTime,
    @SerializedName("messageCount") val messageCount: Int
)

data class ChatMessage(
    @SerializedName("id") val id: Long,
    @SerializedName("sessionId") val sessionId: Long,
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String,
    @SerializedName("metadata") val metadata: Map<String, Any>?,
    @SerializedName("createdAt") val createdAt: LocalDateTime
)