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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.pvzlauncher.pvzlauncher.ui.theme.XW_DarkTheme
import com.pvzlauncher.pvzlauncher.ui.theme.XW_LightTheme
import com.pvzlauncher.pvzlauncher.utils.UseDarkTheme
import com.pvzlauncher.pvzlauncher.utils.checkedupdate
import com.pvzlauncher.pvzlauncher.utils.checkinternetconnect
import com.pvzlauncher.pvzlauncher.utils.requestappinstall
import kotlinx.coroutines.launch


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
    ImportPage(label="",icon=Icons.Default.QuestionMark)
}

@Composable
fun InitializeAppInterface()
{

    var lc = LocalContext.current
    globalContext = LocalContext.current
    val scope = rememberCoroutineScope()
    APP_VERSION = (lc.packageManager.getPackageInfo(lc.packageName,0).versionName ?: "1.0.0")
    LaunchedEffect(Unit) {
        val dir = File("${lc.filesDir}/temp")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        if(!requestappinstall.value)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                if(!lc.packageManager.canRequestPackageInstalls())
                {


                    XW_simpledialog(
                        "权限申请(2/2)",
                        "本程序需要申请“应用内安装应用”权限来帮助您安装各版本Pvz和提供游戏，自身升级包，请您同意此权限来使用此程序",
                        {



                            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                                data = Uri.parse("package:${lc.packageName}")
                            }

                            intent.data = Uri.parse(
                                "package:${lc.packageName}"
                            )

                            lc.startActivity(intent)

                        },
                        {},
                        lc,scope
                    )
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if(!Environment.isExternalStorageManager())
                {
                    XW_simpledialog("权限申请(1/2)","此程序需要申请读取存储权限来管理并安装您的游戏obb，请您批准！",{
                        val intent = Intent(
                            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                        )

                        intent.data = Uri.parse(
                            "package:${lc.packageName}"
                        )

                        lc.startActivity(intent)
                    },{},lc,scope)

                }
            }
            else
            {
                requestappinstall.value = true
            }
        }
        try{
            scope.launch {
                ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}"))
            }
        }
        catch(e:Exception) {
            scope.launch {
                WriteJson<LauncherConfig>(
                    LAUNCHERCONFIGNAME, LauncherConfig(
                        UseSystemTheme = true,
                        UseDarkTheme = false,
                        UseEnglishTitle = false,
                        CurrentGameIndex = CurrentIndex(0,0),
                        true, false,false
                    ), lc
                )
            }
        }

        try{
            scope.launch {
                val a = ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"))
                if(ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}")).ListIndex.count() < 1)
                {
                    WriteJson<SaveConfigList>(SAVECONFIGNAME, SaveConfigList(listOf(FavoriteListsConfig("默认收藏夹",emptyList<SaveConfig>()))),lc)
                }
            }

        }
        catch(e:Exception) {
            WriteJson<SaveConfigList>(SAVECONFIGNAME, SaveConfigList(listOf(FavoriteListsConfig("默认收藏夹",emptyList<SaveConfig>()))),lc)
        }


        val config = PRDownloaderConfig.newBuilder()
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(lc, config)

        refreshInstalledapplist(lc)

        if(checkinternetconnect.value == false)
        {

            try
            {
                GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/UpdateIndex.json")
                RefreshGamelist(lc,scope)
            }
            catch(e:Exception){
                XW_simpledialog("警告！","无法访问上游数据库，将无法进行游戏的下载与安装，是否退出程序？\r\n错误信息：${e.stackTraceToString()}",{System.exit(0)},{},lc,scope)
            }
            checkinternetconnect.value = true
        }
        if(checkedupdate.value == false)
        {
            scope.launch {
                if(ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}")).StartUpCheckUpdate == true)
                {
                    CheckUpdate(lc,true,scope)
                }
                checkedupdate.value = true
            }

        }
    }


}

