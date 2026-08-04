package com.pvzlauncher.pvzlauncher.controls

import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pvzlauncher.pvzlauncher.R

public fun XW_simpledialog(title: String,content:String,onConfirm: () -> Unit,onDismiss: () -> Unit,context : Context)
{
    val m3BaseThemeRes = com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar
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