package com.pvzlauncher.pvzlauncher.utils

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
    val GameName : String,
    val GameLink : String,
    val GameImage : String
)

@Serializable
public data class GameListConfig(
    val GameIndex : List<GameConfig>
)

public inline fun <reified T> ReadJson(jsonString : String) : T
{
    return Json.decodeFromString<T>(jsonString)
}

public inline fun <reified T> WriteJson(filePath: String, data: T)
{
    val jsonString = Json.encodeToString(data)
    File(filePath).writeText(jsonString, Charsets.UTF_8)
}