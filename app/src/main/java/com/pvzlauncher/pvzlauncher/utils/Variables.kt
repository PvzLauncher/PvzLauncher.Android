package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.content.pm.PackageInfo
import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.pvzlauncher.pvzlauncher.AppDestinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

lateinit var APP_VERSION: String
var CurrentDestination by mutableStateOf<AppDestinations>(AppDestinations.HomePage)
const val LAUNCHERCONFIGNAME = "LauncherConfig.json"
const val SAVECONFIGNAME = "SaveConfig.json"
lateinit var globalContext: Context
var ManagelistIndex : Int = 0
var ManageIndex : Int = 0
lateinit var DownloadConfig : GameConfig
var DownloadCount : Int = 0
val ProcessList =  mutableStateListOf<ProcessConfig>()
val intProcessList = mutableStateListOf<Int>()
val intProcessProgressList = mutableStateListOf<Float>()
val intProcessSpeedList = mutableStateListOf<Float>()
var totalspeed = mutableStateOf(0.toFloat())
var totalprogress = mutableStateOf(100.toFloat())
lateinit var Downloadlist : MutableState<@Composable () -> Unit>
var Installedappindex = mutableStateListOf<PackageInfo>()
var MDR_FileName : String = ""
var MDR_MDContent : String = ""
val snackbarHostState = SnackbarHostState()
val snackbarScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
var UseDarkTheme = mutableStateOf(true)
var checkedupdate = mutableStateOf(false)
var CanEditSaves = false
var checkinternetconnect = mutableStateOf(false)
lateinit var currentactivity : ComponentActivity
data class ProcessConfig(
    var p_info : GameConfig,
    var p_id : Int

)

