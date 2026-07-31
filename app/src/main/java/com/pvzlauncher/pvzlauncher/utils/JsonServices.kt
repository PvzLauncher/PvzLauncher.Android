package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


@Serializable
public data class UpdateConfig(
    val LatestVersion : String,
    val LatestDescription : String,
    val LatestLink : String
)

@Serializable
public data class GameConfig(
    var GameName : String,
    val GameLink : List<VersionConfig>,
    val GameImage : String,
    val GameDescription : String,
    val ScreenShoot : List<String>
)

@Serializable
public data class VersionConfig(
    val VersionName :String,
    val VersionLink:String,
    val VersionSize:String,
    val VersionVer : String
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
    var LaunchTimes : Long,

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
    var StartUpCheckUpdate : Boolean,
    var CostumThemeColor : Boolean,
    var CostumBackground : Boolean
)




public inline fun <reified T> ReadJson(jsonString : String) : T
{
    return Json.decodeFromString<T>(jsonString)
}

public inline fun <reified T> WriteJson(fileName: String, data: T,context: Context)
{
    val writepath = context.filesDir
    val jsonString = Json.encodeToString(data)
    File("${writepath}/${fileName}").writeText(jsonString, Charsets.UTF_8)
}
