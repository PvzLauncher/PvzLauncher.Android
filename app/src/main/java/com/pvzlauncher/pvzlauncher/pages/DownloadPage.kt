package com.pvzlauncher.pvzlauncher.pages

import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.Downloadlist
import com.pvzlauncher.pvzlauncher.utils.GameListConfig
import com.pvzlauncher.pvzlauncher.utils.GetWebSiteContent
import com.pvzlauncher.pvzlauncher.utils.OpenUrl
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.controls.XW_GameInformationCard
import com.pvzlauncher.pvzlauncher.controls.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.controls.XW_simpledialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.pvzlauncher.pvzlauncher.ui.theme.XW_LightTheme
import com.pvzlauncher.pvzlauncher.utils.GameKindsConfig
import com.pvzlauncher.pvzlauncher.utils.ReadJsonfromText


@Composable
public fun DownloadPage()
{
    var isRendered by rememberSaveable{ mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var lc = LocalContext.current
    Box(Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.padding(10.dp, 35.dp, 10.dp, 5.dp)) {
            Box(modifier = Modifier.fillMaxWidth())
            {
                Text(
                    "下载", fontWeight = Bold, modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.CenterStart), fontSize = 24.sp
                )
                Row(Modifier.align(Alignment.CenterEnd))
                {
                    TextButton(
                        onClick = {
                            OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android/issues/new?template=download.yml",lc)

                        },
                        modifier = Modifier
                            .padding(5.dp)
                            .size(32.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = CircleShape
                    )
                    {

                        Icon(
                            imageVector = Icons.Default.Upload,
                            "检测更新",
                            modifier = Modifier.size(32.dp)
                        )

                    }
                    TextButton(
                        onClick = {
                            isRendered = false
                            RefreshGamelist(lc)
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

            if (isRendered) {

                    Downloadlist.value()

            }
        }
        FloatingActionButton(
            onClick = {
                CurrentDestination = AppDestinations.TaskPage
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.align(Alignment.BottomEnd).padding(30.dp).size(48.dp), containerColor = XW_LightTheme.primary
        )
        {

            Icon(
                imageVector = Icons.Default.Task,
                "检测更新",
                modifier = Modifier.size(32.dp)
            )

        }

    }
}

public fun RefreshGamelist(lc : Context)
{

    try {
        GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameIndex.json")
    }
    catch (e:Exception)
    {
        Downloadlist =
            mutableStateOf(@Composable{
                Box(Modifier.fillMaxSize())
                {
                    Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("无法获取到游戏索引",fontSize = 18.sp, fontWeight = Bold)
                        Text("请稍后重试", fontSize = 14.sp)
                    }
                }
            })

        return
    }
    Downloadlist =  mutableStateOf(@Composable{
        val scrollState = rememberScrollState()
        var search by rememberSaveable {mutableStateOf("")}
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier

                .fillMaxSize()
                .verticalScroll(scrollState)
        )
        {
            var gameindex = emptyList<GameKindsConfig>()
            try {
                gameindex =
                    ReadJsonfromText<GameListConfig>(GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameIndex.json")).ListIndex

            } catch (e: Exception) {

            }
            var selecteditem by rememberSaveable { mutableStateOf(0) }
            SecondaryScrollableTabRow(selecteditem)
            {
                if (gameindex.count() != 0) {
                    gameindex.forEachIndexed { i, c ->
                        Tab(
                            selected = selecteditem == i,
                            onClick = { selecteditem = i },
                            text = { Text(c.kindname) }
                        )
                    }
                }
                else
                {
                    Box(Modifier.fillMaxSize())
                    {
                        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("游戏索引为空",fontSize = 18.sp, fontWeight = Bold)
                            Text("请稍后重试", fontSize = 14.sp)
                        }
                    }
                }

            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically)
            {
                Icon(
                    imageVector = Icons.Default.Search,
                    "检测更新",
                    modifier = Modifier.size(32.dp)
                )
                TextField(value = search, onValueChange = {search = it},Modifier.weight(1f).padding(5.dp), textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp
                ))
            }
            if(gameindex[selecteditem].GameIndex.count() != 0)
            {

                for (j in gameindex[selecteditem].GameIndex) {
                    if(j.GameName.contains(search))
                    {
                        XW_GameInformationCard(j, {
                            CurrentDestination = AppDestinations.DownloadDetailPage
                        }, false, Icons.Default.ArrowForward, false)
                    }
                }
                OutlinedCard(modifier = Modifier.padding(5.dp).fillMaxWidth())
                {
                    Box(modifier = Modifier.padding(5.dp).fillMaxWidth())
                    {
                        Row(
                            modifier = Modifier.align(Alignment.CenterStart)
                                .padding(10.dp, 10.dp, 64.dp, 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            val cont = LocalContext.current
                            AsyncImage(
                                model = "https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameAssets/Origin.png",
                                "",
                                modifier = Modifier.padding(10.dp, 5.dp)
                                    .size(48.dp),
                                placeholder = painterResource(
                                    R.drawable.ic_unknown
                                ),
                                error = painterResource(R.drawable.ic_unknown),
                                onError = { state ->
                                    XW_ToastMessage(
                                        "获取头图时发生错误：${state.result.throwable.message}",
                                        cont
                                    )
                                })
                            Column()
                            {
                                Text(
                                    "更多游戏",
                                    modifier = Modifier.padding(2.dp),
                                    fontWeight = Bold,
                                    fontSize = 18.sp
                                )
                                Column(modifier = Modifier.padding(2.dp))
                                {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Color(0xFFA9A9A9),
                                                RoundedCornerShape(7.5.dp)
                                            )
                                            .padding(4.dp, 2.dp)
                                    )

                                    {
                                        Text(
                                            "200+个版本",
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )

                                    }
                                    Box(modifier = Modifier.padding(2.5.dp)) { }
                                    Box(
                                        modifier = Modifier.background(
                                            Color(
                                                0xFF31A9A9
                                            ), RoundedCornerShape(7.5.dp)
                                        )
                                            .padding(4.dp, 2.dp)
                                    )
                                    {
                                        Text(
                                            "55.42GB",
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )

                                    }
                                }
                            }
                        }


                        TextButton(
                            onClick = {
                                XW_simpledialog(
                                    "提示",
                                    "游戏库中的游戏为较冷门改版，需要您手动下载并在管理页面导入，即将跳转至浏览器完成下一步操作，请确保您可以通过小飞机网盘进行版本下载！",
                                    {
                                        OpenUrl(
                                            "https://share.feijipan.com/s/Tfef8jNo",
                                            lc
                                        )
                                    },
                                    {},
                                    lc
                                )

                            },
                            modifier = Modifier.align(Alignment.CenterEnd)
                                .padding(5.dp).size(48.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = CircleShape
                        )
                        {

                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                "检测更新",
                                modifier = Modifier.size(32.dp)
                            )

                        }


                    }
                }

            }
            else
            {
                Box(Modifier.fillMaxSize())
                {
                    Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("可下载列表为空",fontSize = 18.sp, fontWeight = Bold)
                        Text("请稍后重试", fontSize = 14.sp)
                    }
                }
            }

        }

    })
}