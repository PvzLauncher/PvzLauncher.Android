package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.pvzlauncher.pvzlauncher.controls.XW_LoadingMask
import com.pvzlauncher.pvzlauncher.controls.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.controls.XW_UpdateDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

public suspend fun GetWebSiteContent(url : String): String = withContext(Dispatchers.IO) {

        URL(url).readText(Charsets.UTF_8)

}

public  fun GetWebSiteContentLegacy(url : String): String {

    return URL(url).readText(Charsets.UTF_8)

}

public suspend fun CheckUpdate(lc : Context,isSilent : Boolean)
{
    try {

        var ltscfg = ReadJsonfromText<UpdateConfig>(GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/UpdateIndex.json"))

        if(ltscfg.LatestVersion == APP_VERSION)
        {
            if(!isSilent) {
                XW_ToastMessage("当前版本已经是最新版本", lc)
            }
        }
        else
        {

            val info = GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/UpdateInfo.md")
            ltscfg.LatestDescription = info
            XW_UpdateDialog(lc,ltscfg,{},{
                var loa = XW_LoadingMask(lc,"请稍候……")
                loa.show()
                PRDownloader.download(
                    ltscfg.LatestLink,
                    "${lc.filesDir}/temp",
                    "${ltscfg.LatestVersion}.apk"
                )
                    .build()
                    .start(object : OnDownloadListener {
                        override fun onDownloadComplete()
                        {
                            installApklegacy(lc,File("${lc.filesDir}/temp/${ltscfg.LatestVersion}.apk"))
                            System.exit(0)
                        }

                        override fun onError(error: Error?) {
                            // 下载失败
                            XW_ToastMessage("下载出错: ${error?.serverErrorMessage}", lc)
                            loa.hide()
                        }


                    })
            })
        }
    }
    catch(e: Exception)
    {
        XW_ToastMessage("检测更新时遇到错误：${e.message}", lc)
    }
}


