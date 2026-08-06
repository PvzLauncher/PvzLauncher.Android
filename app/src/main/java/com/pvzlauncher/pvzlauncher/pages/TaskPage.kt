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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.controls.XW_TaskInformationCard
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.ProcessList
import com.pvzlauncher.pvzlauncher.utils.totalprogress
import com.pvzlauncher.pvzlauncher.utils.totalspeed

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
    Column(modifier = Modifier.fillMaxWidth().padding(10.dp, 100.dp, 10.dp, 10.dp))
    {
        Column(modifier = Modifier.fillMaxWidth())
        {
            Box(modifier = Modifier.fillMaxWidth())
            {
                Row(modifier = Modifier.align(Alignment.CenterStart))
                {
                    Text("总进度:")
                    Text("${totalprogress.value}%")
                }
                Row(modifier = Modifier.align(Alignment.CenterEnd))
                {
                    Text("总速度:")
                    Text("${totalspeed.value}MB/s")
                }
            }
            LinearProgressIndicator((totalprogress.value)/100,Modifier.fillMaxWidth())
        }
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth().padding(0.dp,5.dp,0.dp,0.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            ProcessList.forEach { procfg ->
                XW_TaskInformationCard(procfg,lc)
            }
            if(ProcessList.count() == 0)
            {
                Box(Modifier.fillMaxSize())
                {
                    Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("暂无任务",fontSize = 18.sp, fontWeight = Bold)
                        Text("可以从下载页选择游戏版本下载", fontSize = 14.sp)
                    }
                }
                totalprogress.value = 100.toFloat()
                totalspeed.value = 0.toFloat()
            }


        }
    }

}