package com.pvzlauncher.pvzlauncher

import android.app.AppOpsManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.jakewharton.threetenabp.AndroidThreeTen
import com.pvzlauncher.pvzlauncher.ui.theme.PvzLauncherAndroidTheme
import com.pvzlauncher.pvzlauncher.utils.APP_VERSION
import com.pvzlauncher.pvzlauncher.utils.GetWebSiteContent
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.controls.XW_simpledialog
import java.io.File
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.compose.animation.togetherWith
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.pvzlauncher.pvzlauncher.utils.CheckUpdate
import com.pvzlauncher.pvzlauncher.utils.globalContext
import com.pvzlauncher.pvzlauncher.pages.AboutPage
import com.pvzlauncher.pvzlauncher.pages.DownloadDetailPage
import com.pvzlauncher.pvzlauncher.pages.DownloadPage
import com.pvzlauncher.pvzlauncher.pages.HomePage
import com.pvzlauncher.pvzlauncher.pages.ImportPage
import com.pvzlauncher.pvzlauncher.pages.MDReaderPage
import com.pvzlauncher.pvzlauncher.pages.ManageDetailPage
import com.pvzlauncher.pvzlauncher.pages.ManagePage
import com.pvzlauncher.pvzlauncher.pages.RefreshGamelist
import com.pvzlauncher.pvzlauncher.pages.SettingPage
import com.pvzlauncher.pvzlauncher.pages.TaskPage
import com.pvzlauncher.pvzlauncher.pages.refreshInstalledapplist
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.CurrentIndex
import com.pvzlauncher.pvzlauncher.utils.FavoriteListsConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.utils.snackbarHostState
import android.content.Context
import android.os.Environment
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.pvzlauncher.pvzlauncher.controls.XW_MarkdownDialog
import com.pvzlauncher.pvzlauncher.pages.SavePage
import com.pvzlauncher.pvzlauncher.ui.theme.XW_DarkTheme
import com.pvzlauncher.pvzlauncher.ui.theme.XW_LightTheme
import com.pvzlauncher.pvzlauncher.utils.UseDarkTheme
import com.pvzlauncher.pvzlauncher.utils.checkedupdate
import com.pvzlauncher.pvzlauncher.utils.checkinternetconnect
import com.pvzlauncher.pvzlauncher.utils.hasUsageStatsPermission
import com.pvzlauncher.pvzlauncher.utils.requestappinstall
import com.pvzlauncher.pvzlauncher.utils.launchedgame


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        android.os.StrictMode.setThreadPolicy(android.os.StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        enableEdgeToEdge()
        setContent {
            if(UseDarkTheme.value)
            {
                    PvzLauncherAndroidTheme {
                        PvzLauncherAndroidApp()
                    }
            }
            else
            {
                    PvzLauncherAndroidTheme {
                        PvzLauncherAndroidApp()
                    }
            }
        }
    }




}

@PreviewScreenSizes
@Composable
fun PvzLauncherAndroidApp() {
    InitializeAppInterface()
    BackHandler{
        when(CurrentDestination)
        {
            AppDestinations.AboutPage -> CurrentDestination = AppDestinations.HomePage
            AppDestinations.DownloadDetailPage -> CurrentDestination = AppDestinations.DownloadPage
            AppDestinations.DownloadPage -> CurrentDestination = AppDestinations.HomePage
            AppDestinations.HomePage -> System.exit(0)
            AppDestinations.ImportPage -> CurrentDestination = AppDestinations.ManagePage
            AppDestinations.ManageDetailPage -> CurrentDestination = AppDestinations.ManagePage
            AppDestinations.ManagePage -> CurrentDestination = AppDestinations.HomePage
            AppDestinations.MDReaderPage -> CurrentDestination = AppDestinations.AboutPage
            AppDestinations.SettingPage -> CurrentDestination = AppDestinations.HomePage
            AppDestinations.TaskPage -> CurrentDestination = AppDestinations.DownloadPage
            AppDestinations.SavePage -> CurrentDestination = AppDestinations.ManageDetailPage
        }
    }
    val NaviColor = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = Color(0x66749D46),
            selectedIconColor = Color(0xFF63A002)
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            indicatorColor = Color(0x66749D46),
            selectedIconColor = Color(0xFF63A002)
        ),
    )


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
                        selected = it == CurrentDestination,
                        onClick = { CurrentDestination = it },
                        colors = NaviColor
                    )

                }
            }
        )
        {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        snackbarHostState
                    )
                }
            )
            {
                    p -> val a = p
                AnimatedContent(
                    targetState = CurrentDestination,
                    modifier = Modifier
                        .fillMaxSize(),
                    transitionSpec = {
                        val enterAnim = slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                        ) + fadeIn(
                            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                        )

                        val exitAnim = slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                        ) + fadeOut(
                            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                        )
                        enterAnim togetherWith exitAnim
                    },
                    label = "PageTransition"
                ) { targetPage ->
                    when (targetPage) {
                        AppDestinations.HomePage -> HomePage()
                        AppDestinations.ManagePage -> ManagePage()
                        AppDestinations.DownloadPage -> DownloadPage()
                        AppDestinations.SettingPage -> SettingPage()
                        AppDestinations.AboutPage -> AboutPage()
                        //--隐藏页面区域--
                        AppDestinations.ManageDetailPage -> ManageDetailPage()
                        AppDestinations.DownloadDetailPage -> DownloadDetailPage()
                        AppDestinations.TaskPage -> TaskPage()
                        AppDestinations.MDReaderPage -> MDReaderPage()
                        AppDestinations.ImportPage -> ImportPage()
                        AppDestinations.SavePage -> SavePage()


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

    //--隐藏页面区域--
    ManageDetailPage(label="",icon=Icons.Default.QuestionMark),
    DownloadDetailPage(label="",icon=Icons.Default.QuestionMark),
    TaskPage(label="",icon=Icons.Default.QuestionMark),
    MDReaderPage(label="",icon=Icons.Default.QuestionMark),
    ImportPage(label="",icon=Icons.Default.QuestionMark),

    SavePage(label="",icon=Icons.Default.QuestionMark)
}

@Composable
fun InitializeAppInterface()
{

    var lc = LocalContext.current
    globalContext = LocalContext.current
    APP_VERSION = (lc.packageManager.getPackageInfo(lc.packageName,0).versionName ?: "1.0.0")


    val dir = File("${lc.filesDir}/temp")
    if (!dir.exists()) {
        dir.mkdirs()
    }
    if(!requestappinstall.value)
    {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

        }
        XW_MarkdownDialog(lc,"权限申请","本程序需要申请以下权限来运行：","#### 1. 完整的网络访问权\r\n- 用途：用于下载，更新游戏，以及启动器检测更新\r\n#### 2. 请求删除应用程序\r\n- 用途：完全删除某个游戏版本时需要使用\r\n#### 3. 管理所有文件\r\n- 用途：管理游戏obb及存档\r\n#### 4. 请求安装此来源应用\r\n- 用途：用于安装启动器更新，游戏更新和游戏版本本体\r\n#### 5. 请求读取所有应用程序包\r\n- 用途：用于从所有应用中导入游戏版本\r\n#### 6.发送通知\r\n- 用途：在下载游戏完成时提醒您该安装了",{},{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                if(!lc.packageManager.canRequestPackageInstalls())
                {
                            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                                data = Uri.parse("package:${lc.packageName}")
                            }

                            intent.data = Uri.parse(
                                "package:${lc.packageName}"
                            )

                            lc.startActivity(intent)

                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if(!Environment.isExternalStorageManager())
                {
                    val intent = Intent(
                            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                        )

                        intent.data = Uri.parse(
                            "package:${lc.packageName}"
                        )

                        lc.startActivity(intent)


                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                if (ActivityCompat.checkSelfPermission(
                        lc,Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                        launcher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val channel = NotificationChannel(
                    "pvzlauncher",
                    "植物大战僵尸启动器",
                    NotificationManager.IMPORTANCE_HIGH
                )

                val manager = lc.getSystemService(
                    NotificationManager::class.java
                )

                manager.createNotificationChannel(channel)
            }
        })

            requestappinstall.value = true

    }
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
                true, false,false
            ), LocalContext.current
        )
    }

    try{
        val a = ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"))
        if(ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}")).ListIndex.count() < 1)
        {
            WriteJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"), SaveConfigList(listOf(FavoriteListsConfig("默认收藏夹",emptyList<SaveConfig>()))),lc)
        }

    }
    catch(e:Exception) {
        WriteJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"), SaveConfigList(listOf(FavoriteListsConfig("默认收藏夹",emptyList<SaveConfig>()))),lc)
    }


    val config = PRDownloaderConfig.newBuilder()
        .setReadTimeout(30000)
        .setConnectTimeout(30000)
        .build()
    PRDownloader.initialize(LocalContext.current, config)

    refreshInstalledapplist(lc)

    if(checkinternetconnect.value == false)
    {

        try
        {
            GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/UpdateIndex.json")
            RefreshGamelist(lc)
        }
        catch(e:Exception){
            XW_simpledialog("警告！","无法访问上游数据库，将无法进行游戏的下载与安装，是否退出程序？\r\n错误信息：${e.stackTraceToString()}",{System.exit(0)},{},lc)
        }
        checkinternetconnect.value = true
    }
    if(checkedupdate.value == false)
    {
        if(ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}")).StartUpCheckUpdate == true)
        {
            CheckUpdate(lc,true)
        }
        checkedupdate.value = true
    }
}

