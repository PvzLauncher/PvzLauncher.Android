package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import java.util.zip.ZipInputStream
import io.github.lumkit.io.LintFile
import io.github.lumkit.io.*
import kotlinx.coroutines.suspendCancellableCoroutine
import io.github.lumkit.io.file
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun zipFolder(
    activity: ComponentActivity,
    pkg: String,
    sourceRelPath: String,
    zipFile: LintFile
) = withContext(Dispatchers.IO) {

    // 1. 挂起等待获取 Android/data 下的源文件夹权限及实例
    val source = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine<LintFile> { cont ->
            var resumed = false
            accessAndroidDataWithResult(
                activity = activity,
                pkg = pkg,
                relPath = sourceRelPath,
                onResult = { ok ->
                    if (!ok && !resumed) {
                        resumed = true
                        cont.resumeWithException(SecurityException("无法获取 $pkg/$sourceRelPath 的访问权限"))
                    }
                },
                block = {
                    if (!resumed) {
                        resumed = true
                        cont.resume(this) // 拿到 LintFile 实例并恢复协程
                    }
                }
            )
        }
    }

    // 2. 执行压缩逻辑
    if (!source.isDirectory()) {
        error("${source.path}必须是文件夹")
    }

    zipFile.delete()

    ZipOutputStream(
        BufferedOutputStream(
            zipFile.openOutputStream()
        )
    ).use { zipOut ->

        fun addFile(file: LintFile, base: String) {
            if (file.isDirectory()) {
                file.listFiles()?.forEach {
                    addFile(it, base)
                }
            } else {
                val entryName = file.path.removePrefix(base)
                    .replace("\\", "/")
                    .trimStart('/')

                zipOut.putNextEntry(ZipEntry(entryName))
                file.openInputStream().use { input ->
                    input.copyTo(zipOut)
                }
                zipOut.closeEntry()
            }
        }

        addFile(source, source.path)
    }
}

suspend fun unzipFolder(
    activity: ComponentActivity,
    zipFile: LintFile,
    pkg: String,
    targetRelPath: String
) = withContext(Dispatchers.IO) {

    // 1. 挂起等待获取 Android/data 下的目标文件夹权限及实例
    val target = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine<LintFile> { cont ->
            var resumed = false
            accessAndroidDataWithResult(
                activity = activity,
                pkg = pkg,
                relPath = targetRelPath,
                onResult = { ok ->
                    if (!ok && !resumed) {
                        resumed = true
                        cont.resumeWithException(SecurityException("无法获取 $pkg/$targetRelPath 的访问权限"))
                    }
                },
                block = {
                    if (!resumed) {
                        resumed = true
                        cont.resume(this)
                    }
                }
            )
        }
    }

    // 确保目标根目录存在（不执行 delete，保证增量保留原有文件）
    if (!target.exists()) {
        target.mkdirs()
    }

    ZipInputStream(
        zipFile.openInputStream()
    ).use { input ->

        var entry = input.nextEntry

        while (entry != null) {
            val cleanEntryName = entry.name.trimStart('/')

            // ★ 安全防护：防止 Zip Slip (路径穿越) 漏洞，避免恶意 zip 覆盖系统文件
            if (cleanEntryName.contains("../")) {
                entry = input.nextEntry
                continue
            }

            val childRelPath = if (targetRelPath.isEmpty()) cleanEntryName else "$targetRelPath/$cleanEntryName"
            val outFile = file(dataFile(pkg, childRelPath))

            if (entry.isDirectory) {
                outFile.mkdirs()
            } else {
                // ★ 增量覆盖核心优化：
                // 如果文件已存在，且大小与 zip 中的记录一致，则跳过（认为未修改），大幅减少 IO 操作
                val isUnchanged = outFile.exists() && entry.size != -1L && outFile.length() == entry.size

                if (!isUnchanged) {
                    outFile.getParentFile()?.mkdirs()

                    // ★ 覆盖前先删除旧文件
                    // 防止某些安卓系统/定制 ROM 下直接 OutputStream 覆盖导致文件损坏或权限残留
                    if (outFile.exists()) {
                        outFile.delete()
                    }

                    outFile.openOutputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }

            entry = input.nextEntry
        }
    }
}


suspend fun uriToLintFile(
    context: Context,
    uri: Uri
): LintFile = withContext(Dispatchers.IO) {

    val fileName = "selected_${System.currentTimeMillis()}.zip"

    val tempFile = File(
        context.cacheDir,
        fileName
    )

    context.contentResolver
        .openInputStream(uri)
        ?.use { input ->

            tempFile.outputStream()
                .use { output ->
                    input.copyTo(output)
                }
        }

    return@withContext file(tempFile.absolutePath)
}

@Composable
fun ZIPPickerLauncher(
    onSuccess: (Uri) -> Unit,
    onError: (String) -> Unit
): () -> Unit {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            onError("取消选择")
            return@rememberLauncherForActivityResult
        }

        onSuccess(uri)
    }
    return {
        launcher.launch("application/zip")
    }

}

fun ZipShare(context: Context,file: File) {
    val imageUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, imageUri)
        type = "*/*"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val shareIntent = Intent.createChooser(sendIntent, "导出存档")
    context.startActivity(shareIntent)
}

