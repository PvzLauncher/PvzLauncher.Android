package com.pvzlauncher.pvzlauncher.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.DownloadConfig
import com.pvzlauncher.pvzlauncher.utils.DownloadCount
import com.pvzlauncher.pvzlauncher.utils.ProcessConfig
import com.pvzlauncher.pvzlauncher.utils.ProcessList
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import com.pvzlauncher.pvzlauncher.controls.XW_GameInformationCard
import com.pvzlauncher.pvzlauncher.controls.XW_InputDialog
import com.pvzlauncher.pvzlauncher.utils.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.utils.installApk
import com.pvzlauncher.pvzlauncher.utils.intProcessList
import com.pvzlauncher.pvzlauncher.utils.intProcessProgressList
import com.pvzlauncher.pvzlauncher.utils.sProcessProgressList
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import kotlin.collections.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun DownloadDetailPage()
{
    var lc = LocalContext.current
    TopAppBar(
        title = {
            Text(
                "下载游戏",
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
            .padding(0.dp, 75.dp, 0.dp, 0.dp)
            .fillMaxWidth().verticalScroll(scrollState)
    )
    {
        val scrollState = rememberScrollState()
        var lc = LocalContext.current
        var isDialogVisible by remember { mutableStateOf(false) }
        var resultText by remember { mutableStateOf(DownloadConfig.GameName) }
        XW_InputDialog(
            showDialog = isDialogVisible,
            title = "请输入游戏名",
            placeholder = "${DownloadConfig.GameName}",
            onDismiss = { isDialogVisible = false },
            value = DownloadConfig.GameName + DownloadConfig.GameLink[DownloadCount].VersionName,
            onConfirm = { text ->
                // 这里处理你拿到的输入内容
                resultText = text
                try {


                    var dlc = DownloadConfig
                    dlc.GameName = resultText
                    var pid = 0
                    var cprsc = ProcessConfig(
                        p_id = pid,
                        p_info = dlc,

                        )
                    ProcessList.add(cprsc)
                    intProcessList.add(pid)
                    intProcessProgressList.add(0.toFloat())
                    sProcessProgressList.add("0%")



                    pid = PRDownloader.download(
                        dlc.GameLink[DownloadCount].VersionLink,
                        "${lc.filesDir}",
                        "${dlc.GameName}.apk"
                    )
                        .build()
                        .setOnProgressListener { progress ->
                            intProcessProgressList[intProcessList.indexOf(pid)] =
                                ((progress.currentBytes * 100 / progress.totalBytes).toFloat())
                            sProcessProgressList[intProcessList.indexOf(pid)] =
                                ((progress.currentBytes * 100 / progress.totalBytes).toString()) + "%"
                        }
                        .start(object : OnDownloadListener {
                            override fun onDownloadComplete() {
                                XW_ToastMessage(
                                    "下载 ${
                                        ProcessList[intProcessList.indexOf(
                                            pid
                                        )].p_info.GameName
                                    } 完成", lc
                                )


                                try {
                                    installApk(
                                        lc,
                                        File("${lc.filesDir}/${dlc.GameName}.apk"),
                                        {

                                            var sl =
                                                ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}").readText())
                                            sl.GameIndex += SaveConfig(
                                                GameName = ProcessList[intProcessList.indexOf(
                                                    pid
                                                )].p_info.GameName,
                                                PackageName = "${
                                                    lc.packageManager.getPackageArchiveInfo(
                                                        "${lc.filesDir}/${dlc.GameName}.apk",
                                                        0
                                                    )?.packageName
                                                }",
                                                AddTime = ZonedDateTime.now(
                                                    ZoneId.systemDefault()
                                                ).format(
                                                    DateTimeFormatter.ofPattern(
                                                        "yyyy/MM/dd HH:mm"
                                                    )
                                                ),
                                                PlayTime = 0,
                                                LaunchTimes = 0,
                                                headImage = ProcessList[intProcessList.indexOf(
                                                    pid
                                                )].p_info.GameImage,
                                                gameversion = ProcessList[intProcessList.indexOf(
                                                    pid
                                                )].p_info.GameLink[DownloadCount].VersionVer

                                            )
                                            WriteJson<SaveConfigList>(
                                                SAVECONFIGNAME,
                                                sl,
                                                lc
                                            )

                                            intProcessProgressList.removeAt(
                                                index = intProcessList.indexOf(
                                                    pid
                                                )
                                            )
                                            sProcessProgressList.removeAt(
                                                intProcessList.indexOf(pid)
                                            )
                                            ProcessList.removeAt(
                                                index = intProcessList.indexOf(
                                                    pid
                                                )
                                            )
                                            intProcessList.remove(pid)

                                        },
                                        {
                                            intProcessProgressList.removeAt(
                                                index = intProcessList.indexOf(
                                                    pid
                                                )
                                            )
                                            sProcessProgressList.removeAt(
                                                intProcessList.indexOf(pid)
                                            )
                                            ProcessList.removeAt(
                                                index = intProcessList.indexOf(
                                                    pid
                                                )
                                            )
                                            intProcessList.remove(pid)
                                        })


                                } catch (e: Exception) {
                                    XW_ToastMessage("安装失败：${e.message}", lc)
                                    intProcessProgressList.removeAt(
                                        index = intProcessList.indexOf(
                                            pid
                                        )
                                    )
                                    sProcessProgressList.removeAt(
                                        intProcessList.indexOf(
                                            pid
                                        )
                                    )
                                    ProcessList.removeAt(
                                        index = intProcessList.indexOf(
                                            pid
                                        )
                                    )
                                    intProcessList.remove(pid)
                                }


//

                            }

                            override fun onError(error: Error?) {
                                // 下载失败
                                XW_ToastMessage(
                                    "下载出错: ${error?.serverErrorMessage}",
                                    lc
                                )
                            }


                        })





                    ProcessList[ProcessList.count() - 1].p_id = pid
                    intProcessList[ProcessList.count() - 1] = pid
                } catch (e: Exception) {
                    XW_ToastMessage("${e.message}", lc)
                }
                XW_ToastMessage("成功创建下载任务", lc)
            }
        )
        Box(modifier = Modifier.padding(5.dp))
        {
            XW_GameInformationCard(DownloadConfig, {
                isDialogVisible = true

            }, true, Icons.Default.Download, true)
        }
        Text(
            "简介",
            fontSize = 22.sp,
            fontWeight = Bold,
            modifier = Modifier.padding(10.dp, 2.dp)
        )
        Text(
            DownloadConfig.GameDescription,
            fontSize = 14.sp,
            modifier = Modifier.padding(10.dp, 5.dp)
        )
        Text(
            "游戏截图",
            fontSize = 22.sp,
            fontWeight = Bold,
            modifier = Modifier.padding(10.dp, 5.dp)
        )
        Row(
            modifier = Modifier
                .padding(5.dp)
                .horizontalScroll(scrollState)
        )
        {

            for (i in DownloadConfig.ScreenShoot) {
                AsyncImage(
                    model = i, "", modifier = Modifier
                        .height(200.dp)
                        .width(360.dp)
                )
            }
        }


    }

}