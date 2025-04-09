// ComponentCatalog.kt
package com.example.dartadventure.ui.theme.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dartadventure.ui.theme.DartAdventureTheme

@Composable
fun ComponentCatalog() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text("Headline Large", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Headline Small", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Body Large", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {}) {
            Text("Label Large Button", style = MaterialTheme.typography.labelLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Add more components and styles as needed, e.g.,
        // OutlinedButton, TextField, Card, etc.
    }
}

@Preview(showBackground = true)
@Composable
fun ComponentCatalogPreview() {
    DartAdventureTheme {
        ComponentCatalog()
    }
}