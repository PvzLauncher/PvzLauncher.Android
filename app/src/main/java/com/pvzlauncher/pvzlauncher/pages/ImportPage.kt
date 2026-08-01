package com.pvzlauncher.pvzlauncher.pages

import android.content.Context
import com.pvzlauncher.pvzlauncher.controls.XW_InputDialog
import com.pvzlauncher.pvzlauncher.controls.XW_ManageInformationCard
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.GetApkInfo
import com.pvzlauncher.pvzlauncher.utils.Installedappindex
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.utils.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.utils.isAppInstalled
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import kotlin.collections.plus
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.pvzlauncher.pvzlauncher.utils.APKPickerLauncher
import com.pvzlauncher.pvzlauncher.utils.DownloadCount
import com.pvzlauncher.pvzlauncher.utils.Downloadlist
import com.pvzlauncher.pvzlauncher.utils.ProcessList
import com.pvzlauncher.pvzlauncher.utils.installApk
import com.pvzlauncher.pvzlauncher.utils.intProcessList
import com.pvzlauncher.pvzlauncher.utils.intProcessProgressList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ImportPage()
{
    val lc = LocalContext.current
    var isRendered by rememberSaveable{ mutableStateOf(true) }
    TopAppBar(
        title = {
            Text(
                "导入",
                fontWeight = Bold,
                modifier = Modifier.padding(5.dp),
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            TextButton(
                onClick = {
                    CurrentDestination = AppDestinations.ManagePage
                }, modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp), contentPadding = PaddingValues(0.dp),
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
    Column(
        Modifier
            .padding(10.dp, 90.dp, 10.dp, 10.dp)
            .fillMaxSize().verticalScroll(scrollState)
    )
    {
        var listcount by rememberSaveable { mutableStateOf(0) }
        var aaa =
            ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText())
        if(isRendered)
        {
            Installedappindex.forEach outer@ { i ->

                if ((lc.packageManager.getApplicationInfo(i.packageName, 0).flags and ApplicationInfo.FLAG_SYSTEM) == 0 && isAppInstalled(lc, i.packageName) && i.packageName != "com.pvzlauncher.pvzlauncher") {



                        for(k in aaa.ListIndex)
                        {
                            for(l in k.GameIndex)
                            {
                                if(l.PackageName == i.packageName)
                                {
                                    return@outer
                                }
                            }
                        }
                    listcount += 1
                    var isdialogvisible by rememberSaveable { mutableStateOf(false) }
                    XW_InputDialog(isdialogvisible,title = "提示","请输入版本标题","","${lc.packageManager.getApplicationLabel(lc.packageManager.getApplicationInfo(i.packageName, 0)).toString()}",{isdialogvisible = false},{t ->

                        aaa.ListIndex[0].GameIndex += SaveConfig(
                            GameName = t,
                            PackageName = i.packageName,
                            AddTime = ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                            PlayTime = 0,
                            LaunchTimes = 0,
                            headImage = "https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameAssets/Default.png",
                            gameversion = GetApkInfo(i.packageName, lc).versionName ?: "1.0.0",
                            like = false
                        )
                        WriteJson<SaveConfigList>(
                            SAVECONFIGNAME,
                            aaa,
                            lc
                        )
                        Installedappindex.remove(i)
                        XW_ToastMessage("导入成功", lc)})

                    XW_ManageInformationCard(SaveConfig("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameAssets/Default.png",GetApkInfo(i.packageName, lc).versionName ?: "1.0.0","${lc.packageManager.getApplicationLabel(lc.packageManager.getApplicationInfo(i.packageName, 0)).toString()}",i.packageName,ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),0,0,false),{isdialogvisible = true},true)



                }

            }
            if(listcount == 0)
            {
                Box(Modifier.fillMaxSize())
                {
                    Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("暂无可导入游戏",fontSize = 18.sp, fontWeight = Bold)
                        Text("请检查是否未给予权限或手机内应用已全部导入", fontSize = 14.sp)
                    }
                }

            }
        }
    }
    Box(Modifier.fillMaxSize().padding(5.dp,35.dp))
    {
        var isDialogVisible by remember { mutableStateOf(false) }
        lateinit var currentapk : File
        lateinit var cinfo : PackageInfo
        var apptitle by rememberSaveable { mutableStateOf("") }



            val al = APKPickerLauncher({ i ->
                currentapk = i
                try
                {
                    cinfo = lc.packageManager.getPackageArchiveInfo(
                        i.absolutePath,
                        PackageManager.GET_ACTIVITIES or
                                PackageManager.GET_SERVICES or
                                PackageManager.GET_RECEIVERS or
                                PackageManager.GET_PROVIDERS or
                                PackageManager.GET_META_DATA
                    )!!
                }
                catch (e: Exception)
                {
                    XW_ToastMessage("安装包已损坏，无法读取！",lc)
                }
                CurrentDestination = AppDestinations.ImportPage
                installApk(
                    lc,
                    i,
                    {
                        isRendered = false
                        refreshInstalledapplist(lc)
                        isRendered = true
                    },{})



            },{})




        Row(modifier = Modifier.align(Alignment.TopEnd), verticalAlignment = Alignment.CenterVertically)
        {
            TextButton(
                onClick = {
                    al("application/vnd.android.package-archive")
                },
                modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape
            )
            {

                Icon(
                    imageVector = Icons.Default.Add,
                    "检测更新",
                    modifier = Modifier.size(32.dp)
                )

            }
            TextButton(
                onClick = {
                    isRendered = false
                    refreshInstalledapplist(lc)
                    isRendered = true
                },
                modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape
            )
            {

                Icon(
                    imageVector = Icons.Default.Refresh,
                    "检测更新",
                    modifier = Modifier.size(32.dp)
                )

            }
        }
    }
}

public fun refreshInstalledapplist(lc : Context)
{
    try {
        Installedappindex =
            lc.packageManager.getInstalledPackages(0).toMutableStateList()


    } catch (e: Exception) {
        XW_ToastMessage("无法获取到游戏索引,${e.message}", lc)
    }
}