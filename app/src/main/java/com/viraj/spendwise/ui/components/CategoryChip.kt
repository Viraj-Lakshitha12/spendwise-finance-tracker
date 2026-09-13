package com.viraj.spendwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.viraj.spendwise.ui.theme.getMappedCategoryColor

@Composable
fun CategoryChip(
    name: String,
    colorHex: String = "", // kept for backwards compatibility in caller signatures
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val baseColor = if (name.equals("All", ignoreCase = true)) {
        Color.Gray
    } else {
        getMappedCategoryColor(name, colorHex)
    }

    val backgroundColor = if (isSelected) baseColor else baseColor.copy(alpha = 0.2f)
    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = name,
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
