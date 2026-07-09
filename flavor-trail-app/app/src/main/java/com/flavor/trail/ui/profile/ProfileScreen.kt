package com.flavor.trail.ui.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Text(
        text = "个人中心页面",
        modifier = modifier.fillMaxSize().padding(16.dp)
    )
}