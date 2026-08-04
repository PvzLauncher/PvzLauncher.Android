package com.pvzlauncher.pvzlauncher.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.controls.RadioDialog
import com.pvzlauncher.pvzlauncher.controls.XW_InputDialog
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.ManageIndex
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.controls.XW_ManageInformationCard
import com.pvzlauncher.pvzlauncher.controls.XW_ToastMessage
import com.pvzlauncher.pvzlauncher.controls.XW_simpledialog
import com.pvzlauncher.pvzlauncher.ui.theme.XW_LightTheme
import com.pvzlauncher.pvzlauncher.utils.CurrentIndex
import com.pvzlauncher.pvzlauncher.utils.FavoriteListsConfig
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.ManagelistIndex
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.WriteJson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
public fun ManagePage()
{
    var lc = LocalContext.current
    val scope = rememberCoroutineScope()
    var isRendered by rememberSaveable { mutableStateOf(true) }
    var cfg = SaveConfigList(listOf(FavoriteListsConfig("默认收藏夹",emptyList<SaveConfig>())))
        var lcg = LauncherConfig(
            UseSystemTheme = true,
    UseDarkTheme = false,
    UseEnglishTitle = false,
    CurrentGameIndex = CurrentIndex(0,0),
    true, false,false
    )
    LaunchedEffect(Unit) {
        scope.launch {
            lcg = ReadJson<LauncherConfig>(File("${lc.filesDir}/${LAUNCHERCONFIGNAME}"))
            cfg = ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}"))
        }
    }

    Box(Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.padding(10.dp, 35.dp, 10.dp, 5.dp)) {
            Box(modifier = Modifier.fillMaxWidth())
            {
                Text(
                    "管理",
                    fontWeight = Bold,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.CenterStart),
                    fontSize = 24.sp
                )


            }


            if(isRendered)
            {
                val scrollState = rememberScrollState()
                var search by rememberSaveable {mutableStateOf("")}
                Box(Modifier.fillMaxSize())
                {

                    fun getnames() : List<String>
                    {
                        var names = emptyList<String>().toMutableList()
                        for(i in cfg.ListIndex)
                        {
                            names.add(i.listname)
                        }

                        return names
                    }
                    var isDialogVisible by rememberSaveable { mutableStateOf(false) }
                    var isDialogVisible2 by rememberSaveable { mutableStateOf(false) }
                    Column(
                        Modifier

                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    )
                    {



                        var selecteditem by rememberSaveable { mutableStateOf(0) }


                        var isDialogVisible3 by rememberSaveable { mutableStateOf(false) }
                        var tmpsel by rememberSaveable { mutableStateOf(0) }




                        RadioDialog(isDialogVisible,"提示","请选择要删除的收藏夹：",getnames().toList(),{isDialogVisible = false},{j ->
                            XW_simpledialog("警告","您确定要删除收藏夹吗？其中的游戏将永久消失！",{

                                scope.launch {
                                    val v1 = cfg.ListIndex.toMutableList()
                                    v1.removeAt(j)
                                    cfg.ListIndex = v1.toList()
                                    WriteJson(SAVECONFIGNAME, cfg, lc)
                                    if(selecteditem == j)
                                    {
                                        selecteditem = 0
                                    }
                                    if(lcg.CurrentGameIndex.ListIndex == j)
                                    {

                                        lcg.CurrentGameIndex.ListIndex = 0
                                        lcg.CurrentGameIndex.GameIndex = 0
                                        WriteJson<LauncherConfig>(
                                            LAUNCHERCONFIGNAME,lcg,lc)
                                    }
                                    isDialogVisible = false
                                    isRendered = false
                                    isRendered = true
                                }

                            },{},lc,scope)
                        })
                        RadioDialog(isDialogVisible2,"提示","请选择要改名的收藏夹：",getnames().toList(),{isDialogVisible2 = false},{j ->
                            tmpsel = j
                            isDialogVisible3 = true
                            isDialogVisible2 = false
                        })
                        XW_InputDialog(isDialogVisible3,"提示","请输入新名字","请输入……","",{isDialogVisible3 = false},{k ->
                            scope.launch {
                                val v1 = cfg.ListIndex.toMutableList()
                                v1[tmpsel].listname = k
                                cfg.ListIndex = v1.toList()
                                WriteJson(SAVECONFIGNAME, cfg, lc)
                                isDialogVisible3 = false
                                isRendered = false
                                isRendered = true
                            }
                        })


                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically)
                        {
                            SecondaryScrollableTabRow(
                                selectedTabIndex = selecteditem, Modifier.weight(1f)
                            ) {
                                cfg.ListIndex.forEachIndexed { i, c ->
                                    Tab(
                                        selected = selecteditem == i,
                                        onClick = { selecteditem = i},
                                        text = { Text(c.listname) }
                                    )
                                }

                            }
                            Row()
                            {
                                TextButton(onClick = {
                                    if(getnames().count() != 1)
                                    {
                                        isDialogVisible2 = true
                                    }
                                    else
                                    {
                                        XW_ToastMessage("暂无可改名的收藏夹")
                                    }
                                },contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        "检测更新",
                                        Modifier.size(24.dp)
                                    )
                                }
                                TextButton(onClick = {

                                    if(getnames().count() != 1)
                                    {
                                        isDialogVisible = true
                                    }
                                    else
                                    {
                                        XW_ToastMessage("暂无可删除的收藏夹")
                                    }
                                },contentPadding = PaddingValues(0.dp),modifier = Modifier.size(32.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        "检测更新",
                                        Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically)
                        {
                            Icon(
                                imageVector = Icons.Default.Search,
                                "检测更新",
                                modifier = Modifier.size(32.dp)
                            )
                            TextField(value = search, onValueChange = {search = it},Modifier.weight(1f).padding(5.dp), textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp
                            ))
                        }
                        if(cfg.ListIndex[selecteditem].GameIndex.count() != 0)
                        {
                            for (i in cfg.ListIndex[selecteditem].GameIndex) {
                                if(i.GameName.contains(search))
                                {
                                    XW_ManageInformationCard(
                                        args = i,
                                        onBack = {
                                           scope.launch {
                                               ManagelistIndex = selecteditem
                                               ManageIndex =
                                                   ReadJson<SaveConfigList>(File("${lc.filesDir}/${SAVECONFIGNAME}")).ListIndex[selecteditem].GameIndex.indexOf(
                                                       i
                                                   )
                                               CurrentDestination = AppDestinations.ManageDetailPage
                                           }
                                        },
                                        IsButtonEnable = true,

                                        )
                                }

                            }
                        }
                        else
                        {
                            Box(Modifier.fillMaxSize())
                            {
                                Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("暂无任何游戏版本",fontSize = 18.sp, fontWeight = Bold)
                                    Text("请导入或下载！", fontSize = 14.sp)
                                }
                            }
                        }
                    }

                }
            }


        }
        var isDialogVisible by remember { mutableStateOf(false) }
        var isDialogVisible2 by remember { mutableStateOf(false) }
        XW_InputDialog(isDialogVisible2,"提示","请输入收藏夹名","请输入……","",{isDialogVisible2 = false},{ s ->

            scope.launch {
                val v0 = cfg.ListIndex.toMutableList()
                v0.add(FavoriteListsConfig(s,emptyList()))
                cfg.ListIndex = v0.toList()
                WriteJson(SAVECONFIGNAME, cfg, lc)
                isRendered = false
                isRendered = true
            }

        })
        RadioDialog(isDialogVisible,"提示","请选择添加内容",listOf("添加游戏","添加收藏夹"),{isDialogVisible=false},{ i ->
            isDialogVisible=false
            when(i)
            {
                0 -> {
                    CurrentDestination = AppDestinations.ImportPage
                }
                1 -> {
                    isDialogVisible2 = true
                }

            }

        })
        FloatingActionButton(
            onClick = {
                isDialogVisible = true
            },
            modifier = Modifier
                .padding(30.dp)
                .size(48.dp)
                .align(Alignment.BottomEnd),
            shape = RoundedCornerShape(10.dp), containerColor = XW_LightTheme.primary
        )
        {

            Icon(
                imageVector = Icons.Default.Add,
                "检测更新",
                modifier = Modifier.size(32.dp)
            )

        }
    }
}