package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun rememberPhotoPickerLauncher(
    onSuccess: (File) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri == null) {
            onError("取消选择照片")
            return@rememberLauncherForActivityResult
        }

        scope.launch {
            try {
                val file = copyImageToPrivateDir(context, uri)
                if (file != null && file.exists()) {
                    onSuccess(file)
                } else {
                    onError("图片复制失败：文件未成功创建")
                }
            } catch (e: Exception) {
                onError("处理图片时发生异常: ${e.localizedMessage}")
            }
        }
    }

    return {
        launcher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}

private suspend fun copyImageToPrivateDir(context: Context, uri: Uri): File? = withContext(Dispatchers.IO) {
    val destFile = File(context.filesDir, "Background.png")
    context.contentResolver.openInputStream(uri)?.use { input ->
        destFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    destFile
}