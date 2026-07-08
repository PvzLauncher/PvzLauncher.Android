package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

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