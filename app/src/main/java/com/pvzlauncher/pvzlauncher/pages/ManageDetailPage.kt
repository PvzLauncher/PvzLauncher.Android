package com.pvzlauncher.pvzlauncher.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.controls.XW_CheckBoxDialog
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.ManageIndex
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.controls.XW_InputDialog
import com.pvzlauncher.pvzlauncher.controls.XW_ManageInformationCard
import com.pvzlauncher.pvzlauncher.utils.ManagelistIndex
import com.pvzlauncher.pvzlauncher.utils.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.utils.uninstallApk
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ManageDetailPage()
{
    val lc = LocalContext.current
    var isRendered by rememberSaveable{ mutableStateOf(true) }
    TopAppBar(
        title = {
            Text(
                "管理游戏",
                fontWeight = Bold,
                modifier = Modifier.padding(5.dp),
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            TextButton(
                onClick = {
                    CurrentDestination = AppDestinations.ManagePage
                },
                modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp)
                    .background(Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape

            )
            {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    "返回",
                    modifier = Modifier.size(32.dp)
                )

            }
        }
    )
    val scope = rememberCoroutineScope()
    fun RefreshDetail()
    {
        isRendered = false
        scope.launch {
            isRendered = true
        }
    }
    val scrollState = rememberScrollState()
    if(isRendered)
    {
        Column(
            modifier = Modifier
                .padding(0.dp, 75.dp, 0.dp, 0.dp)
                .fillMaxSize().verticalScroll(scrollState)
        )
        {

            val all = ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText())
            if(ManageIndex < all.ListIndex[ManageIndex].GameIndex.count())
            {
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                )
                {

                    if(all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].like == false)
                    {
                        XW_ManageInformationCard(
                            args = all.ListIndex[ManagelistIndex].GameIndex[ManageIndex],
                            onBack = {
                                all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].like = true
                                    WriteJson(SAVECONFIGNAME, all, lc)
                                RefreshDetail()

                            }, IsButtonEnable = true,icon = Icons.Default.StarOutline

                        )
                    }
                    else
                    {
                        XW_ManageInformationCard(
                            args = all.ListIndex[ManagelistIndex].GameIndex[ManageIndex],
                            onBack = {
                                all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].like = false
                                val svc = all.ListIndex[ManagelistIndex].GameIndex[ManageIndex]
                                val v1 = all.ListIndex[ManagelistIndex].GameIndex.toMutableList()
                                v1.removeAt(ManageIndex)
                                v1.add(0,svc)
                                val v2 = v1.toList()
                                all.ListIndex[ManagelistIndex].GameIndex = v2
                                    WriteJson(SAVECONFIGNAME, all, lc)
                                RefreshDetail()

                            }, IsButtonEnable = true,icon = Icons.Default.Star
                        )
                    }
                }
                Column(Modifier.padding(10.dp,5.dp,)) {
                    Row(Modifier.fillMaxWidth())
                    {
                        var isDialogVisible by remember { mutableStateOf(false) }

                        var isDialogVisible2 by remember { mutableStateOf(false) }

                        var isDialogVisible3 by remember { mutableStateOf(false) }

                        OutlinedButton(onClick = {
                            var kk =
                                ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}").readText())
                            kk.CurrentGameIndex.GameIndex = ManageIndex
                            kk.CurrentGameIndex.ListIndex = ManagelistIndex
                            WriteJson("${LAUNCHERCONFIGNAME}", kk, lc)
                            XW_ToastMessage("操作成功", lc)
                            CurrentDestination = AppDestinations.ManagePage
                        }, Modifier.padding(2.dp)) {
                            Text("设为活动")
                        }
                        OutlinedButton(onClick = {
                            isDialogVisible = true


                        }, Modifier.padding(2.dp)) {
                            Text("删除游戏", color = Color.Red)
                        }
                        XW_CheckBoxDialog(
                            showDialog = isDialogVisible,
                            title = "警告",
                            content = "您一定要删除${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].GameName}吗？这个游戏将会永久消失(真的很久！)",
                            require = "一并卸载apk",
                            onDismiss = { isDialogVisible = false },
                            onConfirm = { i ->
                                if (i == true) {
                                    uninstallApk(lc,all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName)
                                }
                                var a2 = all.ListIndex[ManagelistIndex].GameIndex.toMutableList()
                                a2.removeAt(ManageIndex)
                                all.ListIndex[ManagelistIndex].GameIndex = a2.toList()
                                WriteJson(SAVECONFIGNAME, all, lc)
                                isDialogVisible = false
                                CurrentDestination = AppDestinations.ManagePage
                            }
                        )
                        OutlinedButton(onClick = {
                            isDialogVisible2 = true
                        }, Modifier.padding(2.dp)) {
                            Text("更改名称")
                        }
                        XW_InputDialog(
                            showDialog = isDialogVisible2,
                            title = "提示",
                            content = "请输入新名字",
                            placeholder = "请输入",
                            onDismiss = { isDialogVisible2 = false },
                            value = "",
                            onConfirm = { text ->

                                all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].GameName = text
                                WriteJson(SAVECONFIGNAME, all, lc)

                                XW_ToastMessage("操作成功", lc)
                                CurrentDestination = AppDestinations.ManagePage
                            })
                    }
                    Column()
                    {
                        Text("入库时间:${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].AddTime}")
                        Text("启动次数:${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].LaunchTimes}")
                    }
                }
            }
            else
            {
                Row(modifier = Modifier.padding(2.dp).fillMaxWidth())
                {
                    XW_ManageInformationCard(args = SaveConfig("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameAssets/Default.png","","","","",0,0,false), onBack = {}, IsButtonEnable = false)
                }
                Column(Modifier) {
                    Row(Modifier.fillMaxWidth())
                    {
                        OutlinedButton(onClick = {
                        }, Modifier.padding(2.dp)) {
                            Text("设为活动")
                        }
                        OutlinedButton(onClick = {
                        }, Modifier.padding(2.dp)) {
                            Text("删除游戏", color = Color.Red)
                        }
                        OutlinedButton(onClick = {
                        }, Modifier.padding(2.dp)) {
                            Text("更改名称")
                        }

                    }
                    Column()
                    {
                        Text("入库时间:1970/01/01 00:00:00")
                        Text("启动次数:0")
                    }
                }
            }
        }
    }

}