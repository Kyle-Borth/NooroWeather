package com.nooro.weather.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nooro.weather.R

private val poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold)
)

val Typography = Typography(
    // displayLarge is the largest display text.
    displayLarge = TextStyle(
        fontFamily = poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 60.sp,
        lineHeight = 90.sp
    ),

    // bodyLarge is the largest body, and is typically used for long-form writing as it works well for small text sizes.
    // For longer sections of text, a serif or sans serif typeface is recommended.
    bodyLarge = TextStyle(
        fontFamily = poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.5.sp
    ),

    // headlineLarge is the largest headline, reserved for short, important text or numerals. For headlines, you can
    // choose an expressive font, such as a display, handwritten, or script style. These unconventional font designs
    // have details and intricacy that help attract the eye.
    headlineLarge = TextStyle(
        fontFamily = poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 70.sp,
        lineHeight = 105.sp
    ),

    // headlineMedium is the second largest headline, reserved for short, important text or numerals. For headlines, you
    // can choose an expressive font, such as a display, handwritten, or script style. These unconventional font designs
    // have details and intricacy that help attract the eye.
    headlineMedium = TextStyle(
        fontFamily = poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 45.sp
    ),

    // headlineSmall is the smallest headline, reserved for short, important text or numerals. For headlines, you can
    // choose an expressive font, such as a display, handwritten, or script style. These unconventional font designs
    // have details and intricacy that help attract the eye.
    headlineSmall = TextStyle(
        fontFamily = poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.5.sp
    ),

    // titleLarge is the largest title, and is typically reserved for medium-emphasis text that is shorter in length.
    // Serif or sans serif typefaces work well for subtitles.
    titleLarge = TextStyle(
        fontFamily = poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 30.sp
    ),

    // titleMedium is the second largest title, and is typically reserved for medium-emphasis text that is shorter in
    // length. Serif or sans serif typefaces work well for subtitles.
    titleMedium = TextStyle(
        fontFamily = poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.5.sp
    ),

    // labelMedium is one of the smallest font sizes. It is used sparingly to annotate imagery or to introduce a
    // headline.
    labelMedium = TextStyle(
        fontFamily = poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )
)