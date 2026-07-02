package com.pvzlauncher.pvzlauncher.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.pvzlauncher.pvzlauncher.R

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    secondary = Color(0xFF0288D1),
    tertiary = Color(0xFF01579B)
)

val appfontfamily = FontFamily(
    Font(resId = R.font.appfont_regular, weight = FontWeight.Normal),
    Font(resId = R.font.appfont_bold, weight = FontWeight.Bold),
    Font(resId = R.font.appfont_thin, weight = FontWeight.Thin),
    Font(resId = R.font.appfont_black, weight = FontWeight.Black),
    Font(resId = R.font.appfont_light, weight = FontWeight.Light),
    Font(resId = R.font.appfont_medium, weight = FontWeight.Medium),
)

val AppTypography = Typography()

// 3. 核心：用你的字体，强行覆盖系统默认的所有字号样式
val GlobalTypography = Typography(
    displayLarge = AppTypography.displayLarge.copy(fontFamily = appfontfamily),
    displayMedium = AppTypography.displayMedium.copy(fontFamily = appfontfamily),
    displaySmall = AppTypography.displaySmall.copy(fontFamily = appfontfamily),

    headlineLarge = AppTypography.headlineLarge.copy(fontFamily = appfontfamily),
    headlineMedium = AppTypography.headlineMedium.copy(fontFamily = appfontfamily),
    headlineSmall = AppTypography.headlineSmall.copy(fontFamily = appfontfamily),

    titleLarge = AppTypography.titleLarge.copy(fontFamily = appfontfamily),
    titleMedium = AppTypography.titleMedium.copy(fontFamily = appfontfamily),
    titleSmall = AppTypography.titleSmall.copy(fontFamily = appfontfamily),

    bodyLarge = AppTypography.bodyLarge.copy(fontFamily = appfontfamily),
    bodyMedium = AppTypography.bodyMedium.copy(fontFamily = appfontfamily),
    bodySmall = AppTypography.bodySmall.copy(fontFamily = appfontfamily),

    labelLarge = AppTypography.labelLarge.copy(fontFamily = appfontfamily),
    labelMedium = AppTypography.labelMedium.copy(fontFamily = appfontfamily),
    labelSmall = AppTypography.labelSmall.copy(fontFamily = appfontfamily)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF29B6F6),
    secondary = Color(0xFF0288D1),
    tertiary = Color(0xFF01579B)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PvzLauncherAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,

    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GlobalTypography,
        content = content
    )
}