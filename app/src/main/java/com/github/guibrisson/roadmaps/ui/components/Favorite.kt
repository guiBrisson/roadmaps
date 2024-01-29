package com.github.guibrisson.roadmaps.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.guibrisson.roadmaps.R
import com.github.guibrisson.roadmaps.ui.theme.yellow

@Composable
fun Favorite(
    onFavorite: () -> Unit,
    isFavorite: Boolean,
) {
    IconButton(modifier = Modifier, onClick = onFavorite) {
        val tint = if (isFavorite) {
            yellow
        } else {
            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
        }

        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = stringResource(id = R.string.favorite_roadmap_icon_content_description),
            tint = tint,
        )
    }
}
