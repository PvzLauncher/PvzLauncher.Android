package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.github.lumkit.io.LintFile
import io.github.lumkit.io.LintFileConfiguration
import io.github.lumkit.io.data.PermissionType
import io.github.lumkit.io.file
import io.github.lumkit.io.use


// ==================== 常量 ====================

const val REQ_STORAGE = 0x1001
const val REQ_SAF     = 0x1002

const val ANDROID_DATA_ROOT = "/storage/emulated/0/Android/data"
private const val EXTERNAL_STORAGE_AUTHORITY = "com.android.externalstorage.documents"
private const val TREE_ANDROID_DATA = "primary:Android/data"

const val EVENT_GRANTED              = "GRANTED"
const val EVENT_WAITING_RUNTIME_PERM = "WAITING_RUNTIME_PERM"
const val EVENT_WAITING_SAF          = "WAITING_SAF"
const val EVENT_DENIED               = "DENIED"
const val EVENT_UNAVAILABLE          = "UNAVAILABLE"

// ==================== 顶层状态（单 Activity 场景） ====================

private var pendingRetry: (() -> Unit)? = null
private var retryCount = 0
private var onAuthDone: ((Boolean) -> Unit)? = null
private var onSafDone: ((Boolean) -> Unit)? = null

// ==================== 路径工具 ====================

fun dataDir(pkg: String) = "$ANDROID_DATA_ROOT/$pkg"

fun dataFile(pkg: String, relPath: String) = "${dataDir(pkg)}/$relPath"

// ==================== 初始化 / 释放 ====================

fun initLintFile(activity: ComponentActivity) {
    LintFileConfiguration.instance.init(activity)   // NORMAL 模式，不碰 Shizuku
}

fun destroyLintFile() {
    LintFileConfiguration.instance.destroy()
}

// ==================== ① 授权入口（三形参，探测驱动，无 pkg） ====================
//
// 方法1：已有持久化授权（整树或任意包目录）→ 成功
// 方法2：直接读写探测（含库内置零宽绕过）→ 成功
// 方法3：运行时存储权限（仅 ≤10 存在此方法）→ 授权后复探
// 方法4：SAF 授权
//        - 10~12：弹整树授权
//        - 13+ ：整树被系统禁止、按包又需要包名 → 直接 onSuccess，
//                 由 accessAndroidData(pkg) 首次访问时用包名自动完成授权

fun requestAndroidDataAccess(
    context: Context,
    onSuccess: () -> Unit,
    onFailed: () -> Unit
) {
    val activity = context as? ComponentActivity ?: run { onFailed(); return }

    // 方法1：已有持久化授权覆盖 Android/data
    if (hasAnyAndroidDataGrant(activity)) { onSuccess(); return }

    // 方法2：能直接读写（零宽绕过有效）
    if (probeDirectAccessRoot()) { onSuccess(); return }

    // 方法3：运行时存储权限（该方法只在 Android ≤10 存在）
    if (Build.VERSION.SDK_INT <= 29 && !hasRuntimeStoragePermission(activity)) {
        onAuthDone = { granted ->
            if (granted && probeDirectAccessRoot()) {
                onSuccess()                          // 权限生效，直读写可用
            } else {
                trySafStage(activity, onSuccess, onFailed)   // 被拒，或给了权限仍读不了（10 scoped）→ 继续试 SAF
            }
        }
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            REQ_STORAGE
        )
        return
    }

    // 方法4
    trySafStage(activity, onSuccess, onFailed)
}

private fun trySafStage(
    activity: ComponentActivity,
    onSuccess: () -> Unit,
    onFailed: () -> Unit
) {
    // 13+：无法整树授权，按包授权推迟到 accessAndroidData() 时自动完成 → 直接成功
    if (Build.VERSION.SDK_INT >= 33) { onSuccess(); return }

    // 10~12：弹 SAF 整树授权
    launchSaf(activity, TREE_ANDROID_DATA) { ok ->
        if (ok) onSuccess() else onFailed()
    }
}

// ==================== ② 读写入口（包名动态，探测失败自动补授权并重放） ====================

fun accessAndroidData(
    activity: ComponentActivity,
    pkg: String,
    relPath: String,
    onEvent: ((String) -> Unit)? = null,
    block: LintFile.() -> Unit
) {
    file(dataFile(pkg, relPath)).use(
        onRequestPermission = { type ->
            when (type) {
                PermissionType.EXTERNAL_STORAGE -> {
                    if (hasRuntimeStoragePermission(activity)) {
                        // ★ 权限已有却仍读不了 → 该权限在本设备无效（Android 10 scoped storage）
                        //   不再重复要权限，换下一个方法：用传入的包名走 SAF
                        startSafForAccess(activity, pkg, relPath, onEvent, block)
                    } else {
                        onEvent?.invoke(EVENT_WAITING_RUNTIME_PERM)
                        pendingRetry = { accessAndroidData(activity, pkg, relPath, onEvent, block) }
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            REQ_STORAGE
                        )
                    }
                }

                PermissionType.STORAGE_ACCESS_FRAMEWORK ->
                    startSafForAccess(activity, pkg, relPath, onEvent, block)

                else -> {}
            }
        },
        granted = {
            retryCount = 0
            pendingRetry = null
            onEvent?.invoke(EVENT_GRANTED)
            block(this)   // this == LintFile，API 与 java.io.File 一致
        }
    )
}

private fun startSafForAccess(
    activity: ComponentActivity,
    pkg: String,
    relPath: String,
    onEvent: ((String) -> Unit)?,
    block: LintFile.() -> Unit
) {
    onEvent?.invoke(EVENT_WAITING_SAF)
    pendingRetry = { accessAndroidData(activity, pkg, relPath, onEvent, block) }
    launchSaf(activity, "$TREE_ANDROID_DATA/$pkg") { ok ->
        if (!ok) onEvent?.invoke(EVENT_DENIED)
    }
}

// ==================== ③ SAF：统一入口（docId 决定整树/按包） ====================

private fun launchSaf(
    activity: ComponentActivity,
    docId: String,
    done: ((Boolean) -> Unit)? = null
) {
    onSafDone = done
    val encoded = Uri.encode(docId)
    val initialUri = Uri.parse(
        "content://$EXTERNAL_STORAGE_AUTHORITY/tree/$encoded/document/$encoded"
    )
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)
    }
    runCatching {
        @Suppress("DEPRECATION")
        activity.startActivityForResult(intent, REQ_SAF)
    }.onFailure {
        onSafDone?.invoke(false)
        onSafDone = null
    }
}

/** 校验授权结果确实落在 Android/data 下（Android 14 后期的重定向在此被识破） */
private fun isAndroidDataGrant(uri: Uri): Boolean {
    if (uri.authority != EXTERNAL_STORAGE_AUTHORITY) return false
    val docId = runCatching { DocumentsContract.getTreeDocumentId(uri) }.getOrNull()
        ?: return false
    return docId == TREE_ANDROID_DATA || docId.startsWith("$TREE_ANDROID_DATA/")
}

/** 是否已持久化持有 Android/data 的任何授权（整树或任意包目录） */
private fun hasAnyAndroidDataGrant(context: Context): Boolean =
    context.contentResolver.persistedUriPermissions.any { p ->
        if (!p.isReadPermission || !p.isWritePermission) return@any false
        if (p.uri.authority != EXTERNAL_STORAGE_AUTHORITY) return@any false
        val docId = runCatching { DocumentsContract.getTreeDocumentId(p.uri) }.getOrNull()
            ?: return@any false
        docId == TREE_ANDROID_DATA || docId.startsWith("$TREE_ANDROID_DATA/")
    }

/** 直接读写探测（file() 内部含零宽绕过探测与缓存） */
private fun probeDirectAccessRoot(): Boolean = runCatching {
    file(ANDROID_DATA_ROOT).list() != null
}.getOrDefault(false)

private fun hasRuntimeStoragePermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

// ==================== ④ Activity 结果转发 ====================

fun onStoragePermissionResult(
    requestCode: Int,
    grantResults: IntArray,
    onEvent: ((String) -> Unit)? = null
) {
    if (requestCode != REQ_STORAGE) return
    val ok = grantResults.isNotEmpty() &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    if (ok) retryPending(onEvent) else pendingRetry = null
    onAuthDone?.invoke(ok)
    onAuthDone = null
}

fun onSafActivityResult(
    activity: ComponentActivity,
    requestCode: Int,
    resultCode: Int,
    data: Intent?,
    onEvent: ((String) -> Unit)? = null
) {
    if (requestCode != REQ_SAF) return
    val uri = data?.data
    val ok = resultCode == ComponentActivity.RESULT_OK &&
            uri != null && isAndroidDataGrant(uri)
    if (ok) {
        // ★ 持久化保存授权：重启不失效
        activity.contentResolver.takePersistableUriPermission(
            uri!!,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        retryPending(onEvent)          // 自动重放刚才那次访问
    } else {
        pendingRetry = null
    }
    onSafDone?.invoke(ok)
    onSafDone = null
}

private fun retryPending(onEvent: ((String) -> Unit)?) {
    val op = pendingRetry ?: return
    if (++retryCount > 2) {            // 熔断，防死循环
        pendingRetry = null
        onEvent?.invoke(EVENT_UNAVAILABLE)
        return
    }
    op()
}

fun accessAndroidDataWithResult(
    activity: ComponentActivity,
    pkg: String,
    relPath: String,
    onResult: (Boolean) -> Unit,       // ★ true=权限成功且即将读写，false=失败
    block: LintFile.() -> Unit
) {
    var reported = false
    fun report(ok: Boolean) {
        if (!reported) { reported = true; onResult(ok) }
    }

    accessAndroidData(
        activity = activity,
        pkg = pkg,
        relPath = relPath,
        onEvent = { event ->
            when (event) {
                EVENT_GRANTED -> report(true)
                EVENT_DENIED,
                EVENT_UNAVAILABLE -> report(false)
                else -> {}
            }
        },
        block = block
    )
}