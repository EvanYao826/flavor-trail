package com.flavor.trail.util

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val tokenProvider: TokenProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = runBlocking { tokenProvider.getToken() }

        val newRequest = if (token.isNotEmpty()) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(newRequest)
    }
}

interface TokenProvider {
    suspend fun getToken(): String
}