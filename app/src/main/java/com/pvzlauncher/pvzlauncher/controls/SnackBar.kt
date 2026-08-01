package com.pvzlauncher.pvzlauncher.controls

import android.content.Context
import com.pvzlauncher.pvzlauncher.utils.snackbarHostState
import com.pvzlauncher.pvzlauncher.utils.snackbarScope
import kotlinx.coroutines.launch

public fun XW_ToastMessage(message:String,context: Context? = null)
{
    snackbarScope.launch {
        snackbarHostState.showSnackbar(message)
    }
}












