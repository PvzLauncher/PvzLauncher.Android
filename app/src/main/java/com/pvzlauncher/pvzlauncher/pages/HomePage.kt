package com.pvzlauncher.pvzlauncher.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.controls.LaunchAnimation
import com.pvzlauncher.pvzlauncher.controls.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.ui.theme.XW_LightTheme
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.utils.launchApp
import java.io.File

@Composable
public fun HomePage()
{   var lc = LocalContext.current
    var showAnimation by rememberSaveable {mutableStateOf(false)}
    val lcc = ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}"))
    Box(modifier = Modifier.fillMaxSize()) {
        if(lcc.CostumBackground)
        {
            AsyncImage(File("${lc.filesDir}/Background.png"),"", Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)
        }
        if (lcc.UseEnglishTitle) {
            AsyncImage(File("${lc.filesDir}/Title.png"),"",alignment = Alignment.TopCenter,
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxWidth())
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_apptitle_zh),
                "123",
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxWidth()
            )
        }

        if (ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}")).ListIndex[lcc.CurrentGameIndex.ListIndex].GameIndex.count() != 0)
        {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                if(showAnimation)
                {
                    LaunchAnimation {

                        val new =
                            ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"))
                        new.ListIndex[lcc.CurrentGameIndex.ListIndex].GameIndex[lcc.CurrentGameIndex.GameIndex].LaunchTimes += 1
                        WriteJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"), new, lc)
                        showAnimation = false
                        launchApp(lc, new.ListIndex[lcc.CurrentGameIndex.ListIndex].GameIndex[lcc.CurrentGameIndex.GameIndex].PackageName)

                    }
                }

                    FloatingActionButton(onClick = {
                        showAnimation = true
                    },Modifier.size(64.dp),containerColor = XW_LightTheme.primary)
                    { Icon(imageVector = Icons.Default.RocketLaunch,modifier= Modifier.size(32.dp), contentDescription =  "") }




            }
            Column(Modifier.align(Alignment.BottomStart).padding(15.dp))
            {
                Row()
                {
                    Text("当前游戏：", fontSize = 12.sp)

                    val cur = ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}")).CurrentGameIndex
                    val current =
                        ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}")).ListIndex[cur.ListIndex].GameIndex[cur.GameIndex]
                    Text(
                        "${current.GameName}",
                        fontSize = 12.sp,
                        fontWeight = Bold
                    )


                }
            }
        }
        else
        {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                    FloatingActionButton(onClick = {
                        XW_ToastMessage("请先下载或导入游戏")
                    },
                        Modifier.size(64.dp), containerColor = Color.Gray
                    )
                    { Icon(imageVector = Icons.Default.RocketLaunch,modifier= Modifier.size(32.dp), contentDescription = "") }



            }
            Column(Modifier.align(Alignment.BottomStart).padding(15.dp))
            {
                Text("提示：请先下载游戏或导入游戏", fontSize = 12.sp, fontWeight = Bold)
            }
        }
    }
}