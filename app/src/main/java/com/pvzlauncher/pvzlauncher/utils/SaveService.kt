package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import java.io.File

fun ImportSaves(packageName : String,lc : Context)
{
    val f = File(
        when (packageName) {
            "com.popcap.game.pvz_row" ->
                "Android/data/$packageName/userdata"

            "com.ea.game.pvz2_row" ->
                "Android/data/$packageName/files/No_Backup"

            "com.ea.gp.pvz3" ->
                "Android/data/$packageName"

            else ->
                ""
        }
    )
}

fun ExportSaves(packageName : String,lc : Context)
{
    val f = File(
        when (packageName) {
            "com.popcap.game.pvz_row" ->
                "Android/data/$packageName/userdata"

            "com.ea.game.pvz2_row" ->
                "Android/data/$packageName/files/No_Backup"

            "com.ea.gp.pvz3" ->
                "Android/data/$packageName"

            else ->
                ""
        }
    )
}