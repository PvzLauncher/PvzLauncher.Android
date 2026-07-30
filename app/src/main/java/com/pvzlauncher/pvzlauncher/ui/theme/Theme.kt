package com.pvzlauncher.pvzlauncher.ui.theme

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val XW_LightTheme = lightColorScheme(
    primary = Color(0xFF63A002),
    secondary = Color(0xFF749D46),
    tertiary = Color(0xFF69835A),

)

val XW_DarkTheme = darkColorScheme(
    primary = Color(0xFF63A002),
    secondary = Color(0xFF749D46),
    tertiary = Color(0xFF69835A) ,

    )

val XW_Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    )
)

@Composable
fun PvzLauncherAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,

    content: @Composable () -> Unit
) {
    val XW_ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> XW_DarkTheme
        else -> XW_LightTheme
    }
    MaterialTheme(
        colorScheme = XW_ColorScheme,
        typography = XW_Typography,
        content = content
    )
}