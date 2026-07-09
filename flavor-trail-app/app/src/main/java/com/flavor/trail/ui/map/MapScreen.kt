package com.flavor.trail.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    Text(
        text = "地图页面 - 中国美食地图",
        modifier = modifier.fillMaxSize().padding(16.dp)
    )
}