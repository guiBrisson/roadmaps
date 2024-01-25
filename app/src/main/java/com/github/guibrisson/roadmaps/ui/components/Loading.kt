package com.github.guibrisson.roadmaps.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.guibrisson.roadmaps.R

fun LazyListScope.loading(modifier: Modifier = Modifier,) {
    item {
        Text(
            modifier = modifier.padding(bottom = 40.dp),
            text = stringResource(id = R.string.loading),
        )
    }
}

