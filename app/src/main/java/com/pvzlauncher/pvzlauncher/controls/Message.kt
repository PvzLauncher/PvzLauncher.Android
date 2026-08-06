package com.pvzlauncher.pvzlauncher.controls

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.utils.snackbarHostState
import com.pvzlauncher.pvzlauncher.utils.snackbarScope
import kotlinx.coroutines.launch

public fun XW_ToastMessage(message:String,context: Context? = null)
{
    snackbarScope.launch {
        snackbarHostState.showSnackbar(message)
    }
}

fun XW_NotifyMessage(
    context: Context,
    title: String,
    text: String
) {

    val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager


    manager.notify(
        System.currentTimeMillis().toInt(),

        NotificationCompat.Builder(
            context,
            "default_channel"
        )
            .setSmallIcon(R.drawable.ic_appicon_vector)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    )
}












