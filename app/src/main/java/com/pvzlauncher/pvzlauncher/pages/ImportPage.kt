package com.pvzlauncher.pvzlauncher.pages

import android.content.pm.ApplicationInfo
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.GetApkInfo
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.utils.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.utils.isAppInstalled
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import kotlin.collections.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ImportPage()
{
    val lc = LocalContext.current
    TopAppBar(
        title = {
            Text(
                "导入",
                fontWeight = Bold,
                modifier = Modifier.padding(5.dp),
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            TextButton(
                onClick = {
                    CurrentDestination = AppDestinations.ManagePage
                }, modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp), contentPadding = PaddingValues(0.dp),
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
        Modifier
            .padding(10.dp, 90.dp, 10.dp, 10.dp)
            .fillMaxSize().verticalScroll(scrollState)
    )
    {
        var gameindex =
            emptyList<android.content.pm.PackageInfo>().toMutableStateList()
        try {
            gameindex =
                lc.packageManager.getInstalledPackages(0).toMutableStateList()


        } catch (e: Exception) {
            XW_ToastMessage("无法获取到游戏索引,${e.message}", lc)
        }


        gameindex.forEach { i ->

            if ((lc.packageManager.getApplicationInfo(
                    i.packageName,
                    0
                ).flags and ApplicationInfo.FLAG_SYSTEM) == 0
            ) {
                var tempname = lc.packageManager.getApplicationLabel(
                    lc.packageManager.getApplicationInfo(
                        i.packageName,
                        0
                    )
                ).toString()
                if (isAppInstalled(
                        lc,
                        i.packageName
                    ) && ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex.none { it.PackageName == i.packageName }
                ) {
                    OutlinedCard(
                        Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                    )
                    {
                        Box(
                            Modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                        )
                        {
                            Column(
                                Modifier.align(Alignment.CenterStart)
                                    .padding(10.dp, 10.dp, 64.dp, 10.dp)
                            )
                            {
                                Row(verticalAlignment = Alignment.CenterVertically)
                                {
                                    AsyncImage(
                                        model = lc.packageManager.getApplicationIcon(
                                            i.packageName
                                        ),
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
                                                lc
                                            )
                                        })


                                    Column(Modifier.padding(2.dp))
                                    {
                                        Text(
                                            lc.packageManager.getApplicationLabel(
                                                lc.packageManager.getApplicationInfo(
                                                    i.packageName,
                                                    0
                                                )
                                            ).toString(), fontWeight = Bold
                                        )

                                    }
                                }
                                OutlinedTextField(
                                    label = { Text("请输入导入后的游戏名") },
                                    onValueChange = { t -> tempname = t },
                                    value = lc.packageManager.getApplicationLabel(
                                        lc.packageManager.getApplicationInfo(
                                            i.packageName,
                                            0
                                        )
                                    ).toString()
                                )
                            }

                            TextButton(
                                onClick = {
                                    var aaa =
                                        ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText())
                                    aaa.GameIndex += SaveConfig(
                                        GameName = tempname,
                                        PackageName = i.packageName,
                                        AddTime = ZonedDateTime.now(ZoneId.systemDefault())
                                            .format(
                                                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
                                            ),
                                        PlayTime = 0,
                                        LaunchTimes = 0,
                                        headImage = "https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameAssets/Default.png",
                                        gameversion = GetApkInfo(
                                            i.packageName,
                                            lc
                                        ).versionName ?: "1.0.0"
                                    )
                                    WriteJson<SaveConfigList>(
                                        SAVECONFIGNAME,
                                        aaa,
                                        lc
                                    )
                                    gameindex.remove(i)
                                    XW_ToastMessage("导入成功", lc)


                                },
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.CenterEnd)
                                    .size(48.dp),
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
}