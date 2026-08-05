package com.pvzlauncher.pvzlauncher.pages

import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Switch
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.controls.RadioDialog
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
import com.pvzlauncher.pvzlauncher.controls.XW_LoadingMask
import com.pvzlauncher.pvzlauncher.controls.XW_ManageInformationCard
import com.pvzlauncher.pvzlauncher.utils.ManagelistIndex
import com.pvzlauncher.pvzlauncher.controls.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.utils.OBBPickerLauncher
import com.pvzlauncher.pvzlauncher.utils.createTempFileFromUri
import com.pvzlauncher.pvzlauncher.utils.createTempOBBFileFromUri
import com.pvzlauncher.pvzlauncher.utils.deleteOBBFile
import com.pvzlauncher.pvzlauncher.utils.getFileName
import com.pvzlauncher.pvzlauncher.utils.isAppInstalled
import com.pvzlauncher.pvzlauncher.utils.millisToDate
import com.pvzlauncher.pvzlauncher.utils.uninstallApk
import kotlinx.coroutines.launch
import java.io.File
import kotlin.collections.listOf

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
    val all = ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"))

    if(isRendered)
    {
        Column(
            modifier = Modifier
                .padding(0.dp, 75.dp, 0.dp, 0.dp)
                .fillMaxSize().verticalScroll(scrollState)
        )
        {


            if(ManageIndex < all.ListIndex[ManagelistIndex].GameIndex.count())
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
                                WriteJson(File("${lc.filesDir}/${SAVECONFIGNAME}"), all, lc)
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
                                WriteJson(File("${lc.filesDir}/${SAVECONFIGNAME}"), all, lc)
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


                        OutlinedButton(onClick = {
                            var kk =
                                ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}"))
                            kk.CurrentGameIndex.GameIndex = ManageIndex
                            kk.CurrentGameIndex.ListIndex = ManagelistIndex
                            WriteJson(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}"), kk, lc)
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
                            require = "一并卸载对应应用",
                            onDismiss = { isDialogVisible = false },
                            onConfirm = { i ->
                                if (i == true) {
                                    uninstallApk(lc,all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName)
                                }
                                var a2 = all.ListIndex[ManagelistIndex].GameIndex.toMutableList()
                                a2.removeAt(ManageIndex)
                                all.ListIndex[ManagelistIndex].GameIndex = a2.toList()
                                WriteJson(File("${lc.filesDir}/${SAVECONFIGNAME}"), all, lc)
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
                                WriteJson(File("${lc.filesDir}/${SAVECONFIGNAME}"), all, lc)

                                XW_ToastMessage("操作成功", lc)
                                CurrentDestination = AppDestinations.ManagePage
                            })
                    }
                    if(!isAppInstalled(lc,all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName))
                    {
                        Box(Modifier.fillMaxSize())
                        {
                            Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("此游戏对应的应用程序不存在",fontSize = 18.sp, fontWeight = Bold)
                                Text("请重新导入", fontSize = 14.sp)
                            }
                        }
                    }
                    else
                    {
                        Column()
                        {
                            Row(verticalAlignment = Alignment.CenterVertically)
                            {
                                var isDialogVisible by rememberSaveable { mutableStateOf(false) }
                                val cfg = ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"))
                                var names = emptyList<String>().toMutableList()
                                for(i in cfg.ListIndex)
                                {
                                    names.add(i.listname)
                                }
                                RadioDialog(isDialogVisible,"提示","请选择要移入的收藏夹",names.toList(),{isDialogVisible = false},{j ->
                                    val v1 = all.ListIndex[ManagelistIndex].GameIndex[ManageIndex]
                                    val v0 = all.ListIndex[ManagelistIndex].GameIndex.toMutableList()
                                    v0.remove(v1)
                                    all.ListIndex[ManagelistIndex].GameIndex = v0.toList()
                                    WriteJson(File("${lc.filesDir}/${SAVECONFIGNAME}"), all, lc)
                                    ManagelistIndex = j
                                    val v2 = all.ListIndex[j].GameIndex.toMutableList()
                                    v2.add(v1)
                                    all.ListIndex[j].GameIndex = v2.toList()
                                    WriteJson(File("${lc.filesDir}/${SAVECONFIGNAME}"), all, lc)
                                    isDialogVisible = false
                                    RefreshDetail()})
                                Text("所在收藏夹：${all.ListIndex[ManagelistIndex].listname}")
                                TextButton(onClick = {
                                    isDialogVisible = true
                                })
                                {
                                    Text("修改所在收藏夹")
                                }
                            }

                            fun IsCostumOBBInUse() : Boolean{
                                if(File(
                                        Environment.getExternalStorageDirectory(),
                                        "Android/obb/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}"
                                    ).exists())
                                {
                                    if(File(
                                            Environment.getExternalStorageDirectory(),
                                            "Android/obb/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}"
                                        ).listFiles()
                                            ?.any { it.isFile && it.extension.equals("obb", ignoreCase = true) }
                                            ?: false)
                                    {
                                        return true
                                    }
                                    return false
                                }
                                return false
                            }
                            var m = XW_LoadingMask(lc,"请稍候……")
                            var ischecked by rememberSaveable {mutableStateOf(IsCostumOBBInUse())}
                            val scope = rememberCoroutineScope()
                            var a = OBBPickerLauncher({ i ->
                                try {
                                    if (getFileName(lc,i)?.endsWith(".obb", true) == true) {
                                        m.show()
                                        if(!File(Environment.getExternalStorageDirectory(),"Android/obb/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}").exists())
                                        {
                                            File(Environment.getExternalStorageDirectory(),"Android/obb/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}").mkdirs()
                                        }
                                        scope.launch {
                                            createTempOBBFileFromUri(lc,i,"${Environment.getExternalStorageDirectory()}/Android/obb/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}/${getFileName(lc,i)}")
                                            ischecked = true
                                            m.hide()
                                        }

                                    }
                                    else
                                    {
                                        ischecked = false
                                        XW_ToastMessage("文件不是obb格式，请重新选择！")
                                    }

                                }
                                catch(e: Exception)
                                {
                                    XW_ToastMessage("无法复制obb,${i}")
                                    ischecked = false
                                }
                            },{ i->
                                ischecked = false
                            })
                            Row(verticalAlignment = Alignment.CenterVertically)
                            {
                                Text("使用自定义OBB:",Modifier.padding(0.dp,0.dp,5.dp,0.dp))
                                Switch(ischecked,{ischecked = it
                                    if(ischecked)
                                    {
                                        a("*/*")
                                    }
                                    else
                                    {
                                        //File(Environment.getExternalStorageDirectory(),"Android/obb/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}").delete()
                                        scope.launch {
                                            m.show()
                                            deleteOBBFile("${Environment.getExternalStorageDirectory()}/Android/obb/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}")
                                            m.hide()
                                            ischecked = IsCostumOBBInUse()
                                        }
                                    }

                                })

                            }


                                Row(verticalAlignment = Alignment.CenterVertically)
                                {
                                    Text("存档管理：")
                                    TextButton(
                                        onClick = {
                                            CurrentDestination = AppDestinations.SavePage
                                        }
                                    ) {
                                        Text("进入存档管理页面")
                                    }
                                }

                            Text("入库时间:${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].AddTime}")
                            Text("启动次数:${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].LaunchTimes}")
                            if(false)
                            {
                                Text("游玩时间:${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PlayTime}分钟")
                            }
                            if(all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].latestlaunchtime != 0L)
                            {
                                Text("最后启动时间:${millisToDate(all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].latestlaunchtime)}")
                            }

                        }
                    }

                }
            }
            else
            {
                Row(modifier = Modifier.padding(2.dp).fillMaxWidth())
                {
                    XW_ManageInformationCard(args = SaveConfig("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameAssets/Default.png","","","","",0,0,false,0L), onBack = {}, IsButtonEnable = false)
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
                        Box(Modifier.fillMaxSize())
                        {
                            Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("此游戏对应的应用程序不存在",fontSize = 18.sp, fontWeight = Bold)
                                Text("请重新导入", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }

}