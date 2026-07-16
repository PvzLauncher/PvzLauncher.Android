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



val appfontfamily = FontFamily(
    Font(resId = R.font.appfont_regular, weight = FontWeight.Normal),
    Font(resId = R.font.appfont_bold, weight = FontWeight.Bold),
    Font(resId = R.font.appfont_thin, weight = FontWeight.Thin),
    Font(resId = R.font.appfont_black, weight = FontWeight.Black),
    Font(resId = R.font.appfont_light, weight = FontWeight.Light),
    Font(resId = R.font.appfont_medium, weight = FontWeight.Medium),
)

val AppTypography = Typography()

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

private val MainThemeLight = lightColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    tertiary = Color(0xFFEFB8C8))
private val MainThemeDark = darkColorScheme(
    primary = Color(0xFF6650a4),
    secondary = Color(0xFF625b71),
    tertiary = Color(0xFF7D5260))

@Composable
fun PvzLauncherAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,

    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> MainThemeDark
        else -> MainThemeLight
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GlobalTypography,
        content = content
    )
}