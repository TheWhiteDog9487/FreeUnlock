package xyz.thewhitedog9487.freeunlock.UI.Composable

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun LinkRow(LeftIcon: @Composable () -> Unit,
            vararg Lines: String?,
            modifier: Modifier = Modifier,
            FirstLineUseDifferentColor: Boolean = false,
            TextColor: Color? = null,
            TargetLinkURL: String? = null,
            ActivityContext: Context? = null,
            RightIcon: (@Composable () -> Unit)? = null ){

    val Texts = Lines
        .filterNotNull()
        .toMutableList()
    val UseDifferentStyle = FirstLineUseDifferentColor && Texts.size > 1

    Row(modifier = modifier
        .clickable(enabled = TargetLinkURL != null && ActivityContext != null,
            onClick = {
                if (TargetLinkURL != null && ActivityContext != null) {
                    runCatching {
                        ActivityContext.startActivity(Intent(Intent.ACTION_VIEW).apply {
                            data = TargetLinkURL.toUri() }) } } } ),
        verticalAlignment = Alignment.CenterVertically) {

        LeftIcon()
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Column {
            if (UseDifferentStyle){
                Text(text = Texts.removeAt(0),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextColor ?: MaterialTheme.colorScheme.onSurface) }
            Texts.forEach {
                Text(text = it,
                    style = if (UseDifferentStyle) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium,
                    color = TextColor ?: if (UseDifferentStyle) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface) } }
        Spacer(modifier = Modifier.weight(1f))
        RightIcon?.invoke() } }