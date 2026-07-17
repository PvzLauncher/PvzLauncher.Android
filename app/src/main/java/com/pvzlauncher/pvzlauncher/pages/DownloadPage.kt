package com.pvzlauncher.pvzlauncher.pages

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
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.GameConfig
import com.pvzlauncher.pvzlauncher.utils.GameListConfig
import com.pvzlauncher.pvzlauncher.utils.GetWebSiteContent
import com.pvzlauncher.pvzlauncher.utils.OpenUrl
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.XW_GameInformationCard
import com.pvzlauncher.pvzlauncher.utils.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.utils.XW_simpledialog

@Composable
public fun DownloadPage()
{
    var lc = LocalContext.current
    Column(modifier = Modifier.padding(10.dp, 35.dp, 10.dp, 5.dp)) {
        Box(modifier = Modifier.fillMaxWidth())
        {
            Text(
                "下载", fontWeight = Bold, modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.CenterStart), fontSize = 24.sp
            )
            TextButton(
                onClick = {
                    CurrentDestination = AppDestinations.TaskPage
                },
                modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp)
                    .align(Alignment.CenterEnd),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape
            )
            {

                Icon(
                    imageVector = Icons.Default.Task,
                    "检测更新",
                    modifier = Modifier.size(32.dp)
                )

            }
        }
        val scrollState = rememberScrollState()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier

                .fillMaxSize()
                .verticalScroll(scrollState)
        )
        {
            var gameindex = emptyList<GameConfig>()
            try {
                gameindex =
                    ReadJson<GameListConfig>(GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameIndex.json")).GameIndex

            } catch (e: Exception) {
                XW_ToastMessage("无法获取到游戏索引,${e.message}", lc)
            }
            for (i in gameindex) {
                XW_GameInformationCard(i, {
                    CurrentDestination = AppDestinations.DownloadDetailPage
                }, false, Icons.Default.ArrowForward, false)
            }
            if (gameindex.count() != 0) {
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
                                    fontWeight = FontWeight.Bold,
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

        }

    }
}