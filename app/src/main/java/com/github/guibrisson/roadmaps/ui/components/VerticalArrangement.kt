package com.github.guibrisson.roadmaps.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Density

@Composable
fun alignLastItemToBottom() = remember {
    object : Arrangement.Vertical {
        override fun Density.arrange(
            totalSize: Int,
            sizes: IntArray,
            outPositions: IntArray
        ) {
            var currentOffset = 0
            sizes.forEachIndexed { index, size ->
                if (index == sizes.lastIndex) {
                    outPositions[index] = totalSize - size
                } else {
                    outPositions[index] = currentOffset
                    currentOffset += size
                }
            }
        }
    }
}