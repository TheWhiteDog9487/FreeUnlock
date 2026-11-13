package xyz.thewhitedog9487.freeunlock.UI.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun StatusRow(
    Label: String,
    Value: String,
    ValueColor: Color? = null ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp) ) {
        Text(
            text = Label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium )
        Text(
            text = Value,
            modifier = Modifier.weight(1.4f),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            color = ValueColor ?: MaterialTheme.colorScheme.onSurfaceVariant ) } }