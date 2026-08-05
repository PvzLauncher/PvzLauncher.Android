package com.pvzlauncher.pvzlauncher.pages

import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.DirectoryPicker
import com.pvzlauncher.pvzlauncher.utils.ManageIndex
import com.pvzlauncher.pvzlauncher.utils.ManagelistIndex
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SavePage()
{
    val lc = LocalContext.current
    val all = ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"))
    var path by remember { mutableStateOf("") }
    TopAppBar(
        title = {
            Text(
                "导入/导出存档",
                fontWeight = Bold,
                modifier = Modifier.padding(5.dp),
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            TextButton(
                onClick = {
                    CurrentDestination = AppDestinations.ManageDetailPage
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
    Box(Modifier.fillMaxSize().padding(0.dp, 75.dp, 0.dp, 0.dp)
        .fillMaxSize().verticalScroll(scrollState))
    {
        Column(Modifier.fillMaxSize())
        {
            Text("请选择对应版本的存档目录")
            if(false)
            {
                if(!File(Environment.getExternalStorageDirectory(),"Android/data/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}/").exists())
                {
                    Box(Modifier.fillMaxSize())
                    {
                        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("此游戏无法管理存档",fontSize = 18.sp, fontWeight = Bold)
                            Log.d("Error","Android/data/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName},${File(Environment.getExternalStorageDirectory(),"Android/data/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}/").absolutePath},${File(Environment.getExternalStorageDirectory(),"Android/data/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}/").exists()}")
                        }
                    }
                    return

                }

                if(!File(Environment.getExternalStorageDirectory(),"Android/data/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}/files/").exists())
                {
                    Box(Modifier.fillMaxSize())
                    {
                        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("此游戏无法管理存档",fontSize = 18.sp, fontWeight = Bold)
                            Log.d("Error","Android/data/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName},${File(Environment.getExternalStorageDirectory(),"Android/data/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}/").absolutePath},${File(Environment.getExternalStorageDirectory(),"Android/data/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}/").exists()}")
                        }
                    }
                    return
                }
            }
            val dir = File(
                "/storage/emulated/0/Android/data/"
            )

            Log.d(
                "TEST",
                """
    path=${dir.absolutePath}
    exists=${dir.exists()}
    isDirectory=${dir.isDirectory}
    canRead=${dir.canRead()}
    files=${dir.listFiles()?.size}
    """.trimIndent()
            )

            DirectoryPicker(File(Environment.getExternalStorageDirectory(),"Android/data/${all.ListIndex[ManagelistIndex].GameIndex[ManageIndex].PackageName}/files").absolutePath) {
                p -> path = p
            }

        }

    }
}
