package com.pvzlauncher.pvzlauncher.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.controls.XW_Switch
import java.io.File

@Composable
public fun SettingPage()
{
    val lc = LocalContext.current
    Column(modifier = Modifier.padding(10.dp, 35.dp)) {
        var LocalSettings =
            ReadJson<LauncherConfig>(File("${LocalContext.current.filesDir}/${LAUNCHERCONFIGNAME}").readText())
        Box(modifier = Modifier.fillMaxWidth())
        {
            Text(
                "设置",
                fontWeight = Bold,
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.CenterStart),
                fontSize = 24.sp
            )
        }
        val scrollState = rememberScrollState()
        Column(Modifier.fillMaxSize().verticalScroll(scrollState))
        {
            if (false) {
                Column(
                    modifier = Modifier
                        .padding(10.dp, 2.dp)
                )
                {
                    Text(
                        "主题设置",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(0.dp),
                        fontSize = 18.sp
                    )
                    XW_Switch(
                        Icons.Default.LightMode,
                        "自动切换主题",
                        "",
                        modifier = Modifier.padding(0.dp),
                        LocalSettings.UseSystemTheme,
                        { isChecked ->
                            LocalSettings.UseSystemTheme = isChecked

                            WriteJson<LauncherConfig>(
                                LAUNCHERCONFIGNAME,
                                LocalSettings,
                                lc
                            )

                        })
                    XW_Switch(
                        Icons.Default.DarkMode,
                        "启用深色模式",
                        "",
                        modifier = Modifier.padding(0.dp),
                        LocalSettings.UseDarkTheme,
                        { isChecked ->
                            LocalSettings.UseDarkTheme = isChecked
                            WriteJson<LauncherConfig>(
                                LAUNCHERCONFIGNAME,
                                LocalSettings,
                                lc
                            )
                        })
                    Button(modifier = Modifier.padding(5.dp), onClick = {

                    }) {
                        Text("选择主题色")
                    }
                }
            }
            Column(modifier = Modifier.padding(10.dp, 2.dp))
            {
                Text(
                    "标题设置",
                    fontWeight = Bold,
                    modifier = Modifier.padding(0.dp),
                    fontSize = 18.sp
                )
                XW_Switch(
                    Icons.Default.Title,
                    "启用英文版标题",
                    "使用“Plants Vs. Zombies”字样而不是“植物大战僵尸”作为标题",
                    modifier = Modifier.padding(0.dp),
                    LocalSettings.UseEnglishTitle,
                    { isChecked ->
                        LocalSettings.UseEnglishTitle = isChecked
                        WriteJson<LauncherConfig>(
                            LAUNCHERCONFIGNAME,
                            LocalSettings,
                            lc
                        )
                    })
            }
            Column(modifier = Modifier.padding(10.dp, 2.dp))
            {
                Text(
                    "更新设置",
                    fontWeight = Bold,
                    modifier = Modifier.padding(0.dp),
                    fontSize = 18.sp
                )
                XW_Switch(
                    Icons.Default.Update,
                    "启动时检测更新",
                    "当程序启动时自动检测是否可以更新，如有更新将会弹窗通知",
                    modifier = Modifier.padding(0.dp),
                    LocalSettings.StartUpCheckUpdate,
                    { isChecked ->
                        LocalSettings.StartUpCheckUpdate = isChecked
                        WriteJson<LauncherConfig>(
                            LAUNCHERCONFIGNAME,
                            LocalSettings,
                            lc
                        )
                    })
            }
        }
    }
}