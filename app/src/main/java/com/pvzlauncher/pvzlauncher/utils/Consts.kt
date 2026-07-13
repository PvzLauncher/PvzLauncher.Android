package com.pvzlauncher.pvzlauncher.utils

import androidx.compose.runtime.mutableStateListOf
import com.downloader.request.DownloadRequest
import okhttp3.internal.concurrent.Task
import android.content.Context

lateinit var APP_VERSION: String
const val LAUNCHERCONFIGNAME = "LauncherConfig.json"
const val SAVECONFIGNAME = "SaveConfig.json"
lateinit var globalContext: Context




//ManageDetailPageArgs
var ManageConfigPath : String? = null
var ManageIndex : Int = 0



//DownloadDetailPageArgs
lateinit var DownloadConfig : GameConfig
var DownloadCount : Int = 0
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