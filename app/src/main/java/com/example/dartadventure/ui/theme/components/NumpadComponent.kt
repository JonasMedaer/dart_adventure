package com.example.dartadventure.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NumpadComponent(
    onNumpadClick: (String) -> Unit,
    onClearClick: () -> Unit,
    buttonSize: Dp = 50.dp,
    spacing: Dp = 6.dp,
    isLandscape: Boolean
) {
    val landscapeButtonSize = 90.dp // Increased button size for landscape/tablet
    val landscapeSpacing = 10.dp
    val clearButtonColor = Color(0xFF444444)
    val clearButtonTextColor = Color.White

    if (isLandscape) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(landscapeSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(landscapeSpacing)) {
                Row(horizontalArrangement = Arrangement.spacedBy(landscapeSpacing)) {
                    NumpadButton(
                        text = "1",
                        onClick = onNumpadClick,
                        size = landscapeButtonSize,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                    NumpadButton(
                        text = "2",
                        onClick = onNumpadClick,
                        size = landscapeButtonSize,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                    NumpadButton(
                        text = "3",
                        onClick = onNumpadClick,
                        size = landscapeButtonSize,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(landscapeSpacing)) {
                    NumpadButton(
                        text = "4",
                        onClick = onNumpadClick,
                        size = landscapeButtonSize,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                    NumpadButton(
                        text = "5",
                        onClick = onNumpadClick,
                        size = landscapeButtonSize,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                    NumpadButton(
                        text = "6",
                        onClick = onNumpadClick,
                        size = landscapeButtonSize,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(landscapeSpacing)) {
                    NumpadButton(
                        text = "7",
                        onClick = onNumpadClick,
                        size = landscapeButtonSize,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                    NumpadButton(
                        text = "8",
                        onClick = onNumpadClick,
                        size = landscapeButtonSize,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                    NumpadButton(
                        text = "9",
                        onClick = onNumpadClick,
                        size = landscapeButtonSize,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(landscapeSpacing)) {
                Button(
                    onClick = onClearClick,
                    modifier = Modifier
                        .width(landscapeButtonSize)
                        .height(landscapeButtonSize),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = clearButtonColor)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            "X",
                            style = MaterialTheme.typography.titleMedium,
                            color = clearButtonTextColor
                        )
                    }
                }
                NumpadButton(
                    text = "0",
                    onClick = onNumpadClick,
                    size = landscapeButtonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
            }
        }
    } else {
        val clearButtonColorPortrait = Color(0xFF444444)
        val clearButtonTextColorPortrait = Color.White
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                NumpadButton(
                    text = "1",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
                NumpadButton(
                    text = "2",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
                NumpadButton(
                    text = "3",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                NumpadButton(
                    text = "4",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
                NumpadButton(
                    text = "5",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
                NumpadButton(
                    text = "6",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                NumpadButton(
                    text = "7",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
                NumpadButton(
                    text = "8",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
                NumpadButton(
                    text = "9",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                Button(
                    onClick = onClearClick,
                    modifier = Modifier
                        .width(buttonSize)
                        .height(buttonSize),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = clearButtonColorPortrait)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            "X",
                            style = MaterialTheme.typography.titleMedium,
                            color = clearButtonTextColorPortrait
                        )
                    }
                }
                NumpadButton(
                    text = "0",
                    onClick = onNumpadClick,
                    size = buttonSize,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(buttonSize))
            }
        }
    }
}

@Composable
private fun NumpadButton(
    text: String,
    onClick: (String) -> Unit,
    size: Dp = 60.dp,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall
) {
    Button(
        onClick = { onClick(text) },
        modifier = Modifier
            .width(size)
            .height(size),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text, style = textStyle)
        }
    }
}