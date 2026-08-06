package com.pvzlauncher.pvzlauncher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.utils.CurrentIndex
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import java.io.File

val XW_LightTheme = lightColorScheme(
    primary = Color(0xFF63A002),
    secondary = Color(0xFF749D46),
    tertiary = Color(0xFF69835A),
    onPrimary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000)
)

val XW_DarkTheme = darkColorScheme(
    primary = Color(0xFF63A002),
    secondary = Color(0xFF749D46),
    tertiary = Color(0xFF69835A) ,
    onPrimary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFFFFFFF)
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
    val lc = LocalContext.current
    try{
        ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}"))
    }
    catch(e:Exception) {
        WriteJson<LauncherConfig>(
            File("${lc.filesDir}/${LAUNCHERCONFIGNAME}"), LauncherConfig(
                UseSystemTheme = true,
                UseDarkTheme = false,
                UseEnglishTitle = false,
                CurrentGameIndex = CurrentIndex(0,0),
                true, false,false,false
            ), lc
        )
    }
    val a = ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}"))
    val XW_ColorScheme = when {

        darkTheme -> {
            if(a.UseSystemTheme)
            {
                XW_DarkTheme
            }
            else{
                if(a.UseDarkTheme)
                {
                    XW_DarkTheme
                }
                else
                {
                    XW_LightTheme
                }
            }
        }
        else -> {
            if(a.UseSystemTheme)
            {
                XW_LightTheme
            }
            else{
                if(a.UseDarkTheme)
                {
                    XW_DarkTheme
                }
                else
                {
                    XW_LightTheme
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = XW_ColorScheme,
        typography = XW_Typography,
        content = content
    )
}

