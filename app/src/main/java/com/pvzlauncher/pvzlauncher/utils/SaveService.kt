package com.pvzlauncher.pvzlauncher.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


// =======================
// 数据结构
// =======================

data class DirectoryNode(
    val name: String,
    val path: String,

    // null 表示未加载
    val children: List<DirectoryNode>? = null,

    val expanded: Boolean = false,

    val loading: Boolean = false
)


// =======================
// 加载当前目录
// =======================

suspend fun loadDirectoryChildren(
    path: String
): List<DirectoryNode> {

    return withContext(Dispatchers.IO) {

        val file = File(path)

        file.listFiles()
            ?.filter {
                it.isDirectory
            }
            ?.map {

                DirectoryNode(
                    name = it.name,
                    path = it.absolutePath
                )

            }
            ?: emptyList()
    }
}


// =======================
// 更新树节点
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

                children =
                    node.children?.let {

                        updateNode(
                            it,
                            targetPath,
                            update
                        )

                    }

            )
        }
    }
}


// =======================
// 展开目录
// =======================

suspend fun expandNode(
    nodes: List<DirectoryNode>,
    path: String
): List<DirectoryNode> {


    val target =
        findNode(nodes, path)
            ?: return nodes



    // 已经加载过

    if (target.children != null) {


        return updateNode(
            nodes,
            path
        ) {

            it.copy(
                expanded = !it.expanded
            )

        }

    }



    // 显示加载状态

    var result =
        updateNode(
            nodes,
            path
        ) {

            it.copy(
                loading = true
            )

        }



    val children =
        loadDirectoryChildren(path)



    result =
        updateNode(
            result,
            path
        ) {

            it.copy(

                children = children,

                expanded = true,

                loading = false

            )

        }


    return result
}



// 查找节点

fun findNode(
    list: List<DirectoryNode>,
    path: String
): DirectoryNode? {


    list.forEach {


        if (it.path == path)
            return it



        it.children?.let { child ->

            findNode(
                child,
                path
            )?.let { result ->

                return result

            }

        }

    }


    return null
}




// =======================
// UI
// =======================

@Composable
fun DirectoryPicker(
    rootPath: String,
    onDirectorySelected: (String) -> Unit
) {


    var tree by remember {


        mutableStateOf(

            listOf(

                DirectoryNode(

                    name =
                        File(rootPath).name.ifEmpty {
                            rootPath
                        },

                    path = rootPath

                )

            )

        )

    }



    var selected by remember {

        mutableStateOf<String?>(null)

    }



    val scope =
        rememberCoroutineScope()



    Column {


        tree.forEach {


            DirectoryRow(

                node = it,

                level = 0,

                selected = selected,


                onSelect = { path ->


                    selected = path


                    onDirectorySelected(path)

                },


                onExpand = { path ->


                    scope.launch {


                        tree =
                            expandNode(
                                tree,
                                path
                            )


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

    onSelect: (String)->Unit,

    onExpand: (String)->Unit
) {


    Column {


        Row(

            modifier =
                Modifier

                    .fillMaxWidth()

                    .padding(
                        start =
                            (level * 24).dp
                    ),


            verticalAlignment =
                Alignment.CenterVertically

        ) {



            // 箭头

            IconButton(

                onClick = {

                    onExpand(node.path)

                }

            ) {


                Icon(

                    imageVector =

                        if(node.expanded)

                            Icons.Default.KeyboardArrowDown

                        else

                            Icons.Default.KeyboardArrowRight,


                    contentDescription = null

                )

            }

            Icon(
                imageVector = Icons.Default.Folder,
                "",
                modifier = Modifier.size(32.dp)
            )


            RadioButton(

                selected =
                    selected == node.path,


                onClick = {

                    onSelect(node.path)

                }

            )




            Text(

                text = node.name,

                modifier =
                    Modifier.weight(1f)

            )




            if(node.loading) {


                CircularProgressIndicator(

                    modifier =
                        Modifier.size(18.dp)

                )

            }

        }



        // 子目录

        if(node.expanded) {


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