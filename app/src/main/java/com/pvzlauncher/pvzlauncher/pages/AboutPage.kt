package com.pvzlauncher.pvzlauncher.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.utils.APP_VERSION
import com.pvzlauncher.pvzlauncher.utils.CheckUpdate
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.GetWebSiteContent
import com.pvzlauncher.pvzlauncher.utils.MDR_FileName
import com.pvzlauncher.pvzlauncher.utils.MDR_MDContent
import com.pvzlauncher.pvzlauncher.utils.OpenUrl
import com.pvzlauncher.pvzlauncher.controls.XW_ToastMessage

@Composable
public fun AboutPage()
{
    val lc = LocalContext.current
    Column(modifier = Modifier.padding(10.dp, 35.dp)) {
        Box(modifier = Modifier.fillMaxWidth())
        {
            Text(
                "关于",
                fontWeight = Bold,
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.CenterStart),
                fontSize = 24.sp
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
                            fontSize = 22.sp,
                            fontWeight = Bold
                        )

                    }

                    Row(verticalAlignment = Alignment.CenterVertically)
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
                        TextButton(
                            onClick = {
                                CheckUpdate(lc, false)
                            }, modifier = Modifier


                        )
                        {


                            Text(
                                "检测更新",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )


                        }


                    }

                    Row()
                    {
                        val cont = LocalContext.current
                        OutlinedButton(onClick = {
                            OpenUrl(
                                "https://github.com/PvzLauncher/PvzLauncher.Android/issues/new?template=bug.yml",
                                cont
                            )
                        })
                        {

                            Text("反馈漏洞")
                        }

                        OutlinedButton(onClick = {
                            //OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android/blob/main/Assets/EULA.md",cont)
                            try {
                                MDR_FileName = "许可协议"
                                MDR_MDContent =
                                    GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/Files/EULA.md")
                                CurrentDestination =
                                    AppDestinations.MDReaderPage
                            } catch (e: Exception) {
                                XW_ToastMessage(
                                    "无法读取EULA信息,${e.message}",
                                    lc
                                )
                            }

                        })
                        {

                            Text("许可协议")
                        }

                        OutlinedButton(onClick = {
                            //OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android/blob/main/Assets/QandA.md",cont)
                            try {
                                MDR_FileName = "常见问题"
                                MDR_MDContent =
                                    GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/Files/QandA.md")
                                CurrentDestination =
                                    AppDestinations.MDReaderPage
                            } catch (e: Exception) {
                                XW_ToastMessage(
                                    "无法读取常见问题信息,${e.message}",
                                    lc
                                )
                            }
                        })
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
                Text(
                    "源代码仓库",
                    fontWeight = Bold,
                    modifier = Modifier.padding(2.dp)
                )
                TextButton(
                    {
                        OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android",lc)
                    },
                    modifier = Modifier.padding(2.dp)
                )
                {
                    Text("访问 PvzLauncher.Android 源代码仓库")
                }
                Text(
                    "开发者",
                    fontWeight = Bold,
                    modifier = Modifier.padding(2.dp)
                )
                TextButton(
                    {
                        OpenUrl("https://github.com/Xiaowang0229",lc)
                    },
                    modifier = Modifier.padding(2.dp)
                )
                {
                    Text("Xiaowang0229 - 主要开发人员")
                }
                Text(
                    "原作者",
                    fontWeight = Bold,
                    modifier = Modifier.padding(2.dp)
                )
                TextButton(
                    {
                        OpenUrl("https://github.com/ishuamouren",lc)
                    },
                    modifier = Modifier.padding(2.dp)
                )
                {
                    Text("ishuamouren - 启动器原作者")
                }
                Text(
                    "贡献者",
                    fontWeight = Bold,
                    modifier = Modifier.padding(2.dp)
                )
                TextButton(
                    {
                        OpenUrl("https://github.com/PvzLauncher/PvzLauncher.Android",lc)
                    },
                    modifier = Modifier.padding(2.dp)
                )
                {
                    Text("衷心感谢支持PvzLauncher的每一名用户！")
                }
            }
        }
    }
}