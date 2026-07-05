package com.pvzlauncher.pvzlauncher.utils

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
val TaskList = mutableListOf<Int>()

val TaskInformationList = mutableListOf<GameConfig>()



//MDReaderPageArgs
var MDR_FileName : String = ""
var MDR_MDContent : String = ""

//SettingsPageArgs
var LauncherConfigPath : String? = null