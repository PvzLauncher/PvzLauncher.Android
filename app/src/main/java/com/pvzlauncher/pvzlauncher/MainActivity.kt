package com.pvzlauncher.pvzlauncher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.pvzlauncher.pvzlauncher.ui.theme.PvzLauncherAndroidTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.utils.GetWebSiteContent
import com.pvzlauncher.pvzlauncher.utils.OpenUrl
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.UpdateConfig
import com.pvzlauncher.pvzlauncher.utils.XW_GameInformationCard
import com.pvzlauncher.pvzlauncher.utils.XW_Switch
import com.pvzlauncher.pvzlauncher.utils.XW_ToastMessage

const val APP_VERSION = "1.0.0-alpha.2";

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HomePage) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
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

            // 用 when 表达式根据当前选中的 destination 切换不同的 UI
            when (currentDestination) {
                AppDestinations.HomePage -> {
                    Box(modifier = Modifier.fillMaxSize()){
                        Image(painter = painterResource(id=R.drawable.ic_apptitle_zh),"123",
                            alignment = Alignment.TopCenter, modifier = Modifier.padding(40.dp).fillMaxWidth())
                        Column(modifier = Modifier.align(Alignment.BottomCenter).padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            Button(onClick = {

                            })
                            {
                                Column()
                                {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically){

                                        Icon(imageVector = Icons.Default.RocketLaunch,"", modifier = Modifier.padding(5.dp).size(24.dp))
                                        Text("启动游戏",modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    }

                                }
                            }
                            Row()
                            {
                                Text("当前游戏：" , fontSize = 14.sp)
                                Text("CurrentGame", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                AppDestinations.ManagePage -> {
                    Column(modifier = Modifier.padding(10.dp,35.dp)){
                        Box(modifier = Modifier.fillMaxWidth().height(58.dp))
                        {
                            Text(
                                "管理",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(5.dp).align(Alignment.CenterStart),
                                fontSize = 28.sp
                            )
                        }
                        XW_GameInformationCard(
                            painterResource(R.drawable.ic_appicon_vector),
                            "Title",
                            "Description",
                            null
                        )
                    }
                }
                AppDestinations.DownloadPage -> {
                    Column(modifier = Modifier.padding(10.dp,35.dp)){
                        Box(modifier = Modifier.fillMaxWidth())
                        {
                            Text("下载", fontWeight = FontWeight.Bold,modifier = Modifier.padding(5.dp).align(Alignment.CenterStart), fontSize = 28.sp)
                            Button(onClick = {

                            }, modifier = Modifier.padding(5.dp).size(48.dp).align(Alignment.CenterEnd),contentPadding = PaddingValues(0.dp),
                                shape = CircleShape
                            )
                            {

                                Icon(imageVector = Icons.Default.Downloading,"检测更新", modifier = Modifier.size(32.dp))

                            }
                        }
                        XW_GameInformationCard(
                            painterResource(R.drawable.ic_appicon_vector),
                            "Title",
                            "Description",
                            null
                        )
                    }
                }
                AppDestinations.SettingPage -> {
                    Column(modifier = Modifier.padding(10.dp,35.dp)){
                        Box(modifier = Modifier.fillMaxWidth().height(58.dp))
                        {
                            Text(
                                "设置",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(5.dp).align(Alignment.CenterStart),
                                fontSize = 28.sp
                            )
                        }
                        Column(modifier = Modifier.padding(10.dp,2.dp))
                        {
                            Text("主题设置", fontWeight = FontWeight.Bold,modifier = Modifier.padding(0.dp), fontSize = 18.sp)
                            XW_Switch("自动切换主题", modifier = Modifier.padding(0.dp))
                            XW_Switch("启用深色模式", modifier = Modifier.padding(0.dp))
                        }
                        Column(modifier = Modifier.padding(10.dp,2.dp))
                        {
                            Text("标题设置", fontWeight = FontWeight.Bold,modifier = Modifier.padding(0.dp), fontSize = 18.sp)
                            XW_Switch("启用英文版标题", modifier = Modifier.padding(0.dp))
                        }
                    }
                }
                AppDestinations.AboutPage -> {
                    Column(modifier = Modifier.padding(10.dp,35.dp)){
                        Box(modifier = Modifier.fillMaxWidth().height(58.dp))
                        {
                            Text(
                                "关于",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(5.dp).align(Alignment.CenterStart),
                                fontSize = 28.sp
                            )
                        }
                        Card(Modifier.fillMaxWidth().padding(5.dp)){
                            Column(Modifier.padding(10.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
                            {

                                    Image(
                                        painter = painterResource(id=R.drawable.ic_appicon_vector),
                                        contentDescription = "AppIcon",
                                        modifier = Modifier.size(200.dp),

                                        )
                                    Row()
                                    {
                                        Text("PvzLauncher for Android", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                        Text("Post-Reset", fontSize = 10.sp,modifier= Modifier.padding(2.dp))
                                    }

                                Row(verticalAlignment = Alignment.CenterVertically)
                                {
                                    Row()
                                    {

                                        Text("版本：",fontSize=14.sp, fontWeight = FontWeight.Normal, modifier = Modifier.padding(2.dp))
                                        Text(text = APP_VERSION, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
                                    }
                                    val context = LocalContext.current
                                    Button(onClick = {
                                        try {
                                            val jsondata = GetWebSiteContent("https://raw.giteeusercontent.com/PvzLauncher/PvzLauncher.Service.Android/raw/main/UpdateIndex.json")
                                            val ConfigInline = ReadJson<UpdateConfig>(jsondata)
                                            if(ConfigInline.LatestVersion == APP_VERSION)
                                            {
                                                XW_ToastMessage("当前版本已经是最新版本",context)
                                            }
                                            else
                                            {
                                                XW_ToastMessage("当前版本不是最新版本",context)
                                            }
                                        }
                                        catch(e: Exception)
                                        {
                                            XW_ToastMessage("检测更新时遇到错误：${e.message}",context)
                                        }
                                    }, modifier = Modifier.padding(5.dp).size(48.dp),contentPadding = PaddingValues(0.dp),
                                        shape = CircleShape
                                    )
                                    {

                                        Icon(imageVector = Icons.Default.Upload,"检测更新", modifier = Modifier.size(32.dp))

                                    }
                                }
                                Row()
                                {
                                    val cont = LocalContext.current
                                    Button(onClick = {
                                        OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android/issues/new",cont)
                                    }, modifier = Modifier.padding(5.dp))
                                    {

                                        Text("反馈漏洞")
                                    }

                                    Button(onClick = {
                                        OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android/blob/main/Assets/EULA.md",cont)

                                    }, modifier = Modifier.padding(5.dp))
                                    {

                                        Text("许可协议")
                                    }

                                    Button(onClick = {
                                        OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android/blob/main/Assets/QandA.md",cont)
                                    }, modifier = Modifier.padding(5.dp))
                                    {

                                        Text("常见问题")
                                    }


                                }


                            }

                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(5.dp).fillMaxWidth())
                        {
                            Text("开发者", fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
                            Text("Xiaowang0229 - 主要开发人员", modifier = Modifier.padding(2.dp))
                            Text("版权方", fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
                            Text("ishuamouren - 启动器版权方", modifier = Modifier.padding(2.dp))
                            Text("贡献者", fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
                            Text("衷心感谢支持PvzLauncher的每一名用户！", modifier = Modifier.padding(2.dp))


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
    //HOME("主页", R.drawable.ic_home),
    HomePage(label="启动",icon=Icons.Default.Rocket),
    ManagePage(label="管理",icon=Icons.Default.VideogameAsset),
    DownloadPage(label="下载",icon=Icons.Default.Download),
    SettingPage(label="设置",icon=Icons.Default.Settings),
    AboutPage(label="关于",icon=Icons.Default.Info)


}

