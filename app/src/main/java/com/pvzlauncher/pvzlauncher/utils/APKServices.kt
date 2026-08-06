package com.pvzlauncher.pvzlauncher.utils

import android.app.AppOpsManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import kotlinx.coroutines.launch
import java.io.File
import kotlin.concurrent.thread
import android.annotation.SuppressLint


import android.provider.Settings

import java.io.FileInputStream
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

private const val EXTRA_INSTALL_TOKEN = "extra_install_token"

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

fun installApklegacy(context: Context, apkFile: File) {
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


@SuppressLint("ExportedReceiver", "UnspecifiedRegisterReceiverFlag", "MutablePendingIntent")
fun installApk(
    context: Context,
    apkFile: File,
    onSuccess: () -> Unit,
    onFailed: () -> Unit
) {
    val appContext = context.applicationContext
    val mainHandler = Handler(Looper.getMainLooper())

    fun post(block: () -> Unit) {
        mainHandler.post { block() }
    }

    if (!apkFile.exists() || !apkFile.canRead()) {
        post(onFailed)
        return
    }

    /*
     * Android 8.0+ 安装未知来源应用需要“允许安装未知应用”权限。
     * 如果你已经在外部处理过权限申请，可以删除这一段。
     */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
        !appContext.packageManager.canRequestPackageInstalls()
    ) {
        try {
            val settingsIntent = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${appContext.packageName}")
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(settingsIntent)
        } catch (_: Exception) {
        }
        post(onFailed)
        return
    }

    val installer = appContext.packageManager.packageInstaller
    val sessionParams = PackageInstaller.SessionParams(
        PackageInstaller.SessionParams.MODE_FULL_INSTALL
    ).apply {
        setSize(apkFile.length())
    }

    val sessionId = try {
        installer.createSession(sessionParams)
    } catch (e: Exception) {
        post(onFailed)
        return
    }

    val action = "${appContext.packageName}.INSTALL_STATUS_$sessionId"
    val token = UUID.randomUUID().toString()

    val callbackDelivered = AtomicBoolean(false)
    var receiverRef: BroadcastReceiver? = null

    fun finishWithResult(success: Boolean) {
        if (!callbackDelivered.compareAndSet(false, true)) return

        receiverRef?.let {
            try {
                appContext.unregisterReceiver(it)
            } catch (_: Exception) {
            }
        }

        post(if (success) onSuccess else onFailed)
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            if (intent.getIntExtra(PackageInstaller.EXTRA_SESSION_ID, -1) != sessionId) return
            if (intent.getStringExtra(EXTRA_INSTALL_TOKEN) != token) return

            val status = intent.getIntExtra(
                PackageInstaller.EXTRA_STATUS,
                PackageInstaller.STATUS_FAILURE
            )

            when (status) {
                PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                    // 普通应用安装时，系统需要用户确认，这里拉起系统安装确认页
                    val confirmIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(Intent.EXTRA_INTENT)
                    }

                    if (confirmIntent == null) {
                        finishWithResult(false)
                        return
                    }

                    try {
                        confirmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        ctx.startActivity(confirmIntent)
                    } catch (e: Exception) {
                        finishWithResult(false)
                    }
                }

                PackageInstaller.STATUS_SUCCESS -> {
                    finishWithResult(true)
                }

                else -> {
                    finishWithResult(false)
                }
            }
        }
    }
    receiverRef = receiver

    try {
        val filter = IntentFilter(action)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appContext.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            appContext.registerReceiver(receiver, filter)
        }
    } catch (e: Exception) {
        try {
            installer.abandonSession(sessionId)
        } catch (_: Exception) {
        }
        post(onFailed)
        return
    }

    val session = try {
        installer.openSession(sessionId)
    } catch (e: Exception) {
        finishWithResult(false)
        try {
            installer.abandonSession(sessionId)
        } catch (_: Exception) {
        }
        return
    }

    try {
        FileInputStream(apkFile).use { input ->
            session.openWrite("base.apk", 0, apkFile.length()).use { output ->
                input.copyTo(output)
                session.fsync(output)
            }
        }

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val statusIntent = Intent(action)
            .setPackage(appContext.packageName)
            .putExtra(EXTRA_INSTALL_TOKEN, token)

        val statusReceiver = PendingIntent.getBroadcast(
            appContext,
            sessionId,
            statusIntent,
            pendingIntentFlags
        )

        session.commit(statusReceiver.intentSender)

        try {
            session.close()
        } catch (_: Exception) {
        }
    } catch (e: Exception) {
        try {
            session.close()
        } catch (_: Exception) {
        }
        try {
            installer.abandonSession(sessionId)
        } catch (_: Exception) {
        }
        finishWithResult(false)
    }
}


fun launchApp(context: Context, packageName: String) {
    val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) {
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
    } else {
        Toast.makeText(context, "未找到该应用或无法启动", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun APKPickerLauncher(
    onSuccess: (File) -> Unit,
    onError: (String) -> Unit
): (type : String) -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            onError("取消选择")
            return@rememberLauncherForActivityResult
        }

        scope.launch {
            try {
                val cacheFile = createTempFileFromUri(context, uri)
                if (cacheFile != null && cacheFile.exists()) {
                    onSuccess(cacheFile)
                } else {
                    onError("失败：文件未成功创建")
                }
            } catch (e: Exception) {
                onError("处理时发生异常: ${e.localizedMessage}")
            }
        }
    }
    return { mimeType ->
        launcher.launch(mimeType)
    }
}

public fun createTempFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("imported_apk_", ".apk", File("${context.filesDir}/temp"))

        tempFile.outputStream().use { outputStream ->
            inputStream.use { input ->
                input.copyTo(outputStream)
            }
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun hasUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(
        Context.APP_OPS_SERVICE
    ) as AppOpsManager

    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
    } else {
        @Suppress("DEPRECATION")
        appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
    }

    return mode == AppOpsManager.MODE_ALLOWED
}

