package com.pvzlauncher.pvzlauncher.controls

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import coil3.compose.AsyncImage
import androidx.compose.foundation.clickable

fun XW_PhotoMask(context: Context, PhotoResources: Any?): Dialog {
    val dialog = Dialog(context).apply {
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    val composeView = ComposeView(context).apply {
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.4f)).clickable(true, onClick = { dialog.hide() }),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(PhotoResources,"",Modifier.fillMaxWidth())
            }
        }
    }

    if (context is ComponentActivity) {
        composeView.setViewTreeLifecycleOwner(context)
        composeView.setViewTreeViewModelStoreOwner(context)
        composeView.setViewTreeSavedStateRegistryOwner(context)
    }

    dialog.setContentView(composeView)

    dialog.window?.apply {
        setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    return dialog
}