package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.pvzlauncher.pvzlauncher.controls.XW_LoadingMask
import com.pvzlauncher.pvzlauncher.controls.XW_ToastMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


@Composable
fun rememberPhotoPickerLauncher(
    onSuccess: (File) -> Unit,
    onError: (String) -> Unit,IsBlur : Boolean = true
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
                var m = XW_LoadingMask(context,"请稍候……")
                m.show()
                if(IsBlur)
                {
                    val file = copyImageToPrivateDir(context, uri,5)
                    if (file != null && file.exists()) {
                        m.hide()
                        onSuccess(file)
                    } else {
                        m.hide()
                        onError("图片复制失败：文件未成功创建")
                    }
                }
                else
                {
                    val file = copyImageToPrivateDir(context, uri)
                    if (file != null && file.exists()) {
                        m.hide()
                        onSuccess(file)
                    } else {
                        m.hide()
                        onError("图片复制失败：文件未成功创建")
                    }
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

private suspend fun copyImageToPrivateDir(
    context: Context,
    uri: Uri,radius:Int
): File? = withContext(Dispatchers.IO) {

    val destFile = File(context.filesDir, "Background.png")

    if (destFile.exists()) {
        withContext(Dispatchers.Main) {
            XW_ToastMessage("需要重启应用以替换背景！")
        }
    }

    // 复制图片
    context.contentResolver.openInputStream(uri)?.use { input ->
        destFile.outputStream().use { output ->
            input.copyTo(output)
        }
    } ?: return@withContext null


    // ===== Bitmap 高斯模糊 =====

    val src = BitmapFactory.decodeFile(destFile.absolutePath)
        ?: return@withContext destFile

    // 缩小处理
    val small = Bitmap.createScaledBitmap(
        src,
        (src.width).coerceAtLeast(1),
        (src.height).coerceAtLeast(1),
        true
    )

    val bitmap = small.copy(
        Bitmap.Config.ARGB_8888,
        true
    )

    val w = bitmap.width
    val h = bitmap.height

    val pixels = IntArray(w * h)

    bitmap.getPixels(
        pixels,
        0,
        w,
        0,
        0,
        w,
        h
    )

    val temp = IntArray(w * h)


    // 横向模糊
    for (y in 0 until h) {
        for (x in 0 until w) {

            var r = 0
            var g = 0
            var b = 0
            var count = 0

            for (i in -radius..radius) {

                val px = (x + i).coerceIn(0, w - 1)
                val color = pixels[y * w + px]

                r += color shr 16 and 0xff
                g += color shr 8 and 0xff
                b += color and 0xff

                count++
            }

            temp[y * w + x] =
                (0xff shl 24) or
                        ((r / count) shl 16) or
                        ((g / count) shl 8) or
                        (b / count)
        }
    }


    // 纵向模糊
    for (x in 0 until w) {
        for (y in 0 until h) {

            var r = 0
            var g = 0
            var b = 0
            var count = 0

            for (i in -radius..radius) {

                val py = (y + i).coerceIn(0, h - 1)
                val color = temp[py * w + x]

                r += color shr 16 and 0xff
                g += color shr 8 and 0xff
                b += color and 0xff

                count++
            }

            pixels[y * w + x] =
                (0xff shl 24) or
                        ((r / count) shl 16) or
                        ((g / count) shl 8) or
                        (b / count)
        }
    }


    bitmap.setPixels(
        pixels,
        0,
        w,
        0,
        0,
        w,
        h
    )


    // 恢复原尺寸
    val result = Bitmap.createScaledBitmap(
        bitmap,
        src.width,
        src.height,
        true
    )


    // 覆盖保存
    FileOutputStream(destFile).use {
        result.compress(
            Bitmap.CompressFormat.PNG,
            100,
            it
        )
    }


    src.recycle()
    small.recycle()
    bitmap.recycle()
    result.recycle()


    destFile
}

private suspend fun copyImageToPrivateDir(context: Context, uri: Uri): File? = withContext(Dispatchers.IO) {
    val destFile = File(context.filesDir, "Title.png")
    context.contentResolver.openInputStream(uri)?.use { input ->
        destFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    destFile
}