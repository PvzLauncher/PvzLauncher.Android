package com.pvzlauncher.pvzlauncher.utils

import androidx.compose.runtime.mutableStateListOf
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations

lateinit var APP_VERSION: String
var CurrentDestination by mutableStateOf<AppDestinations>(AppDestinations.HomePage)
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
lateinit var Downloadlist : MutableState<@Composable () -> Unit>



data class ProcessConfig(
    var p_info : GameConfig,
    var p_id : Int

)

//MDReaderPageArgs
var MDR_FileName : String = ""
var MDR_MDContent : String = ""

//SettingsPageArgs
var LauncherConfigPath : String? = null

