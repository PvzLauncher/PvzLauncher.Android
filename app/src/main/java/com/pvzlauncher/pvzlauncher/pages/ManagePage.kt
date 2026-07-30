package com.pvzlauncher.pvzlauncher.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.ManageIndex
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.controls.XW_ManageInformationCard
import com.pvzlauncher.pvzlauncher.ui.theme.XW_LightTheme
import java.io.File

@Composable
public fun ManagePage()
{
    var lc = LocalContext.current
    Box(Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.padding(10.dp, 35.dp, 10.dp, 5.dp)) {
            Box(modifier = Modifier.fillMaxWidth())
            {
                Text(
                    "管理",
                    fontWeight = Bold,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.CenterStart),
                    fontSize = 24.sp
                )


            }
            val scrollState = rememberScrollState()
            Column(
                Modifier

                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                if(ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex.count() != 0)
                {
                    for (i in ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex) {
                        XW_ManageInformationCard(
                            args = i,
                            onBack = {
                                ManageIndex =
                                    ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex.indexOf(
                                        i
                                    )
                                CurrentDestination = AppDestinations.ManageDetailPage
                            },
                            IsButtonEnable = true,

                            )

                    }
                }
                else
                {
                    Box(Modifier.fillMaxSize())
                    {
                        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("暂无任何游戏版本",fontSize = 18.sp, fontWeight = Bold)
                            Text("请导入或下载！", fontSize = 14.sp)
                        }
                    }
                }
            }

        }
        FloatingActionButton(
            onClick = {
                CurrentDestination = AppDestinations.ImportPage
            },
            modifier = Modifier
                .padding(30.dp)
                .size(48.dp)
                .align(Alignment.BottomEnd),
            shape = RoundedCornerShape(10.dp), containerColor = XW_LightTheme.primary, contentColor = Color.White
        )
        {

            Icon(
                imageVector = Icons.Default.Add,
                "检测更新",
                modifier = Modifier.size(32.dp)
            )

        }
    }
}