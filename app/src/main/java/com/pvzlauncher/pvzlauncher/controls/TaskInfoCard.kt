package com.pvzlauncher.pvzlauncher.controls

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.downloader.PRDownloader
import com.pvzlauncher.pvzlauncher.utils.ProcessConfig
import com.pvzlauncher.pvzlauncher.utils.ProcessList
import com.pvzlauncher.pvzlauncher.utils.intProcessList
import com.pvzlauncher.pvzlauncher.utils.intProcessProgressList
import com.pvzlauncher.pvzlauncher.utils.intProcessSpeedList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
public fun XW_TaskInformationCard(procfg : ProcessConfig, lc : Context)
{
    return OutlinedCard(
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

                                ProcessList.removeAt(
                                    index = intProcessList.indexOf(
                                        procfg.p_id
                                    )
                                )
                                intProcessSpeedList.removeAt(intProcessList.indexOf(
                                    procfg.p_id
                                ))
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
                if(intProcessProgressList[intProcessList.indexOf(procfg.p_id)] != 100.toFloat())
                {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    )
                    {
                        Box(Modifier.fillMaxWidth())
                        {
                            Row(Modifier.align(Alignment.CenterStart)) {
                                Text("下载中……")
                                Text("${intProcessProgressList[intProcessList.indexOf(
                                    procfg.p_id
                                )]}%")
                            }
                            Row(Modifier.align(Alignment.CenterEnd))
                            {
                                Text("速度:")
                                Text("${intProcessSpeedList[intProcessList.indexOf(
                                    procfg.p_id
                                )]}MB/s")
                            }
                        }
                        LinearProgressIndicator(
                            intProcessProgressList[intProcessList.indexOf(
                                procfg.p_id
                            )] / 100, Modifier.fillMaxWidth()
                        )

                    }
                }
                else
                {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    )
                    {
                        Box(Modifier.fillMaxWidth())
                        {
                            Row(Modifier.align(Alignment.CenterStart)) {
                                Text("安装中……")
                            }
                        }
                        LinearProgressIndicator(Modifier.fillMaxWidth()
                        )

                    }
                }
            }


        }
    }
}