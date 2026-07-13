package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import android.graphics.pdf.content.PdfPageGotoLinkContent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.pvzlauncher.pvzlauncher.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import android.app.Dialog
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pvzlauncher.pvzlauncher.AppDestinations
import java.util.Objects
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.unit.dp
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


@Composable
public fun XW_Switch(text : String,modifier: Modifier,isEnabled : Boolean,OnChanged : (isChecked : Boolean) -> Unit) {
    var checked by remember { mutableStateOf(isEnabled) }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(text,modifier = Modifier.padding(10.dp,5.dp), fontSize = 16.sp)

        Switch(
            checked = checked,
            onCheckedChange = { checked = it
                OnChanged(checked) }
        )
    }
}

@Composable
public fun XW_GameInformationCard(args : GameConfig,onBack: () -> Unit,Disablebuttonwhenclick : Boolean,Icon: ImageVector,ischooseindex : Boolean)
{
    var isEnabled by remember { mutableStateOf(true) }
    return Card(modifier = Modifier.padding(5.dp).fillMaxWidth())
    {
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth())
        {
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(10.dp,10.dp,64.dp,10.dp), verticalAlignment = Alignment.CenterVertically)
            {
                val cont = LocalContext.current
                AsyncImage(model = args.GameImage,"", modifier = Modifier.padding(10.dp,5.dp).size(48.dp),placeholder = painterResource(
                    R.drawable.ic_unknown), error = painterResource(R.drawable.ic_unknown),onError = { state -> XW_ToastMessage("获取头图时发生错误：${state.result.throwable.message}",cont)})
                Column()
                {
                    Text(args.GameName,modifier = Modifier.padding(2.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Row(modifier = Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically)
                    {
                        Box(modifier = Modifier
                            .background(Color(0xFFA9A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp)
                        )

                        {
                            //Text(args.GameVersion)
                            Column(){
                                for(i in 0 until args.GameLink.count())
                                {
                                    if(i != (args.GameLink.count()-1))
                                    {
                                        Text("${args.GameLink[i].VersionVer}/")
                                    }
                                    else
                                    {
                                        Text(args.GameLink[i].VersionVer)
                                    }
                                }
                            }
                        }
                        Box(modifier = Modifier.padding(2.5.dp)) { }
                        Box(modifier = Modifier.background(Color(0xFF31A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp))
                        {
                            //Text(args.GameSize)
                            Column() {
                                for(i in 0 until args.GameLink.count())
                                {
                                    if(i != (args.GameLink.count()-1))
                                    {
                                        Text("${args.GameLink[i].VersionSize}/")
                                    }
                                    else
                                    {
                                        Text(args.GameLink[i].VersionSize)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            var isvisible by rememberSaveable { mutableStateOf(false) }
            RadioDialog(isvisible,args.GameLink,{isvisible = false},{ i -> DownloadCount = i
                isvisible = false
                DownloadConfig = args

                onBack()})
            Button(onClick = {
                if(!ischooseindex)
                {
                    DownloadConfig = args

                    onBack()
                }
                else
                {
                    isvisible = true
                }
            }, modifier = Modifier.align(Alignment.CenterEnd).padding(5.dp).size(48.dp),contentPadding = PaddingValues(0.dp),
                shape = CircleShape, enabled = isEnabled
            )
            {

                Icon(imageVector = Icon,"检测更新", modifier = Modifier.size(32.dp))

            }


        }
    }
}



@Composable
public fun XW_ManageInformationCard(args : SaveConfig,onBack: () -> Unit, IsButtonEnable :  Boolean)
{

    return Card(modifier = Modifier.padding(5.dp).fillMaxWidth())
    {
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth())
        {
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(10.dp,10.dp,64.dp,10.dp), verticalAlignment = Alignment.CenterVertically)
            {
                val cont = LocalContext.current

                    AsyncImage(model = args.headImage,"", modifier = Modifier.padding(10.dp,5.dp).size(48.dp),placeholder = painterResource(
                        R.drawable.ic_unknown), error = painterResource(R.drawable.ic_unknown),onError = { state -> XW_ToastMessage("获取头图时发生错误：${state.result.throwable.message}",cont)})

                Column()
                {
                    Text(args.GameName,modifier = Modifier.padding(2.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Row(modifier = Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically)
                    {
                        Box(modifier = Modifier
                            .background(Color(0xFFA9A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp)
                        )

                        {
                            Text(args.gameversion)
                        }
//                        Box(modifier = Modifier.background(Color(0xFF31A9A9), RoundedCornerShape(7.5.dp))
//                            .padding(4.dp,2.dp))
//                        {
//                            Text(args.GameSize)
//                        }
                        val rj =  ReadJson<SaveConfigList>(File("${cont.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex
                        if(rj.indexOf(args) == ReadJson<LauncherConfig>(File("${cont.filesDir}/${LAUNCHERCONFIGNAME}").readText()).CurrentGameIndex)
                        {
                            Box(modifier = Modifier.background(Color.Red, RoundedCornerShape(7.5.dp))
                                .padding(4.dp,2.dp))
                            {
                                Text("活动")
                            }
                        }


                    }
                }
            }



            if(IsButtonEnable)
            {
                Button(onClick = {
                    onBack()
                }, modifier = Modifier.align(Alignment.CenterEnd).padding(5.dp).size(48.dp),contentPadding = PaddingValues(0.dp),
                    shape = CircleShape
                )
                {

                    Icon(imageVector = Icons.Default.ArrowRightAlt,"检测更新", modifier = Modifier.size(32.dp))


                }
            }


        }
    }
}

public fun XW_ToastMessage(message:String,context: Context)
{
    Toast.makeText(context, message, Toast.LENGTH_SHORT,).show()
}

@Composable
public fun XW_InputDialog(
    showDialog: Boolean,
    title: String = "输入内容",
    placeholder: String = "请输入...",
    value : String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    if (showDialog) {
        // 用于记录输入框中的文本
        var inputText by remember { mutableStateOf(value) }

        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(text = title)
            },
            text = {
                Column {
                    Text(text = "请在下方输入：")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text(placeholder) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm(inputText) // 传回输入的数据
                        onDismiss()          // 关闭对话框
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("取消")
                }
            }
        )
    }
}

fun XW_LoadingMask(context: Context): Dialog {
    // 1. 创建一个干净的 Dialog，使用内置的无标题栏样式
    val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)

    // 2. 动态用代码创建一个全屏的居中进度条布局（省去写 XML 的麻烦）
    val rootLayout = RelativeLayout(context).apply {
        layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
    }

    // 创建原生圆形进度条（默认就是一直转的）
    val progressBar = ProgressBar(context).apply {
        isIndeterminate = true
    }

    // 将进度条居中放入布局
    val layoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        addRule(RelativeLayout.CENTER_IN_PARENT)
    }
    rootLayout.addView(progressBar, layoutParams)

    // 3. 将布局塞入 Dialog
    dialog.setContentView(rootLayout)

    // 4. 【核心控制：硬核防御，杜绝所有除了代码以外的退出方式】
    dialog.setCancelable(false)          // 禁用返回键
    dialog.setCanceledOnTouchOutside(false) // 禁用点击遮罩外部消失

    // 5. 配置遮罩的透明度与全屏属性
    dialog.window?.apply {
        // 启用暗化背景
        addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        // 设置暗化透明度：0.0f（完全透明）到 1.0f（完全全黑）。0.5f 是最正常的半透明
        attributes.dimAmount = 0.5f

        // 确保填满全屏
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    // 6. 显示遮罩
    dialog.show()

    // 返回这个实例，以便你在代码中手动控制它关闭
    return dialog
}

public fun XW_simpledialog(title: String,content:String,onConfirm: () -> Unit,onDismiss: () -> Unit,context : Context)
{
    val m3Context = ContextThemeWrapper(
        context,
        com.google.android.material.R.style.Theme_Material3_DayNight_Dialog
    )

    MaterialAlertDialogBuilder(m3Context)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton("确定") { dialog, which ->
            onConfirm()
        }
        .setNegativeButton("取消") { dialog, which ->
            onDismiss()
        }
        .setCancelable(false)
        .show()
}

@Composable
private fun RadioDialog(visible : Boolean,content : List<VersionConfig>,onDismiss: () -> Unit,onConfirm: (Int) -> Unit)
{
    if(visible)
    {
        var oncheckeditem by rememberSaveable { mutableStateOf(0) }

        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("选择版本", fontWeight = FontWeight.Bold) },
            text = { Column(
            ){
                for(i in 0 until content.count())
                {
                    Row(Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically)
                    {

                        if(i == oncheckeditem)
                        {
                            RadioButton(
                                selected = true,
                                onClick = {
                                    oncheckeditem = i
                                },

                                )

                        }
                        else{
                            RadioButton(
                                selected = false,
                                onClick = {
                                    oncheckeditem = i
                                },

                                )
                        }
                        Text(content[i].VersionName)
                        Box(modifier = Modifier
                            .background(Color(0xFFA9A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp)
                        )

                        {
                            Text(content[i].VersionVer)
                        }
                        Box(modifier = Modifier.background(Color(0xFF31A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp))
                        {
                            Text(content[i].VersionSize)
                        }
                    }
                }
            } },
            confirmButton = {
                TextButton(
                    onClick = {

                        onConfirm(oncheckeditem)
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("取消")
                }
            }
        )
    }
}




