package com.github.guibrisson.roadmaps.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.guibrisson.roadmaps.R

fun LazyListScope.failure(modifier: Modifier = Modifier, errorMessage: String) {
    item {
        Row(
            modifier = modifier.padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(id = R.string.error_signal),
                color = MaterialTheme.colorScheme.error,
            )

            Text(text = errorMessage)
        }
    }
}