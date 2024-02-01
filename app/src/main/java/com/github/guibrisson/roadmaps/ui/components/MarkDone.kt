package com.github.guibrisson.roadmaps.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.guibrisson.roadmaps.R
import com.github.guibrisson.roadmaps.ui.theme.RoadmapsTheme
import com.github.guibrisson.roadmaps.ui.theme.green

@Composable
fun MarkDone(
    modifier: Modifier = Modifier,
    isDone: Boolean,
    onMarkDone: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier.clickable(
            enabled = !isDone,
            onClick = onMarkDone,
            interactionSource = interactionSource,
            indication = null,
        ),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val iconPainter = if (isDone) {
                painterResource(id = R.drawable.ic_check)
            } else {
                painterResource(id = R.drawable.ic_minus)
            }
            val textColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            val iconTint = if (isDone) green else textColor

            Text(text = "[", color = textColor)
            Icon(painter = iconPainter, contentDescription = null, tint = iconTint)
            Text(text = "]", color = textColor)
        }

        val text = if (isDone) "done" else "mark as done"
        Text(text = text)
    }
}

@Preview
@Composable
private fun PreviewMarkDoneTrue() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MarkDone(isDone = true) {

            }
        }
    }
}


@Preview
@Composable
private fun PreviewMarkDoneFalse() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MarkDone(isDone = false) {

            }
        }
    }
}