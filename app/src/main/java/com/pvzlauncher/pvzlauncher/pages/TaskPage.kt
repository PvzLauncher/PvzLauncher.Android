package com.pvzlauncher.pvzlauncher.pages

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.downloader.PRDownloader
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.controls.XW_InputDialog
import com.pvzlauncher.pvzlauncher.controls.XW_simpledialog
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.ProcessList
import com.pvzlauncher.pvzlauncher.utils.intProcessList
import com.pvzlauncher.pvzlauncher.utils.intProcessProgressList
import com.pvzlauncher.pvzlauncher.utils.sProcessProgressList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TaskPage()
{
    val lc = LocalContext.current
    TopAppBar(
        title = {
            Text(
                "任务",
                fontWeight = Bold,
                modifier = Modifier.padding(5.dp),
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            TextButton(
                onClick = {
                    CurrentDestination = AppDestinations.DownloadPage
                },
                modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp)
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
        }
    )
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 75.dp, 10.dp, 10.dp).verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        ProcessList.forEach { procfg ->
            OutlinedCard(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            )
            {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                )
                {
                    val scope = rememberCoroutineScope()
                    TextButton(
                        onClick = {
                            XW_simpledialog("提示","是否删除任务:${procfg.p_info.GameName}？",{
                                PRDownloader.cancel(procfg.p_id)
                                scope.launch {
                                    delay(100)
                                    intProcessProgressList.removeAt(
                                        index = intProcessList.indexOf(
                                            procfg.p_id
                                        )
                                    )
                                    sProcessProgressList.removeAt(
                                        intProcessList.indexOf(
                                            procfg.p_id
                                        )
                                    )
                                    ProcessList.removeAt(
                                        index = intProcessList.indexOf(
                                            procfg.p_id
                                        )
                                    )
                                    intProcessList.remove(procfg.p_id)
                                }
                            },{},lc)




                        }, modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.CenterEnd)
                            .size(48.dp), contentPadding = PaddingValues(0.dp),
                        shape = CircleShape
                    )

                    {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            "检测更新",
                            modifier = Modifier.size(32.dp)
                        )

                    }


                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                    )
                    {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                        )
                        {
                            AsyncImage(
                                model = procfg.p_info.GameImage,
                                "",
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(32.dp)
                            )
                            Column(Modifier.padding(2.dp))
                            {
                                Text(
                                    "下载 ${procfg.p_info.GameName}",
                                    fontSize = 22.sp,
                                    modifier = Modifier.padding(2.dp),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "pid:${procfg.p_id}",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                        )
                        {
                            Row(Modifier.fillMaxWidth()) {
                                Text("下载中……")
                                Text(
                                    sProcessProgressList[intProcessList.indexOf(
                                        procfg.p_id
                                    )]
                                )
                            }
                            LinearProgressIndicator(
                                intProcessProgressList[intProcessList.indexOf(
                                    procfg.p_id
                                )] / 100, Modifier.fillMaxWidth()
                            )

                        }
                    }


                }
            }

        }


    }
}