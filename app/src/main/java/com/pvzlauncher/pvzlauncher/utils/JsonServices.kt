package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.core.content.FileProvider
import kotlinx.coroutines.launch
import java.io.File


@Serializable
public data class UpdateConfig(
    val LatestVersion : String,
    var LatestDescription : String,
    val LatestLink : String
)

@Serializable
public data class GameConfig(
    var GameName : String,
    val GameLink : List<VersionConfig>,
    val GameImage : String,
    val GameDescription : String,
    val ScreenShoot : List<String>,
    val latestupdatetime : String,
    val supportVersion : SupportConfig,
    val recommend : Boolean
)

@Serializable
public data class VersionConfig(
    val VersionName :String,
    val VersionLink:String,
    val VersionSize:String,
    val VersionVer : String,

)

@Serializable
public data class SupportConfig(
    val SupportPlatform : List<String>,
    val SupportSystem : Int
)

@Serializable
public data class GameKindsConfig(
    var kindname : String,
    var GameIndex : List<GameConfig>
)

@Serializable
public data class GameListConfig(
    val ListIndex : List<GameKindsConfig>
)

@Serializable
public data class SaveConfig(
    val headImage: String,
    val gameversion : String,
    var GameName : String,
    val PackageName : String,
    val AddTime : String,
    var PlayTime : Long,
    var LaunchTimes : Long,
    var like : Boolean

)

@Serializable
public data class SaveConfigList(
    var ListIndex : List<FavoriteListsConfig>
)

@Serializable
public data class FavoriteListsConfig(
    var listname : String,
    var GameIndex : List<SaveConfig>
)

@Serializable
public data class LauncherConfig(
    var UseSystemTheme: Boolean,
    var UseDarkTheme : Boolean,
    var UseEnglishTitle : Boolean,
    var CurrentGameIndex : CurrentIndex,
    var StartUpCheckUpdate : Boolean,
    var CostumThemeColor : Boolean,
    var CostumBackground : Boolean
)

@Serializable
public data class CurrentIndex(
    var GameIndex : Int,
    var ListIndex : Int
)




public inline fun <reified T> ReadJson(jsonString : String) : T
{
    return Json.decodeFromString<T>(jsonString)
}

public inline fun <reified T> WriteJson(fileName: String, data: T,context: Context)
{
    val writepath = context.filesDir
    val jsonString = Json.encodeToString(data)
    File("${writepath}/${fileName}").writeText(jsonString, Charsets.UTF_8)
}

fun shareConfig(context: Context) {
    val imageUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        File("${context.filesDir}/${LAUNCHERCONFIGNAME}")
    )
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, imageUri)
        type = "json/*"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val shareIntent = Intent.createChooser(sendIntent, "导出设置")
    context.startActivity(shareIntent)
}

@Composable
fun JsonPickerLauncher(
    onSuccess: (File) -> Unit,
    onError: (String) -> Unit
): (String) -> Unit {
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
        val tempFile = File.createTempFile("imported_json_", ".json", File("${context.filesDir}/temp"))

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