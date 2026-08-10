package com.pvzlauncher.pvzlauncher.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.pvzlauncher.pvzlauncher.controls.XW_LoadingMask
import com.pvzlauncher.pvzlauncher.controls.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.ui.theme.XW_LightTheme
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.DefaultSaveConfig
import com.pvzlauncher.pvzlauncher.utils.DirectoryPicker
import com.pvzlauncher.pvzlauncher.utils.ManageIndex
import com.pvzlauncher.pvzlauncher.utils.ManagelistIndex
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.ZIPPickerLauncher
import com.pvzlauncher.pvzlauncher.utils.ZipShare
import com.pvzlauncher.pvzlauncher.utils.accessAndroidData
import com.pvzlauncher.pvzlauncher.utils.accessAndroidDataWithResult
import com.pvzlauncher.pvzlauncher.utils.currentactivity
import com.pvzlauncher.pvzlauncher.utils.unzipFolder
import com.pvzlauncher.pvzlauncher.utils.uriToLintFile
import com.pvzlauncher.pvzlauncher.utils.zipFolder
import io.github.lumkit.io.LintFile
import io.github.lumkit.io.file
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SavePage()
{
    val lc = LocalContext.current
    var all by remember { mutableStateOf(DefaultSaveConfig()) }
    var accessFile by remember { mutableStateOf<LintFile?>(null) }
    var canEdit by remember { mutableStateOf(false) }
    var expand by remember { mutableStateOf(false) }
    var datapath by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        all = ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"))
    }
    LaunchedEffect(all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName) {
        var a = false
        accessAndroidData(currentactivity, all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName, "",)
        {
            if(this.listFiles().count() != 0)
            {
                a = true
            }
        }
        accessAndroidDataWithResult(currentactivity, all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName, "/files",{res ->
            if(res && a)
            {
                canEdit = true
            }

        }){
            accessFile = this
        }
    }
    TopAppBar(
        title = {
            Text(
                "导入/导出存档",
                fontWeight = Bold,
                modifier = Modifier.padding(5.dp),
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            TextButton(
                onClick = {
                    CurrentDestination = AppDestinations.ManageDetailPage
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
    val scrollState = rememberScrollState()
    Box(Modifier.fillMaxSize().padding(0.dp, 75.dp, 0.dp, 0.dp)
        .fillMaxSize().verticalScroll(scrollState))
    {
        if(!canEdit)
        {
            Box(Modifier.fillMaxSize())
            {
                Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("暂无内容",fontSize = 18.sp, fontWeight = Bold)
                    Text("此游戏或者您的系统不支持存档编辑", fontSize = 14.sp)
                }
            }
            return
        }
        Column(Modifier.fillMaxSize().padding(5.dp))
        {
            Text("请选择对应版本的存档目录")
            accessFile?.let { root ->


                    DirectoryPicker(
                        root = root,
                        onDirectorySelected = { path ->
                            datapath = path
                        },
                        onPermissionFailed = {}
                    )

            }


        }
        if(datapath != "")
        {
            FloatingActionButtonMenu(expanded = expand, button = {
                FloatingActionButton(
                    onClick = {
                        expand = !expand
                    },
                    modifier = Modifier
                        .size(48.dp),
                    shape = RoundedCornerShape(10.dp), containerColor = XW_LightTheme.primary
                )
                {

                    Icon(
                        imageVector = Icons.Default.Menu,
                        "检测更新",
                        modifier = Modifier.size(32.dp)
                    )

                }
            }, Modifier.padding(30.dp).align(Alignment.BottomEnd))
            {
                val scope = rememberCoroutineScope()
                val m = XW_LoadingMask(lc,"请稍候……")
                val zippicker = ZIPPickerLauncher(onSuccess = {
                    m.show()
                    scope.launch {
                        val zipfile = uriToLintFile(lc,it)
                        unzipFolder(currentactivity,zipfile,all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName,datapath)
                        m.hide()
                        XW_ToastMessage("导入成功")

                    }
                }, onError = {})


                FloatingActionButtonMenuItem(onClick = {
                    zippicker()


                }, text = {
                    Text("覆盖导入")
                }, icon = {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        "检测更新",
                        modifier = Modifier.size(32.dp)
                    )
                }, containerColor = XW_LightTheme.primary)
                FloatingActionButtonMenuItem(onClick = {
                    scope.launch {
                        m.show()
                        val filename = "${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].GameName}_存档_${System.currentTimeMillis()}.zip"
                        zipFolder(currentactivity,all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName,datapath,file("${lc.filesDir}/temp/${filename}"))
                        m.hide()
                        ZipShare(lc,File("${lc.filesDir}/temp/${filename}"))
                    }
                }, text = {
                    Text("打包导出")
                }, icon = {
                    Icon(
                        imageVector = Icons.Default.FileOpen,
                        "检测更新",
                        modifier = Modifier.size(32.dp)
                    )
                }, containerColor = XW_LightTheme.primary)
            }
        }

    }
}


