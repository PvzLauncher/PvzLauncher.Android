package com.pvzlauncher.pvzlauncher.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import kotlin.concurrent.thread
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInstaller
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

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

fun installApk(
    context: Context,
    apkFile: File,
    onSuccess: () -> Unit,
    onFailed: () -> Unit
) {
    if (!apkFile.exists()) {
        onFailed()
        return
    }

    val installer = context.packageManager.packageInstaller
    val action = "${context.packageName}.INSTALL_STATUS"

    // 1. 动态接收广播
    val statusReceiver = object : BroadcastReceiver() {
        override fun onReceive(receiverContext: Context, intent: Intent) {
            val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE)

            when (status) {
                PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                    // 拉起系统确认弹窗
                    val confirmIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(Intent.EXTRA_INTENT)
                    }
                    confirmIntent?.let {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        receiverContext.startActivity(it)
                    }
                }
                PackageInstaller.STATUS_SUCCESS -> {
                    // 1. 先安全注销广播
                    try {
                        receiverContext.unregisterReceiver(this)
                    } catch (_: Exception) {}

                    // 2. 回传成功（如果是安装别的App，这里会完美触发。如果是更新自己，进程即将被杀）
                    Handler(Looper.getMainLooper()).post {
                        onSuccess()
                    }
                }
                else -> {
                    // 失败或取消
                    try {
                        receiverContext.unregisterReceiver(this)
                    } catch (_: Exception) {}

                    Handler(Looper.getMainLooper()).post {
                        onFailed()
                    }
                }
            }
        }
    }

    // 2. 注册广播
    val filter = IntentFilter(action)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.registerReceiver(statusReceiver, filter, Context.RECEIVER_EXPORTED)
    } else {
        context.registerReceiver(statusReceiver, filter)
    }

    // 3. 写入 Session 并提交
    thread {
        try {
            val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            val sessionId = installer.createSession(params)
            val session = installer.openSession(sessionId)

            session.openWrite("temp_install", 0, apkFile.length()).use { outStream ->
                apkFile.inputStream().use { inStream ->
                    inStream.copyTo(outStream)
                }
            }

            val broadcastIntent = Intent(action).apply {
                setPackage(context.packageName)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                sessionId,
                broadcastIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )

            session.commit(pendingIntent.intentSender)
            session.close()
        } catch (e: Exception) {
            e.printStackTrace()


            try {
                context.unregisterReceiver(statusReceiver)
                onFailed()
            } catch (_: Exception) {}
            Handler(Looper.getMainLooper()).post { onFailed() }
        }
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

private fun createTempFileFromUri(context: Context, uri: Uri): File? {
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