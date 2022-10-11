package com.alfionte.connectivitychange.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun Label(
    color: Color,
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(56.dp)
            .background(color, RoundedCornerShape(8.dp))
    ) {
        Text(
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colors.onPrimary,
            text = text
        )
    }
}