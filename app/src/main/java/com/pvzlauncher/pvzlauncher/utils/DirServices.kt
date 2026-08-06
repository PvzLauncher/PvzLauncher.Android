package com.pvzlauncher.pvzlauncher.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.lumkit.io.LintFile
import io.github.lumkit.io.file
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// =======================
// 数据结构
// =======================

data class DirectoryNode(
    val name: String,
    val path: String,
    val children: List<DirectoryNode>? = null,   // null 表示未加载
    val expanded: Boolean = false,
    val loading: Boolean = false
)

// =======================
// 加载当前目录（java.io.File → LintFile）
// =======================

suspend fun loadDirectoryChildren(
    path: String
): List<DirectoryNode> {

    return withContext(Dispatchers.IO) {

        runCatching {
            file(path)                            // ★ LintFile，不是 java.io.File
                .listFiles()
                ?.filter { it.isDirectory() }     // LintFile 是方法调用，不是属性
                ?.sortedBy { it.name.lowercase() }
                ?.map {
                    DirectoryNode(
                        name = it.name,
                        // 用父路径拼，避免依赖 LintFile 内部 path 表示
                        path = "$path/${it.name}"
                    )
                }
        }.getOrNull() ?: emptyList()              // 无权限/目录不存在 → 空列表，不崩
    }
}

// =======================
// 更新树节点（纯逻辑，不变）
// =======================

fun updateNode(
    list: List<DirectoryNode>,
    targetPath: String,
    update: (DirectoryNode) -> DirectoryNode
): List<DirectoryNode> {

    return list.map { node ->
        if (node.path == targetPath) {
            update(node)
        } else {
            node.copy(
                children = node.children?.let {
                    updateNode(it, targetPath, update)
                }
            )
        }
    }
}

// =======================
// 展开目录（不变）
// =======================

suspend fun expandNode(
    nodes: List<DirectoryNode>,
    path: String
): List<DirectoryNode> {

    val target = findNode(nodes, path) ?: return nodes

    // 已经加载过
    if (target.children != null) {
        return updateNode(nodes, path) {
            it.copy(expanded = !it.expanded)
        }
    }

    // 显示加载状态
    var result = updateNode(nodes, path) {
        it.copy(loading = true)
    }

    val children = loadDirectoryChildren(path)

    result = updateNode(result, path) {
        it.copy(children = children, expanded = true, loading = false)
    }

    return result
}

// 查找节点（不变）

fun findNode(
    list: List<DirectoryNode>,
    path: String
): DirectoryNode? {

    list.forEach {
        if (it.path == path) return it
        it.children?.let { child ->
            findNode(child, path)?.let { result -> return result }
        }
    }

    return null
}

// =======================
// UI
// =======================

@Composable
fun DirectoryPicker(
    root: LintFile,
    onDirectorySelected: (String) -> Unit,  // ★ 现在传出的是相对路径
    onPermissionFailed: () -> Unit = {}
) {
    val context = LocalContext.current
    val rootPath = root.path

    // ==================== 新增：计算包根路径 ====================
    // 从 rootPath 提取 "/storage/emulated/0/Android/data/<pkg>" 作为基准
    val pkgRoot = remember(rootPath) {
        val marker = "$ANDROID_DATA_ROOT/"
        val idx = rootPath.indexOf(marker)
        if (idx >= 0) {
            val afterMarker = rootPath.substring(idx + marker.length)
            val pkg = afterMarker.substringBefore('/') // 取第一段作为包名
            "$marker$pkg"
        } else {
            rootPath // 非 Android/data 路径，不截取
        }
    }

    // ==================== 新增：相对路径转换工具 ====================
    fun toRelativePath(absolutePath: String): String {
        if (!absolutePath.startsWith(pkgRoot)) return absolutePath
        val rel = absolutePath.removePrefix(pkgRoot)
            .replace("\\", "/")
            .trimStart('/')
        return rel // 如果选中的就是包根目录，返回 ""
    }

    // 首次进入且根在 Android/data 下 → 自动走授权探测链
    var permissionReady by remember(rootPath) { mutableStateOf(false) }

    LaunchedEffect(rootPath) {
        if (rootPath.startsWith(ANDROID_DATA_ROOT)) {
            requestAndroidDataAccess(
                context = context,
                onSuccess = { permissionReady = true },
                onFailed = { onPermissionFailed() }
            )
        } else {
            permissionReady = true
        }
    }

    if (!permissionReady) return

    var tree by remember(rootPath) {
        mutableStateOf(
            listOf(
                DirectoryNode(
                    name = root.name.ifEmpty { rootPath },
                    path = rootPath
                )
            )
        )
    }

    var selected by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column {
        tree.forEach {
            DirectoryRow(
                node = it,
                level = 0,
                selected = selected,
                onSelect = { path ->
                    selected = path
                    // ★ 关键改动：传出相对路径，而非绝对路径
                    onDirectorySelected(toRelativePath(path))
                },
                onExpand = { path ->
                    scope.launch {
                        tree = expandNode(tree, path)
                    }
                }
            )
        }
    }
}

@Composable
fun DirectoryRow(
    node: DirectoryNode,
    level: Int,
    selected: String?,
    onSelect: (String) -> Unit,
    onExpand: (String) -> Unit
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = (level * 24).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 箭头
            IconButton(
                onClick = { onExpand(node.path) }
            ) {
                Icon(
                    imageVector = if (node.expanded)
                        Icons.Default.KeyboardArrowDown
                    else
                        Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            }

            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = "",
                modifier = Modifier.size(32.dp)
            )

            RadioButton(
                selected = selected == node.path,
                onClick = { onSelect(node.path) }
            )

            Text(
                text = node.name,
                modifier = Modifier.weight(1f)
            )

            if (node.loading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp))
            }
        }

        // 子目录
        if (node.expanded) {
            node.children?.forEach {
                DirectoryRow(
                    node = it,
                    level = level + 1,
                    selected = selected,
                    onSelect = onSelect,
                    onExpand = onExpand
                )
            }
        }
    }
}