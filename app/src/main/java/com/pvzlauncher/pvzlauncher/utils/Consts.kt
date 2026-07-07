package com.pvzlauncher.pvzlauncher.utils

import androidx.compose.runtime.mutableStateListOf
import com.downloader.request.DownloadRequest
import okhttp3.internal.concurrent.Task

const val APP_VERSION = "1.0.0-alpha.3"
const val LAUNCHERCONFIGNAME = "LauncherConfig.json"

//ManageDetailPageArgs
var ManageConfigPath : String? = null
var ManageIndex : Int = 0

//DownloadDetailPageArgs
var DownloadConfig : GameConfig = GameConfig("","","","","","",listOf(""))

//TaskPageArgs


val ProcessList =  mutableStateListOf<ProcessConfig>()
val intProcessList = mutableStateListOf<Int>()

val intProcessProgressList = mutableStateListOf<Float>()
val sProcessProgressList = mutableStateListOf<String>()

data class ProcessConfig(
    var p_info : GameConfig,
    var p_id : Int

)

//MDReaderPageArgs
var MDR_FileName : String = ""
var MDR_MDContent : String = ""

//SettingsPageArgs
var LauncherConfigPath : String? = null