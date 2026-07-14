package com.pvzlauncher.pvzlauncher

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import com.downloader.OnCancelListener
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnPauseListener
import com.downloader.OnProgressListener
import com.downloader.OnStartOrResumeListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.downloader.Progress
import com.downloader.Status
import com.jakewharton.threetenabp.AndroidThreeTen
import com.pvzlauncher.pvzlauncher.ui.theme.PvzLauncherAndroidTheme
import com.pvzlauncher.pvzlauncher.utils.APP_VERSION

import com.pvzlauncher.pvzlauncher.utils.DownloadConfig
import com.pvzlauncher.pvzlauncher.utils.GameConfig
import com.pvzlauncher.pvzlauncher.utils.GameListConfig
import com.pvzlauncher.pvzlauncher.utils.GetApkInfo
import com.pvzlauncher.pvzlauncher.utils.GetWebSiteContent
import com.pvzlauncher.pvzlauncher.utils.InitializeLauncherSettings
import com.pvzlauncher.pvzlauncher.utils.InitializeSaveLists
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.MDR_FileName
import com.pvzlauncher.pvzlauncher.utils.MDR_MDContent
import com.pvzlauncher.pvzlauncher.utils.ManageConfigPath
import com.pvzlauncher.pvzlauncher.utils.ManageIndex
import com.pvzlauncher.pvzlauncher.utils.OpenUrl
import com.pvzlauncher.pvzlauncher.utils.ProcessConfig
import com.pvzlauncher.pvzlauncher.utils.ProcessList
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.UpdateConfig
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.utils.XW_GameInformationCard
import com.pvzlauncher.pvzlauncher.utils.XW_InputDialog
import com.pvzlauncher.pvzlauncher.utils.XW_LoadingMask
import com.pvzlauncher.pvzlauncher.utils.XW_ManageInformationCard
import com.pvzlauncher.pvzlauncher.utils.XW_Switch
import com.pvzlauncher.pvzlauncher.utils.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.utils.XW_simpledialog
import com.pvzlauncher.pvzlauncher.utils.installApk
import com.pvzlauncher.pvzlauncher.utils.intProcessList
import com.pvzlauncher.pvzlauncher.utils.intProcessProgressList
import com.pvzlauncher.pvzlauncher.utils.isAppInstalled
import com.pvzlauncher.pvzlauncher.utils.launchApp
import com.pvzlauncher.pvzlauncher.utils.sProcessProgressList
import com.pvzlauncher.pvzlauncher.utils.uninstallApk
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.io.File
import kotlinx.coroutines.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil3.compose.rememberAsyncImagePainter
import com.pvzlauncher.pvzlauncher.utils.CheckUpdate
import com.pvzlauncher.pvzlauncher.utils.DownloadCount
import com.pvzlauncher.pvzlauncher.utils.globalContext


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        android.os.StrictMode.setThreadPolicy(android.os.StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        enableEdgeToEdge()
        setContent {
            PvzLauncherAndroidTheme {
                PvzLauncherAndroidApp()
            }
        }




    }
}



@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun PvzLauncherAndroidApp() {

    var lcc = LocalContext.current

    globalContext = LocalContext.current

    APP_VERSION = (lcc.packageManager.getPackageInfo(lcc.packageName,0).versionName ?: "1.0.0")

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HomePage) }
    var startupcheckupdate by rememberSaveable { mutableStateOf(false) }
    var requestappinstall by rememberSaveable { mutableStateOf(false) }
    var checkinternetconnect by rememberSaveable { mutableStateOf(false) }
    if(!requestappinstall)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if(!lcc.packageManager.canRequestPackageInstalls())
            {
                val settingLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { _ ->
                    if (!lcc.packageManager.canRequestPackageInstalls()) {
                        XW_simpledialog(
                            "警告",
                            "警告,您未开启此权限，程序可能无法正常使用！是否前往打开此权限？",
                            {
                                val intent =
                                    Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                                        data = Uri.parse("package:${lcc.packageName}")
                                    }
                                // 启动跳转

                            },
                            {},
                            lcc
                        )
                    }

                }

                XW_simpledialog(
                    "提示",
                    "本程序需要申请“应用内安装应用”权限来帮助您安装各版本Pvz和提供游戏，自身升级包，请您同意此权限来使用此程序",
                    {

                        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                            data = Uri.parse("package:${lcc.packageName}")
                        }
                        // 启动跳转
                        settingLauncher.launch(intent)

                    },
                    {
                        XW_simpledialog(
                            "警告",
                            "警告,如果您还不开启此权限，程序可能无法正常使用！是否前往打开此权限？",
                            {
                                val intent =
                                    Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                                        data = Uri.parse("package:${lcc.packageName}")
                                    }
                                // 启动跳转
                                lcc.startActivity(intent)
                            },
                            {},
                            lcc
                        )
                    },
                    lcc
                )
            }
        }
        else{
            requestappinstall = true
        }


    }


    if (!File("${LocalContext.current.filesDir}/${LAUNCHERCONFIGNAME}").exists())
    {
        InitializeLauncherSettings()
    }
    if (!File("${LocalContext.current.filesDir}/${SAVECONFIGNAME}").exists())
    {
        InitializeSaveLists()
    }
    val config = PRDownloaderConfig.newBuilder()
        .setReadTimeout(30000)
        .setConnectTimeout(30000)
        .build()
    PRDownloader.initialize(LocalContext.current, config)
    if(checkinternetconnect == false)
    {

        try{
            GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/UpdateIndex.json")
        }
        catch(e:Exception){
            XW_simpledialog("警告！","无法访问上游数据库，将无法进行游戏的下载与安装，是否退出程序？\r\n错误信息：${e.stackTraceToString()}",{System.exit(0)},{},lcc)
        }
        checkinternetconnect = true
    }
    if(startupcheckupdate == false)
    {
        if(ReadJson<LauncherConfig>(File("${LocalContext.current.filesDir}/${LAUNCHERCONFIGNAME}").readText()).StartUpCheckUpdate == true)
        {
            CheckUpdate(lcc,true)
        }
    }











        NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                if (it.icon == Icons.Default.QuestionMark) {
                    return@forEach
                }
                    item(
                        icon = {
                            Icon(
                                it.icon,
                                it.label
                            )
                        },
                        label = { Text(it.label) },
                        selected = it == currentDestination,
                        onClick = { currentDestination = it }
                    )

            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val pageModifier = Modifier.padding(innerPadding)
            val lc = LocalContext.current
            val LocalSettings = ReadJson<LauncherConfig>(File("${LocalContext.current.filesDir}/${LAUNCHERCONFIGNAME}").readText())
            when (currentDestination) {
                AppDestinations.HomePage -> {

                    Box(modifier = Modifier.fillMaxSize()){
                        if(LocalSettings.UseEnglishTitle)
                        {
                            Image(painter = painterResource(id=R.drawable.ic_apptitle_en),"123",
                                alignment = Alignment.TopCenter, modifier = Modifier
                                    .padding(40.dp)
                                    .fillMaxWidth())
                        }
                        else
                        {
                            Image(painter = painterResource(id=R.drawable.ic_apptitle_zh),"123",
                                alignment = Alignment.TopCenter, modifier = Modifier
                                    .padding(40.dp)
                                    .fillMaxWidth())
                        }
                        Column(modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            Row(verticalAlignment = Alignment.CenterVertically)
                            {
                                Button(onClick = {
                                    if(ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex.count() != 0)
                                    {
                                        val current = ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex[ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}").readText()).CurrentGameIndex]


                                        val new =  ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText())
                                        new.GameIndex[ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}").readText()).CurrentGameIndex].LaunchTimes += 1
                                        WriteJson<SaveConfigList>(SAVECONFIGNAME,new,lc)
                                        launchApp(lc,current.PackageName)
                                    }

                                }, modifier = Modifier.height(64.dp))
                                {
                                    Column()
                                    {
                                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically){

                                            Icon(imageVector = Icons.Default.RocketLaunch,"", modifier = Modifier
                                                .padding(5.dp)
                                                .size(24.dp))
                                            Text("启动游戏",modifier = Modifier.padding(5.dp), fontWeight = Bold, fontSize = 18.sp)
                                        }

                                    }
                                }

                            }
                            Row()
                            {
                                Text("当前游戏：" , fontSize = 14.sp)
                                if(ReadJson<SaveConfigList>(File("${LocalContext.current.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex.count() != 0)
                                {
                                    val current = ReadJson<SaveConfigList>(File("${LocalContext.current.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex[ReadJson<LauncherConfig>(File("${LocalContext.current.filesDir}/${LAUNCHERCONFIGNAME}").readText()).CurrentGameIndex]
                                    Text("${current.GameName}", fontSize = 14.sp, fontWeight = Bold)
                                }
                                else{
                                    Text("请先下载游戏", fontSize = 14.sp, fontWeight = Bold)
                                }

                            }
                        }
                    }
                }
                AppDestinations.ManagePage -> {
                    Column(modifier = Modifier.padding(10.dp,35.dp)){
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp))
                        {
                            Text(
                                "管理",
                                fontWeight = Bold,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.CenterStart),
                                fontSize = 28.sp
                            )
                            var isDialogVisible by remember { mutableStateOf(false) }
                            var resultText by remember { mutableStateOf("") }
                            var llc = LocalContext.current
                            Button({
                                currentDestination = AppDestinations.ImportPage
                            }, Modifier
                                .padding(5.dp)
                                .align(Alignment.CenterEnd), ) {
                                Text("导入已安装版本")

                            }

                        }
                        val scrollState = rememberScrollState()
                        Column(Modifier
                            .padding(5.dp)
                            .fillMaxSize()
                            .verticalScroll(scrollState)) {
                            for(i in ReadJson<SaveConfigList>(File("${lcc.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex)
                            {
                                XW_ManageInformationCard(
                                    args = i,
                                    onBack = {
                                        ManageIndex = ReadJson<SaveConfigList>(File("${lcc.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex.indexOf(i)
                                        currentDestination = AppDestinations.ManageDetailPage
                                    },
                                    IsButtonEnable = true,

                                )

                            }
                        }

                    }
                }
                AppDestinations.DownloadPage -> {
                    Column(modifier = Modifier.padding(10.dp,35.dp)){
                        Box(modifier = Modifier.fillMaxWidth())
                        {
                            Text("下载", fontWeight = Bold,modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.CenterStart), fontSize = 28.sp)
                            Button(onClick = {
                                currentDestination = AppDestinations.TaskPage
                            }, modifier = Modifier
                                .padding(5.dp)
                                .size(48.dp)
                                .align(Alignment.CenterEnd),contentPadding = PaddingValues(0.dp),
                                shape = CircleShape
                            )
                            {

                                Icon(imageVector = Icons.Default.Downloading,"检测更新", modifier = Modifier.size(32.dp))

                            }
                        }
                        val scrollState = rememberScrollState()
                        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                            .padding(2.dp)
                            .fillMaxSize()
                            .verticalScroll(scrollState))
                        {
                            var gameindex = emptyList<GameConfig>()
                            try {
                                gameindex = ReadJson<GameListConfig>(GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameIndex.json")).GameIndex

                            }
                            catch (e : Exception)
                            {
                                XW_ToastMessage("无法获取到游戏索引,${e.message}",lc)
                            }
                            for(i in gameindex)
                            {
                                XW_GameInformationCard(i,{
                                    currentDestination = AppDestinations.DownloadDetailPage
                                },false,Icons.Default.ArrowRightAlt,false)
                            }

                        }

                    }
                }
                AppDestinations.SettingPage -> {
                    var LocalSettings = ReadJson<LauncherConfig>(File("${LocalContext.current.filesDir}/${LAUNCHERCONFIGNAME}").readText())
                    Log.d("Text",File("${LocalContext.current.filesDir}/${LAUNCHERCONFIGNAME}").readText())
                    var lc = LocalContext.current
                    Column(modifier = Modifier.padding(10.dp,35.dp)){
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp))
                        {
                            Text(
                                "设置",
                                fontWeight = Bold,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.CenterStart),
                                fontSize = 28.sp
                            )
                        }
                        val scrollState = rememberScrollState()
                        Column(Modifier.fillMaxSize().verticalScroll(scrollState))
                        {
                            if(false)
                            {
                                Column(modifier = Modifier
                                    .padding(10.dp, 2.dp)
                                )
                                {
                                    Text("主题设置", fontWeight = FontWeight.Bold,modifier = Modifier.padding(0.dp), fontSize = 18.sp)
                                    XW_Switch("自动切换主题", modifier = Modifier.padding(0.dp),LocalSettings.UseSystemTheme,{ isChecked ->
                                        LocalSettings.UseSystemTheme = isChecked

                                        WriteJson<LauncherConfig>(LAUNCHERCONFIGNAME,LocalSettings,lc)

                                    })
                                    XW_Switch("启用深色模式", modifier = Modifier.padding(0.dp),LocalSettings.UseDarkTheme,{  isChecked ->
                                        LocalSettings.UseDarkTheme = isChecked
                                        WriteJson<LauncherConfig>(LAUNCHERCONFIGNAME,LocalSettings,lc)
                                    })
                                    Button(modifier = Modifier.padding(5.dp),onClick = {

                                    }){
                                        Text("选择主题色")
                                    }
                                }
                            }
                            Column(modifier = Modifier.padding(10.dp,2.dp))
                            {
                                Text("标题设置", fontWeight = Bold,modifier = Modifier.padding(0.dp), fontSize = 18.sp)
                                XW_Switch("启用英文版标题", modifier = Modifier.padding(0.dp),LocalSettings.UseEnglishTitle,{  isChecked ->
                                    LocalSettings.UseEnglishTitle = isChecked
                                    WriteJson<LauncherConfig>(LAUNCHERCONFIGNAME,LocalSettings,lc)
                                })
                            }
                            Column(modifier = Modifier.padding(10.dp,2.dp))
                            {
                                Text("更新设置", fontWeight = Bold,modifier = Modifier.padding(0.dp), fontSize = 18.sp)
                                XW_Switch("启动时检测更新", modifier = Modifier.padding(0.dp),LocalSettings.StartUpCheckUpdate,{  isChecked ->
                                    LocalSettings.StartUpCheckUpdate = isChecked
                                    WriteJson<LauncherConfig>(LAUNCHERCONFIGNAME,LocalSettings,lc)
                                })
                            }
                        }
                    }
                }
                AppDestinations.AboutPage -> {
                    Column(modifier = Modifier.padding(10.dp,35.dp)){
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp))
                        {
                            Text(
                                "关于",
                                fontWeight = Bold,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.CenterStart),
                                fontSize = 28.sp
                            )
                        }
                        val scrollState = rememberScrollState()
                        Column(Modifier.fillMaxSize().verticalScroll(scrollState))
                        {
                            Card(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                            ) {
                                Column(
                                    Modifier
                                        .padding(2.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                )
                                {

                                    Image(
                                        painter = painterResource(id = R.drawable.ic_appicon_vector),
                                        contentDescription = "AppIcon",
                                        modifier = Modifier.size(150.dp),

                                        )

                                    Row()
                                    {
                                        Text(
                                            "PvzLauncher for Android",
                                            fontSize = 24.sp,
                                            fontWeight = Bold
                                        )

                                    }


                                    Row(verticalAlignment = Alignment.CenterVertically)
                                    {
                                        Row()
                                        {

                                            Text(
                                                "版本：",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                modifier = Modifier.padding(2.dp)
                                            )
                                            Text(
                                                text = APP_VERSION,
                                                fontSize = 14.sp,
                                                fontWeight = Bold,
                                                modifier = Modifier.padding(2.dp)
                                            )
                                        }
                                        val context = LocalContext.current
                                        Button(
                                            onClick = {
                                                CheckUpdate(context,false)
                                            }, modifier = Modifier
                                                .padding(5.dp)
                                                .size(48.dp), contentPadding = PaddingValues(0.dp),
                                            shape = CircleShape
                                        )
                                        {

                                            Icon(
                                                imageVector = Icons.Default.Upload,
                                                "检测更新",
                                                modifier = Modifier.size(32.dp)
                                            )

                                        }
                                    }
                                    Row()
                                    {
                                        val cont = LocalContext.current
                                        Button(onClick = {
                                            OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android/issues/new",cont)
                                        }, modifier = Modifier.padding(2.dp))
                                        {

                                            Text("反馈漏洞")
                                        }

                                        Button(onClick = {
                                            //OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android/blob/main/Assets/EULA.md",cont)
                                            try {
                                                MDR_FileName = "许可协议"
                                                MDR_MDContent =
                                                    GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/Files/EULA.md")
                                                currentDestination = AppDestinations.MDReaderPage
                                            } catch (e: Exception) {
                                                XW_ToastMessage("无法读取EULA信息,${e.message}", lc)
                                            }

                                        }, modifier = Modifier.padding(2.dp))
                                        {

                                            Text("许可协议")
                                        }

                                        Button(onClick = {
                                            //OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android/blob/main/Assets/QandA.md",cont)
                                            try {
                                                MDR_FileName = "常见问题"
                                                MDR_MDContent =
                                                    GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/Files/QandA.md")
                                                currentDestination = AppDestinations.MDReaderPage
                                            } catch (e: Exception) {
                                                XW_ToastMessage("无法读取常见问题信息,${e.message}", lc)
                                            }
                                        }, modifier = Modifier.padding(2.dp))
                                        {

                                            Text("常见问题")
                                        }


                                    }


                                }

                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                            )
                            {
                                Text("开发者", fontWeight = Bold, modifier = Modifier.padding(2.dp))
                                Text(
                                    "Xiaowang0229 - 主要开发人员",
                                    modifier = Modifier.padding(2.dp)
                                )
                                Text("原作者", fontWeight = Bold, modifier = Modifier.padding(2.dp))
                                Text(
                                    "ishuamouren - 启动器原作者",
                                    modifier = Modifier.padding(2.dp)
                                )
                                Text("贡献者", fontWeight = Bold, modifier = Modifier.padding(2.dp))
                                Text(
                                    "衷心感谢支持PvzLauncher的每一名用户！",
                                    modifier = Modifier.padding(2.dp)
                                )


                            }
                        }
                    }
                }

                AppDestinations.ManageDetailPage ->
                {
                    val all = ReadJson<SaveConfigList>(File("${LocalContext.current.filesDir}/${SAVECONFIGNAME}").readText())
                    val current = all.GameIndex[ManageIndex]
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 20.dp, 10.dp, 20.dp))
                    {
                        Row(Modifier.align(Alignment.TopStart),verticalAlignment = Alignment.CenterVertically){
                            Button(
                                onClick = {
                                    currentDestination = AppDestinations.ManagePage
                                },
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(48.dp)
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
                            Text(
                                "游戏信息",
                                fontWeight = Bold,
                                modifier = Modifier
                                    .padding(5.dp),
                                fontSize = 28.sp
                            )
                        }
                        val scrollState = rememberScrollState()
                        Column(modifier = Modifier
                            .padding(0.dp, 65.dp, 0.dp, 0.dp)
                            .fillMaxSize().verticalScroll(scrollState))
                        {
                            var lc = LocalContext.current
                            Row(modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth())
                            {

                                XW_ManageInformationCard(
                                    args = current,
                                    onBack = {

                                    }
                                    , IsButtonEnable = false
                                )
                            }
                            Row(Modifier
                                .padding(2.dp)
                                .fillMaxWidth())
                            {
                                var isDialogVisible by remember { mutableStateOf(false) }

                                var isDialogVisible2 by remember { mutableStateOf(false) }

                                Button(onClick = {
                                    var kk = ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}").readText())
                                    kk.CurrentGameIndex = ManageIndex
                                    WriteJson("${LAUNCHERCONFIGNAME}",kk,lc)
                                    XW_ToastMessage("操作成功",lc)
                                    currentDestination = AppDestinations.ManagePage
                                },Modifier.padding(2.dp)) {
                                    Text("设为活动")
                                }
                                Button(onClick = {
                                    isDialogVisible = true


                                },Modifier.padding(2.dp)) {
                                    Text("删除游戏", color = Color.Red)
                                }
                                XW_InputDialog(
                                    showDialog = isDialogVisible,
                                    title = "为防止误触，请重新输入“${current.GameName}”来确认删除",
                                    placeholder = "请输入",
                                    onDismiss = { isDialogVisible = false },
                                    value = "",
                                    onConfirm = { text ->
                                        if(text == current.GameName)
                                        {
                                            uninstallApk(lc,current.PackageName)
                                            var a2 = all.GameIndex.toMutableList()
                                            a2.removeAt(ManageIndex)
                                            all.GameIndex = a2.toList()
                                            WriteJson(SAVECONFIGNAME,all,lc)

                                            XW_ToastMessage("操作成功",lc)
                                            currentDestination = AppDestinations.ManagePage
                                        }
                                    }
                                )
                                Button(onClick = {
                                    isDialogVisible2 = true
                                },Modifier.padding(2.dp)) {
                                    Text("更改名称")
                                }
                                XW_InputDialog(
                                    showDialog = isDialogVisible2,
                                    title = "请输入新名字",
                                    placeholder = "请输入",
                                    onDismiss = { isDialogVisible2 = false },
                                    value = "",
                                    onConfirm = { text ->

                                        all.GameIndex[ManageIndex].GameName = text
                                        WriteJson(SAVECONFIGNAME,all,lc)

                                        XW_ToastMessage("操作成功",lc)
                                        currentDestination = AppDestinations.ManagePage
                                    })
                            }
                            Column(Modifier.padding(5.dp))
                            {
                                Text("入库时间:${current.AddTime}")
                                Text("启动次数:${current.LaunchTimes}")
                            }
                        }

                    }

                }
                AppDestinations.DownloadDetailPage ->
                {
                    var lc = LocalContext.current

                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 20.dp, 10.dp, 20.dp))
                        {
                            Row(Modifier.align(Alignment.CenterStart), verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = {
                                        currentDestination = AppDestinations.DownloadPage
                                    },
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(48.dp)
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
                                Text(
                                    "游戏信息",
                                    fontWeight = Bold,
                                    modifier = Modifier
                                        .padding(5.dp),
                                    fontSize = 28.sp
                                )
                            }

                            Button(onClick = {
                                currentDestination = AppDestinations.TaskPage
                            }, modifier = Modifier
                                .padding(5.dp)
                                .size(48.dp)
                                .align(Alignment.CenterEnd),contentPadding = PaddingValues(0.dp),
                                shape = CircleShape
                            )
                            {

                                Icon(imageVector = Icons.Default.Downloading,"检测更新", modifier = Modifier.size(32.dp))

                            }
                        }
                    val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .padding(0.dp, 85.dp, 0.dp, 0.dp)
                                .fillMaxWidth().verticalScroll(scrollState)
                        )
                        {
                            val scrollState = rememberScrollState()
                            var lc = LocalContext.current
                            var isDialogVisible by remember { mutableStateOf(false) }
                            var resultText by remember { mutableStateOf(DownloadConfig.GameName) }
                            XW_InputDialog(
                                showDialog = isDialogVisible,
                                title = "请输入游戏名",
                                placeholder = "${DownloadConfig.GameName}",
                                onDismiss = { isDialogVisible = false },
                                value = DownloadConfig.GameName + DownloadConfig.GameLink[DownloadCount].VersionName,
                                onConfirm = { text ->
                                    // 这里处理你拿到的输入内容
                                    resultText = text
                                    try
                                    {


                                        var dlc = DownloadConfig
                                        dlc.GameName = resultText
                                        var pid = 0
                                        var cprsc = ProcessConfig(
                                            p_id = pid,
                                            p_info = dlc,

                                            )
                                        ProcessList.add(cprsc)
                                        intProcessList.add(pid)
                                        intProcessProgressList.add(0.toFloat())
                                        sProcessProgressList.add("0%")



                                        pid = PRDownloader.download(
                                            dlc.GameLink[DownloadCount].VersionLink,
                                            "${lc.filesDir}",
                                            "${dlc.GameName}.apk"
                                        )
                                            .build()
                                            .setOnProgressListener { progress ->
                                                intProcessProgressList[intProcessList.indexOf(pid)] = ((progress.currentBytes * 100 / progress.totalBytes).toFloat())
                                                sProcessProgressList[intProcessList.indexOf(pid)] = ((progress.currentBytes * 100 / progress.totalBytes).toString()) + "%"
                                            }
                                            .start(object : OnDownloadListener {
                                                override fun onDownloadComplete() {
                                                    XW_ToastMessage("下载 ${ProcessList[intProcessList.indexOf(pid)].p_info.GameName} 完成",lc)


                                                    try {
                                                        installApk(lc,File("${lc.filesDir}/${dlc.GameName}.apk"))
                                                        var sl = ReadJson<SaveConfigList>(File("${lcc.filesDir}/${SAVECONFIGNAME}").readText())
                                                        sl.GameIndex += SaveConfig(
                                                            GameName =ProcessList[intProcessList.indexOf(pid)].p_info.GameName,
                                                            PackageName = "${lc.packageManager.getPackageArchiveInfo("${lc.filesDir}/${dlc.GameName}.apk",0)?.packageName}",
                                                            AddTime = ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                                                            PlayTime = 0,
                                                            LaunchTimes = 0,
                                                            headImage = ProcessList[intProcessList.indexOf(pid)].p_info.GameImage,
                                                            gameversion = ProcessList[intProcessList.indexOf(pid)].p_info.GameLink[DownloadCount].VersionVer

                                                        )
                                                        WriteJson<SaveConfigList>(SAVECONFIGNAME,sl,lcc)

                                                    }
                                                    catch (e : Exception)
                                                    {
                                                        XW_ToastMessage("安装失败：${e.message}",lc)
                                                    }
                                                    intProcessProgressList.removeAt(index = intProcessList.indexOf(pid))
                                                    sProcessProgressList.removeAt(intProcessList.indexOf(pid))
                                                    ProcessList.removeAt(index = intProcessList.indexOf(pid))
                                                    intProcessList.remove(pid)

//

                                                }

                                                override fun onError(error: Error?) {
                                                    // 下载失败
                                                    XW_ToastMessage("下载出错: ${error?.serverErrorMessage}",lc)
                                                }


                                            })





                                        ProcessList[ProcessList.count() - 1].p_id = pid
                                        intProcessList[ProcessList.count() -1] = pid
                                    }
                                    catch(e : Exception)
                                    {
                                        XW_ToastMessage("${e.message}",lc)
                                    }
                                    XW_ToastMessage("成功创建下载任务", lc)
                                }
                            )
                            Box(modifier = Modifier.padding(5.dp))
                            {
                                XW_GameInformationCard(DownloadConfig, {
                                    isDialogVisible = true

                                }, true, Icons.Default.Download,true)
                            }
                            Text(
                                "简介",
                                fontSize = 22.sp,
                                fontWeight = Bold,
                                modifier = Modifier.padding(10.dp, 5.dp)
                            )
                            Text(
                                DownloadConfig.GameDescription,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(10.dp, 5.dp)
                            )
                            Text(
                                "游戏截图",
                                fontSize = 22.sp,
                                fontWeight = Bold,
                                modifier = Modifier.padding(10.dp, 5.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .horizontalScroll(scrollState)
                            )
                            {

                                for (i in DownloadConfig.ScreenShoot) {
                                    AsyncImage(
                                        model = i, "", modifier = Modifier
                                            .height(200.dp)
                                            .width(360.dp)
                                    )
                                }
                            }


                        }

                }
                AppDestinations.TaskPage ->
                {
                    TopAppBar(
                        title = {
                            Text(
                                "任务",
                                fontWeight = Bold,
                                modifier = Modifier.padding(5.dp),
                                fontSize = 28.sp
                            )
                        },
                        navigationIcon = {
                            Button(onClick = {
                                currentDestination = AppDestinations.DownloadPage
                            }, modifier = Modifier
                                .padding(5.dp)
                                .size(48.dp)
                                .background(Color.Transparent),contentPadding = PaddingValues(0.dp),
                                shape = CircleShape

                            )
                            {

                                Icon(imageVector = Icons.Default.ArrowBack,"返回", modifier = Modifier.size(32.dp))

                            }
                        }
                    )
                    var lc = LocalContext.current
                    val scrollState = rememberScrollState()
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 90.dp, 10.dp, 10.dp).verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        ProcessList.forEach {
                            procfg ->
                            Card(modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth())
                            {
                                Box(modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth())
                                {
                                    val scope = rememberCoroutineScope()
                                    Button(onClick = {
                                        //PRDownloader.cancel(downloadId = Procfg.p_id)


                                        PRDownloader.cancel(procfg.p_id)
                                        scope.launch {
                                            delay(100) // 💡 异步等待 0.5 秒
                                            intProcessProgressList.removeAt(index = intProcessList.indexOf(procfg.p_id))
                                            sProcessProgressList.removeAt(intProcessList.indexOf(procfg.p_id))
                                            ProcessList.removeAt(index = intProcessList.indexOf(procfg.p_id))
                                            intProcessList.remove(procfg.p_id)
                                        }




                                    }, modifier = Modifier
                                        .padding(5.dp)
                                        .align(Alignment.CenterEnd)
                                        .size(48.dp),contentPadding = PaddingValues(0.dp),
                                        shape = CircleShape
                                    )

                                    {

                                        Icon(imageVector = Icons.Default.Delete,"检测更新", modifier = Modifier.size(32.dp))

                                    }


                                    Column(modifier = Modifier
                                        .align(Alignment.Center)
                                        .fillMaxWidth())
                                    {

                                        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth())
                                        {
                                            AsyncImage(model = procfg.p_info.GameImage,"",modifier = Modifier
                                                .padding(5.dp)
                                                .size(32.dp))
                                            Column(Modifier.padding(2.dp))
                                            {
                                                Text("下载 ${procfg.p_info.GameName}", fontSize = 22.sp,modifier = Modifier.padding(2.dp), fontWeight = FontWeight.Bold)
                                                Text("pid:${procfg.p_id}",fontSize = 14.sp,modifier = Modifier.padding(2.dp))
                                            }
                                        }
                                        Column (modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth())
                                        {
                                            Row(Modifier.fillMaxWidth()) {
                                                Text("下载中……")
                                                Text(sProcessProgressList[intProcessList.indexOf(procfg.p_id)])
                                            }
                                            LinearProgressIndicator(intProcessProgressList[intProcessList.indexOf(procfg.p_id)] / 100,Modifier.fillMaxWidth())

                                        }
                                    }


                                }
                            }

                        }




                    }
                }
                AppDestinations.MDReaderPage ->
                {
                    TopAppBar(
                        title = {
                            Text(
                                text = MDR_FileName,
                                fontWeight = Bold,
                                modifier = Modifier.padding(5.dp),
                                fontSize = 28.sp
                            )
                        },
                        navigationIcon = {
                            Button(onClick = {
                                currentDestination = AppDestinations.AboutPage
                            }, modifier = Modifier
                                .padding(5.dp)
                                .size(48.dp)
                                .background(Color.Transparent),contentPadding = PaddingValues(0.dp),
                                shape = CircleShape

                            )
                            {

                                Icon(imageVector = Icons.Default.ArrowBack,"返回", modifier = Modifier.size(32.dp))

                            }
                        }
                    )
                    MarkdownText(MDR_MDContent.trimIndent(),modifier = Modifier
                        .padding(10.dp, 90.dp, 10.dp, 10.dp)
                        .fillMaxSize())
                }

                AppDestinations.ImportPage -> {
                    var lc = LocalContext.current
                    TopAppBar(
                        title = {
                            Text(
                                "导入",
                                fontWeight = Bold,
                                modifier = Modifier.padding(5.dp),
                                fontSize = 28.sp
                            )
                        },
                        navigationIcon = {
                            Button(onClick = {
                                currentDestination = AppDestinations.ManagePage
                            }, modifier = Modifier
                                .padding(5.dp)
                                .size(48.dp)
                                ,contentPadding = PaddingValues(0.dp),
                                shape = CircleShape

                            )
                            {

                                Icon(imageVector = Icons.Default.ArrowBack,"返回", modifier = Modifier.size(32.dp))

                            }
                        }
                    )
                    val scrollState = rememberScrollState()
                    Column(Modifier
                        .padding(10.dp, 90.dp, 10.dp, 10.dp)
                        .fillMaxSize().verticalScroll(scrollState))
                    {
                        var gameindex = emptyList<android.content.pm.PackageInfo>().toMutableStateList()
                       try{
                          gameindex = lc.packageManager.getInstalledPackages(0).toMutableStateList()


                       }
                       catch(e:Exception)
                       {
                           XW_ToastMessage("无法获取到游戏索引,${e.message}",lc)
                       }


                        gameindex.forEach { i ->

                                if((lc.packageManager.getApplicationInfo(i.packageName,0).flags and ApplicationInfo.FLAG_SYSTEM) == 0)
                                {
                                    var tempname = lc.packageManager.getApplicationLabel(lc.packageManager.getApplicationInfo(i.packageName,0)).toString()
                                    if(isAppInstalled(lc,i.packageName) && ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex.none {it.PackageName == i.packageName})
                                    {
                                        Card(Modifier
                                            .padding(2.dp)
                                            .fillMaxWidth())
                                        {
                                            Box(Modifier
                                                .padding(5.dp)
                                                .fillMaxWidth())
                                            {
                                                Column(Modifier.align(Alignment.CenterStart).padding(10.dp,10.dp,64.dp,10.dp))
                                                {
                                                    Row(verticalAlignment = Alignment.CenterVertically)
                                                    {
                                                        AsyncImage(model = lc.packageManager.getApplicationIcon(i.packageName),"", modifier = Modifier.padding(10.dp,5.dp).size(48.dp),placeholder = painterResource(
                                                            R.drawable.ic_unknown), error = painterResource(R.drawable.ic_unknown),onError = { state -> XW_ToastMessage("获取头图时发生错误：${state.result.throwable.message}",lc)})


                                                        Column(Modifier.padding(2.dp))
                                                        {
                                                            Text(lc.packageManager.getApplicationLabel(lc.packageManager.getApplicationInfo(i.packageName,0)).toString(), fontWeight = Bold)

                                                        }
                                                    }
                                                    OutlinedTextField(label = { Text("请输入导入后的游戏名") }, onValueChange = { t-> tempname = t },value = lc.packageManager.getApplicationLabel(lc.packageManager.getApplicationInfo(i.packageName,0)).toString())
                                                }

                                                Button(onClick = {
                                                    var aaa=ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText())
                                                    aaa.GameIndex += SaveConfig(
                                                        GameName =tempname ,
                                                        PackageName = i.packageName,
                                                        AddTime = ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                                                        PlayTime = 0,
                                                        LaunchTimes = 0,
                                                        headImage = "https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameAssets/Import.png",
                                                        gameversion = GetApkInfo(i.packageName,lcc).versionName ?: "1.0.0")
                                                    WriteJson<SaveConfigList>(SAVECONFIGNAME,aaa,lc)
                                                    gameindex.remove(i)
                                                    XW_ToastMessage("导入成功",lc)




                                                }, modifier = Modifier
                                                    .padding(5.dp)
                                                    .align(Alignment.CenterEnd)
                                                    .size(48.dp),contentPadding = PaddingValues(0.dp),
                                                    shape = CircleShape
                                                )

                                                {

                                                    Icon(imageVector = Icons.Default.ArrowRightAlt,"检测更新", modifier = Modifier.size(32.dp))

                                                }
                                            }
                                        }
                                    }
                                }

                        }
                    }
                }

            }

        }
    }
}



enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HomePage(label="启动",icon=Icons.Default.Rocket),
    ManagePage(label="管理",icon=Icons.Default.VideogameAsset),
    DownloadPage(label="下载",icon=Icons.Default.Download),
    SettingPage(label="设置",icon=Icons.Default.Settings),
    AboutPage(label="关于",icon=Icons.Default.Info),

    ManageDetailPage(label="ManageDetail",icon=Icons.Default.QuestionMark),
    DownloadDetailPage(label="DownloadDetail",icon=Icons.Default.QuestionMark),
    TaskPage(label="Tasks",icon=Icons.Default.QuestionMark),
    MDReaderPage(label="MDReader",icon=Icons.Default.QuestionMark),
    ImportPage(label="Import",icon=Icons.Default.QuestionMark)
}

