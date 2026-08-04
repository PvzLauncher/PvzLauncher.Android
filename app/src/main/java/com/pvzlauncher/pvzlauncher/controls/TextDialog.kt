package com.pvzlauncher.pvzlauncher.controls

import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.utils.CurrentIndex
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

public fun XW_simpledialog(title: String,content:String,onConfirm: () -> Unit,onDismiss: () -> Unit,context : Context,scope: CoroutineScope)
{
    var m3BaseThemeRes = com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar
    var a = LauncherConfig(
        UseSystemTheme = true,
        UseDarkTheme = false,
        UseEnglishTitle = false,
        CurrentGameIndex = CurrentIndex(0,0),
        true, false,false
    )
    scope.launch {
        a = ReadJson<LauncherConfig>(File("${context.filesDir}/${LAUNCHERCONFIGNAME}"))
    }
    if(!a.UseSystemTheme)
    {
        if(a.UseDarkTheme)
        {
            m3BaseThemeRes = com.google.android.material.R.style.Theme_Material3_Dark_Dialog
        }
        else
        {
            m3BaseThemeRes = com.google.android.material.R.style.Theme_Material3_Light_Dialog
        }
    }
    val baseContext = ContextThemeWrapper(context, m3BaseThemeRes)
    val m3Context = ContextThemeWrapper(baseContext, R.style.XW_DialogTheme)

    MaterialAlertDialogBuilder(m3Context)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton("确定") { dialog, which ->
            onConfirm()
        }
        .setNegativeButton("取消") { dialog, which ->
            onDismiss()
        }
        .setCancelable(false)
        .show()

}