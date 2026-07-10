package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

import java.io.File


@Serializable
public data class UpdateConfig(
    val LatestVersion : String,
    val LatestDescription : String,
    val LatestLink : String
)

@Serializable
public data class GameConfig(
    val PackageName: String,
    var GameName : String,
    val GameLink : String,
    val GameImage : String,
    val GameSize : String,
    val GameVersion : String,
    val GameDescription : String,
    val ScreenShoot : List<String>
)

@Serializable
public data class GameListConfig(
    val GameIndex : List<GameConfig>
)

@Serializable
public data class SaveConfig(
    val headImage: String,
    val gameversion : String,
    var GameName : String,
    val PackageName : String,
    val AddTime : String,
    var PlayTime : Long,
    var LaunchTimes : Long

)

@Serializable
public data class SaveConfigList(
    var GameIndex : List<SaveConfig>
)

@Serializable
public data class LauncherConfig(
    var UseSystemTheme: Boolean,
    var UseDarkTheme : Boolean,
    var UseEnglishTitle : Boolean,
    var CurrentGameIndex : Int,
    var StartUpCheckUpdate : Boolean
)




public inline fun <reified T> ReadJson(jsonString : String) : T
{
    return Json.decodeFromString<T>(jsonString)
}

public inline fun <reified T> WriteJson(fileName: String, data: T,context: Context)
{
    val writepath = context.filesDir
    val jsonString = Json.encodeToString(data)
    Log.d("FilePathTest", "${jsonString}")
    File("${writepath}/${fileName}").writeText(jsonString, Charsets.UTF_8)
    Log.d("FilePathTest", "文件绝对路径是: ${File("${writepath}/${fileName}").absolutePath}")
}

@Composable
public fun InitializeLauncherSettings()
{
    WriteJson<LauncherConfig>(LAUNCHERCONFIGNAME, LauncherConfig(
        UseSystemTheme = true,
        UseDarkTheme = false,
        UseEnglishTitle = false,
        CurrentGameIndex = 0,
        true
    ), LocalContext.current
    )
}
@Composable
public fun InitializeSaveLists()
{
    WriteJson<SaveConfigList>(SAVECONFIGNAME, SaveConfigList(emptyList<SaveConfig>()),LocalContext.current)
}