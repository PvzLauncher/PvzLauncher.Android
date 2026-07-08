package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

public fun GetApkInfo(pkg : String,context: Context) : PackageInfo
{
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        val k = context.packageManager.getPackageInfo(pkg, PackageManager.PackageInfoFlags.of(0))
        return k
    } else {
        @Suppress("DEPRECATION")
        val k = context.packageManager.getPackageInfo(pkg, 0)
        return k
    }

}

fun isAppInstalled(context: Context, packageName: String): Boolean {
    val packageManager = context.packageManager
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun installApk(context: Context, apkFile: File) {
    if (!apkFile.exists()) {
        Toast.makeText(context, "安装文件不存在", Toast.LENGTH_SHORT).show()
        return
    }

    val intent = Intent(Intent.ACTION_VIEW).apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // API 24+ 必须使用 FileProvider
        val apkUri: Uri = FileProvider.getUriForFile(
            context,
            "com.pvzlauncher.pvzlauncher.fileprovider", // 需与 AndroidManifest 中的 authorities 一致
            apkFile
        )
        setDataAndType(apkUri, "application/vnd.android.package-archive")
    }
    context.startActivity(intent)
}

fun uninstallApk(context: Context, packageName: String) {
    try {
        val intent = Intent(Intent.ACTION_DELETE).apply {
            data = Uri.parse("package:$packageName")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "卸载失败: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun launchApp(context: Context, packageName: String) {
    // 通过包名获取该应用的启动 Intent（通常是配置了 Launcher 的 Activity）
    val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) {
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
    } else {
        // 如果返回 null，说明该包名未安装，或者该应用没有配置启动界面（如纯后台服务应用）
        Toast.makeText(context, "未找到该应用或无法启动", Toast.LENGTH_SHORT).show()
    }
}