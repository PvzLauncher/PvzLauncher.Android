package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.pvzlauncher.pvzlauncher.controls.XW_simpledialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun createTempOBBFileFromUri(
    context: Context,
    uri: Uri,
    path: String
) = withContext(Dispatchers.IO) {
    try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext

        File(path).outputStream().use { outputStream ->
            inputStream.use { input ->
                input.copyTo(outputStream)
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

suspend fun deleteOBBFile(path: String): Boolean = withContext(Dispatchers.IO) {
    try {
        File(path).deleteRecursively()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun getFileName(context: Context, uri: Uri): String? {
    var name: String? = null

    context.contentResolver.query(
        uri,
        null,
        null,
        null,
        null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0) {
                name = cursor.getString(index)
            }
        }
    }

    return name
}


@Composable
fun OBBPickerLauncher(
    onSuccess: (Uri) -> Unit,
    onError: (String) -> Unit
): (type : String) -> Unit {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            onError("取消选择")
            return@rememberLauncherForActivityResult
        }

        onSuccess(uri)
    }
    return { mimeType ->
        launcher.launch(mimeType)
    }
}


fun requestObbPermission(
    context: Context,
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if(!Environment.isExternalStorageManager())
        {
            XW_simpledialog("权限申请(1/2)","此程序需要申请读取存储权限来管理并安装您的游戏obb，请您批准！",{
                val intent = Intent(
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                )

                intent.data = Uri.parse(
                    "package:${context.packageName}"
                )

                context.startActivity(intent)
            },{},context)

        }
    }
}