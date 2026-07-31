package com.pvzlauncher.pvzlauncher.pages

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.controls.XW_Button
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.controls.XW_Switch
import com.pvzlauncher.pvzlauncher.controls.XW_simpledialog
import com.pvzlauncher.pvzlauncher.utils.GameConfig
import com.pvzlauncher.pvzlauncher.utils.JsonPickerLauncher
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.utils.rememberPhotoPickerLauncher
import com.pvzlauncher.pvzlauncher.utils.shareConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
public fun SettingPage()
{
    val lc = LocalContext.current
    val scope = rememberCoroutineScope()
    var LocalSettings = ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}").readText())
    var isRendered by rememberSaveable { mutableStateOf(true) }
    Box(Modifier.fillMaxSize())
    {
        val openJsonPicker = JsonPickerLauncher({i ->
            try {
                XW_simpledialog(
                    "警告",
                    "此操作不可逆，确认要将所有设置覆盖吗？",
                    {
                        val old = ReadJson<LauncherConfig>(i.readText())
                        WriteJson<LauncherConfig>(LAUNCHERCONFIGNAME,old,lc)
                        XW_simpledialog("提示","启动器需要重启以应用更改，应用即将退出……",{System.exit(0)},{System.exit(0)},lc)
                    },
                    {},lc
                )
            }
            catch(e:Exception)
            {
                XW_ToastMessage("配置项不合法！",lc)
            }
        },{})
        Column(modifier = Modifier.padding(10.dp, 35.dp)) {

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
                TextButton(
                    onClick = {
                        openJsonPicker("application/json")
                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(32.dp).align(Alignment.CenterEnd),
                    contentPadding = PaddingValues(0.dp),
                    shape = CircleShape
                )
                {

                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        "检测更新",
                        modifier = Modifier.size(32.dp)
                    )

                }
            }
            val openPicker = rememberPhotoPickerLauncher(
                onSuccess = {
                    LocalSettings.CostumBackground = true
                    WriteJson<LauncherConfig>(
                        LAUNCHERCONFIGNAME,
                        LocalSettings,
                        lc
                    )
                    isRendered = false
                    scope.launch {
                        delay(100)
                        isRendered = true
                    }

                },
                onError = {
                    isRendered = false
                    scope.launch {
                        delay(100)
                        isRendered = true
                    }

                }
            )
            val scrollState = rememberScrollState()
            if(isRendered)
            {
                var LocalSettings = ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}").readText())
                Column(Modifier.fillMaxSize().verticalScroll(scrollState))
                {

                    Column(
                        modifier = Modifier
                            .padding(10.dp, 2.dp)
                    )
                    {
                        Text(
                            "主题",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(0.dp),
                            fontSize = 18.sp
                        )
                        XW_Switch(
                            Icons.Default.Brightness6,
                            "自动切换主题",
                            "使程序随系统主题切换而自动切换(默认)",
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
                            "手动使程序保持深色主题，不随系统切换而切换",
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
                        XW_Switch(
                            Icons.Default.ColorLens,
                            "自定义主题色",
                            "选择你喜欢的颜色作为主题色",
                            Modifier,
                            LocalSettings.CostumThemeColor,
                            { i ->
                                LocalSettings.CostumThemeColor = i
                                WriteJson<LauncherConfig>(
                                    LAUNCHERCONFIGNAME,
                                    LocalSettings,
                                    lc
                                )
                            }
                        )
                        XW_Button(
                            Icons.Default.FileOpen,
                            "导出设置",
                            "将设置导出，方便跨设备传输",
                            "导出设置",
                            Modifier,{
                                shareConfig(lc)
                            }

                        )


                    }

                    Column(modifier = Modifier.padding(10.dp, 2.dp))
                    {
                        Text(
                            "主页",
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
                        XW_Switch(
                            Icons.Default.Image,
                            "自定义背景图",
                            "选择你喜欢的图片作为启动器主页背景(仅图片)",Modifier,
                            LocalSettings.CostumBackground,
                            { i ->
                                if(i == true)
                                {
                                    openPicker()
                                }
                                else
                                {
                                    LocalSettings.CostumBackground = i
                                    WriteJson<LauncherConfig>(
                                        LAUNCHERCONFIGNAME,
                                        LocalSettings,
                                        lc
                                    )
                                    File(lc.filesDir, "Background.png").delete()
                                }
                            }

                        )
                    }
                    Column(modifier = Modifier.padding(10.dp, 2.dp))
                    {
                        Text(
                            "游戏设置",
                            fontWeight = Bold,
                            modifier = Modifier.padding(0.dp),
                            fontSize = 18.sp
                        )
                        XW_Button(
                            Icons.Default.PlaylistRemove,
                            "清除游戏列表",
                            "将启动器内导入的所有游戏清空",
                            "立即删除",
                            Modifier,
                            OnClicked = {
                                XW_simpledialog(
                                    "警告",
                                    "此操作不可逆，确认要将游戏列表清空吗？",
                                    {
                                        WriteJson<SaveConfigList>(SAVECONFIGNAME, SaveConfigList(emptyList<SaveConfig>()),lc)
                                    },
                                    {},lc
                                )
                            }
                        )
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
                        XW_Button(
                            Icons.Default.Delete,
                            "删除下载缓存",
                            "删除下载游戏版本和启动器更新包的缓存",
                            "立即删除",
                            Modifier,
                            OnClicked = {
                                File("${lc.filesDir}/temp").delete()
                                val dir = File("${lc.filesDir}/temp")
                                if (!dir.exists()) {
                                    dir.mkdirs()
                                }
                                XW_ToastMessage("清除缓存成功",lc)
                            }
                        )
                    }
                    Column(modifier = Modifier.padding(10.dp, 2.dp))
                    {
                        Text(
                            "危险区",
                            fontWeight = Bold,
                            modifier = Modifier.padding(0.dp),
                            fontSize = 18.sp,
                            color = Color.Red
                        )
                        XW_Button(
                            Icons.Default.Restore,
                            "恢复默认配置",
                            "将启动器的所有设置恢复默认",
                            "立即删除",
                            Modifier,
                            OnClicked = {
                                XW_simpledialog(
                                    "警告",
                                    "此操作不可逆，确认要将所有设置恢复默认值吗？",
                                    {
                                        WriteJson<LauncherConfig>(
                                            LAUNCHERCONFIGNAME, LauncherConfig(
                                                UseSystemTheme = true,
                                                UseDarkTheme = false,
                                                UseEnglishTitle = false,
                                                CurrentGameIndex = 0,
                                                true, false,false
                                            ), lc
                                        )
                                        WriteJson<SaveConfigList>(SAVECONFIGNAME, SaveConfigList(emptyList<SaveConfig>()),lc)
                                        XW_simpledialog("提示","启动器需要重启以应用更改，应用即将退出……",{System.exit(0)},{System.exit(0)},lc)
                                    },
                                    {},lc
                                )
                            }
                        )
                    }

                }
            }
        }



    }
}