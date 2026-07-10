package com.pvzlauncher.pvzlauncher.utils

import android.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader

import kotlinx.coroutines.runBlocking
import java.io.File
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

public fun CheckUpdate(lc : Context,isSilent : Boolean)
{
    try {
        val jsondata = GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/UpdateIndex.json")
        val ConfigInline = ReadJson<UpdateConfig>(jsondata)
        if(ConfigInline.LatestVersion == APP_VERSION)
        {
            if(!isSilent) {
                XW_ToastMessage("当前版本已经是最新版本", lc)
            }
        }
        else
        {
            XW_simpledialog("更新可用","当前版本:${APP_VERSION}\r\n最新版本:${ConfigInline.LatestVersion}\r\n更新日志:\r\n${ConfigInline.LatestDescription}\r\n请问是否现在就要更新？",{
                var loa = XW_LoadingMask(lc)
                var dprogress = 0
                var ltscfg = ReadJson<UpdateConfig>(GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/UpdateIndex.json"))
                PRDownloader.download(
                    ltscfg.LatestLink,
                    lc.cacheDir.absolutePath,
                    "${ltscfg.LatestVersion}.apk"
                )
                    .build()
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete()
                        {
                            installApk(lc,File("${lc.cacheDir.absolutePath}/${ltscfg.LatestVersion}.apk"))
                            System.exit(0)
                        }

                        override fun onError(error: Error?) {
                            // 下载失败
                            XW_ToastMessage("下载出错: ${error?.serverErrorMessage}",lc)
                            loa.dismiss()
                        }


                    })
            },{},lc)
        }
    }
    catch(e: Exception)
    {
        XW_ToastMessage("检测更新时遇到错误：${e.message}",lc)
    }
}


