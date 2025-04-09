// ColorPalettePreview.kt
package com.example.dartadventure.ui.theme.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dartadventure.ui.theme.AccentOrange
import com.example.dartadventure.ui.theme.HighlightBlue
import com.example.dartadventure.ui.theme.HighlightOrange
import com.example.dartadventure.ui.theme.PrimaryBlue
import com.example.dartadventure.ui.theme.PrimaryTeal
import com.example.dartadventure.ui.theme.SecondaryBlue
import com.example.dartadventure.ui.theme.SecondaryTeal
import com.example.dartadventure.ui.theme.SecondaryYellow

@Composable
fun ColorPalettePreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Main Colors")
        Spacer(modifier = Modifier.height(8.dp))
        ColorSwatchRow(
            colors = listOf(PrimaryTeal, SecondaryYellow, PrimaryBlue),
            names = listOf("PrimaryTeal", "SecondaryYellow", "PrimaryBlue")
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Accent Colors")
        Spacer(modifier = Modifier.height(8.dp))
        ColorSwatchRow(
            colors = listOf(AccentOrange, SecondaryBlue, SecondaryTeal),
            names = listOf("AccentOrange", "SecondaryBlue", "SecondaryTeal")
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Additional Colors")
        Spacer(modifier = Modifier.height(8.dp))
        ColorSwatchRow(
            colors = listOf(HighlightOrange, HighlightBlue),
            names = listOf("HighlightOrange", "HighlightBlue")
        )
    }
}

@Composable
fun ColorSwatchRow(colors: List<Color>, names: List<String>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        colors.forEachIndexed { index, color ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(color)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(names[index])
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorPalettePreviewPreview() {
    ColorPalettePreview()
}