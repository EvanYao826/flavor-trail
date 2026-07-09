package com.flavor.trail.ui.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    Text(
        text = "AI对话页面",
        modifier = modifier.fillMaxSize().padding(16.dp)
    )
}