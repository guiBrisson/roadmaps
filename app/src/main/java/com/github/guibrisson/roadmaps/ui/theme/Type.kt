package com.github.guibrisson.roadmaps.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.guibrisson.roadmaps.R

val jetbrainsMonoFontFamily = FontFamily(
    Font(R.font.jetbrains_mono_extra_bold, FontWeight.ExtraBold),
    Font(R.font.jetbrains_mono_bold, FontWeight.Bold),
    Font(R.font.jetbrains_mono_semi_bold, FontWeight.SemiBold),
    Font(R.font.jetbrains_mono_medium, FontWeight.Medium),
    Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
    Font(R.font.jetbrains_mono_light, FontWeight.Light),
    Font(R.font.jetbrains_mono_extra_light, FontWeight.ExtraLight),
    Font(R.font.jetbrains_mono_thin, FontWeight.Thin),
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = jetbrainsMonoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = jetbrainsMonoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = jetbrainsMonoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = jetbrainsMonoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = jetbrainsMonoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
)