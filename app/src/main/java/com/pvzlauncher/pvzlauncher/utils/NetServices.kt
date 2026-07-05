package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import kotlinx.coroutines.runBlocking
import java.net.URL


public fun OpenUrl(url : String,context : Context)
{
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    context.startActivity(intent)
}

public fun GetWebSiteContent(url : String): String {
    return URL(url).readText(Charsets.UTF_8)
}


