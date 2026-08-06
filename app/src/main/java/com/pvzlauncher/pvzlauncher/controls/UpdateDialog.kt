package com.pvzlauncher.pvzlauncher.controls

import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.ui.theme.PvzLauncherAndroidTheme
import com.pvzlauncher.pvzlauncher.utils.APP_VERSION
import com.pvzlauncher.pvzlauncher.utils.UpdateConfig
import dev.jeziellago.compose.markdowntext.MarkdownText

public fun XW_UpdateDialog(lc: Context, args: UpdateConfig, onDismiss: () -> Unit, onConfirm: () -> Unit)
{
    val m3BaseThemeRes = com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar
    val baseContext = ContextThemeWrapper(lc, m3BaseThemeRes)
    val m3Context = ContextThemeWrapper(baseContext, R.style.XW_DialogTheme)
    val composeView = ComposeView(lc).apply {
        setContent {
            PvzLauncherAndroidTheme {
                if(isSystemInDarkTheme())
                {
                    Column(Modifier.padding(10.dp)) {
                        Text("当前版本：${APP_VERSION}",color = Color.White)
                        Text("最新版本：${args.LatestVersion}", color = Color.White)
                        Text("是否更新？", fontWeight = FontWeight.Bold, color = Color.White)
                        MarkdownText(
                            args.LatestDescription.trimIndent(), color = Color.White
                        )
                    }
                }
                else
                {
                    Column(Modifier.padding(10.dp)) {
                        Text("当前版本：${APP_VERSION}",color = Color.Black)
                        Text("最新版本：${args.LatestVersion}", color = Color.Black)
                        Text("是否更新？", fontWeight = FontWeight.Bold, color = Color.Black)
                        MarkdownText(
                            args.LatestDescription.trimIndent(), color = Color.Black
                        )
                    }
                }

            }
        }
    }
        MaterialAlertDialogBuilder(m3Context)
            .setTitle("更新可用")
            .setView(composeView)
            .setPositiveButton("确定") { dialog, which ->
                onConfirm()
            }
            .setNegativeButton("取消") { dialog, which ->
                onDismiss()
            }
            .setCancelable(false)
            .show()

}