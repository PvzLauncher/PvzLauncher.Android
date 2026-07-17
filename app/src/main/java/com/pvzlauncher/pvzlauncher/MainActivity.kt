package com.pvzlauncher.pvzlauncher

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
import androidx.compose.material3.Scaffold
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
import com.pvzlauncher.pvzlauncher.utils.XW_simpledialog
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
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
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
import com.pvzlauncher.pvzlauncher.ui.theme.XW_LightTheme
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.Downloadlist
import com.pvzlauncher.pvzlauncher.utils.GameConfig
import com.pvzlauncher.pvzlauncher.utils.GameListConfig
import com.pvzlauncher.pvzlauncher.utils.ManageIndex
import com.pvzlauncher.pvzlauncher.utils.OpenUrl
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.utils.XW_GameInformationCard
import com.pvzlauncher.pvzlauncher.utils.XW_ManageInformationCard
import com.pvzlauncher.pvzlauncher.utils.XW_ToastMessage


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
        }
    }
    val NaviColor = NavigationSuiteDefaults.itemColors(
        // 手机生态：底部导航栏颜色
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
    ) {
            AnimatedContent(
                targetState = CurrentDestination,
                modifier = Modifier
                    .fillMaxSize(),
                transitionSpec = {
                    val enterAnim = slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )

                    val exitAnim = slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
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
    ImportPage(label="",icon=Icons.Default.QuestionMark)
}

@Composable
fun InitializeAppInterface()
{
    var lc = LocalContext.current
    globalContext = LocalContext.current
    APP_VERSION = (lc.packageManager.getPackageInfo(lc.packageName,0).versionName ?: "1.0.0")
    var startupcheckupdate by rememberSaveable { mutableStateOf(false) }
    var requestappinstall by rememberSaveable { mutableStateOf(false) }
    var checkinternetconnect by rememberSaveable { mutableStateOf(false) }
    if(!requestappinstall)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if(!lc.packageManager.canRequestPackageInstalls())
            {
                val settingLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { _ ->
                    if (!lc.packageManager.canRequestPackageInstalls()) {
                        XW_simpledialog(
                            "警告",
                            "警告,您未开启此权限，程序可能无法正常使用！是否前往打开此权限？",
                            {
                                val intent =
                                    Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                                        data = Uri.parse("package:${lc.packageName}")
                                    }
                                // 启动跳转

                            },
                            {},
                            lc
                        )
                    }

                }

                XW_simpledialog(
                    "提示",
                    "本程序需要申请“应用内安装应用”权限来帮助您安装各版本Pvz和提供游戏，自身升级包，请您同意此权限来使用此程序",
                    {

                        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                            data = Uri.parse("package:${lc.packageName}")
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
                                        data = Uri.parse("package:${lc.packageName}")
                                    }
                                // 启动跳转
                                lc.startActivity(intent)
                            },
                            {},
                            lc
                        )
                    },
                    lc
                )
            }
        }
        else{
            requestappinstall = true
        }
    }
    if (!File("${LocalContext.current.filesDir}/${LAUNCHERCONFIGNAME}").exists())
    {
        WriteJson<LauncherConfig>(LAUNCHERCONFIGNAME, LauncherConfig(
            UseSystemTheme = true,
            UseDarkTheme = false,
            UseEnglishTitle = false,
            CurrentGameIndex = 0,
            true
        ), LocalContext.current
        )
    }
    if (!File("${LocalContext.current.filesDir}/${SAVECONFIGNAME}").exists())
    {
        WriteJson<SaveConfigList>(SAVECONFIGNAME, SaveConfigList(emptyList<SaveConfig>()),lc)
    }
    val config = PRDownloaderConfig.newBuilder()
        .setReadTimeout(30000)
        .setConnectTimeout(30000)
        .build()
    PRDownloader.initialize(LocalContext.current, config)
    refreshInstalledapplist(lc)
    if(checkinternetconnect == false)
    {

        try
        {
            GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/UpdateIndex.json")
            RefreshGamelist(lc)
        }
        catch(e:Exception){
            XW_simpledialog("警告！","无法访问上游数据库，将无法进行游戏的下载与安装，是否退出程序？\r\n错误信息：${e.stackTraceToString()}",{System.exit(0)},{},lc)
        }
        checkinternetconnect = true
    }
    if(startupcheckupdate == false)
    {
        if(ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}").readText()).StartUpCheckUpdate == true)
        {
            CheckUpdate(lc,true)
        }
        startupcheckupdate = true
    }
}

