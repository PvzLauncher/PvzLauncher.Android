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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.ManageIndex
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.XW_ManageInformationCard
import java.io.File

@Composable
public fun ManagePage()
{
    var lc = LocalContext.current
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
            var isDialogVisible by remember { mutableStateOf(false) }
            var resultText by remember { mutableStateOf("") }

            TextButton(
                onClick = {
                    CurrentDestination = AppDestinations.ImportPage
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
                    imageVector = Icons.Default.Add,
                    "检测更新",
                    modifier = Modifier.size(32.dp)
                )

            }

        }
        val scrollState = rememberScrollState()
        Column(
            Modifier

                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
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

    }
}