package com.pvzlauncher.pvzlauncher.utils

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.CircularProgressIndicator
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowForward
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.platform.ComposeView


@Composable
public fun XW_Switch(icon:ImageVector,title : String,desc :String,modifier: Modifier,isEnabled : Boolean,OnChanged : (isChecked : Boolean) -> Unit) {
    var checked by remember { mutableStateOf(isEnabled) }
    Box(Modifier.fillMaxWidth())
    {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.align(Alignment.CenterStart).padding(5.dp,5.dp,64.dp,5.dp)) {
            Icon(imageVector = icon,"检测更新", modifier = Modifier.size(48.dp).padding(5.dp))
            Column(Modifier.padding(5.dp))
            {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                Text(desc, fontSize = 14.sp)
            }

        }
        Switch(
            modifier = modifier.align(Alignment.CenterEnd),
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
    return OutlinedCard(modifier = Modifier.padding(5.dp).fillMaxWidth())
    {
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth())
        {
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(10.dp,10.dp,64.dp,10.dp), verticalAlignment = Alignment.CenterVertically)
            {
                val cont = LocalContext.current
                AsyncImage(model = args.GameImage,"", modifier = Modifier.padding(10.dp,5.dp).size(48.dp),placeholder = painterResource(
                    R.drawable.ic_unknown), error = painterResource(R.drawable.ic_unknown))
                Column()
                {
                    Text(args.GameName,modifier = Modifier.padding(2.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Column(modifier = Modifier.padding(2.dp))
                    {
                        Box(modifier = Modifier
                            .background(Color(0xFFA9A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp)
                        )

                        {
                            //Text(args.GameVersion)
                            var i = ""
                            for(j in 0 until args.GameLink.count())
                            {
                                if(j != args.GameLink.count()-1)
                                {
                                    i += "${args.GameLink[j].VersionVer}/"
                                }
                                else
                                {
                                    i += "${args.GameLink[j].VersionVer}"
                                }
                            }
                                        Text(i, color = Color.White, fontSize = 14.sp)

                        }
                        Box(modifier = Modifier.padding(2.5.dp)) { }
                        Box(modifier = Modifier.background(Color(0xFF31A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp))
                        {


                            var i = ""
                            for(j in 0 until args.GameLink.count())
                            {
                                if(j != args.GameLink.count()-1)
                                {
                                    i += "${args.GameLink[j].VersionSize}/"
                                }
                                else
                                {
                                    i += "${args.GameLink[j].VersionSize}"
                                }
                            }
                            Text(i, color = Color.White, fontSize = 14.sp)

                        }
                    }
                }
            }
            var isvisible by rememberSaveable { mutableStateOf(false) }
            RadioDialog(isvisible,args.GameLink,{isvisible = false},{ i -> DownloadCount = i
                isvisible = false
                DownloadConfig = args

                onBack()})
            TextButton(onClick = {
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

    return OutlinedCard(modifier = Modifier.padding(5.dp).fillMaxWidth())
    {
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth())
        {
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(10.dp,10.dp,64.dp,10.dp), verticalAlignment = Alignment.CenterVertically)
            {
                val cont = LocalContext.current

                AsyncImage(model = args.headImage,"", modifier = Modifier.padding(10.dp,5.dp).size(48.dp),placeholder = painterResource(R.drawable.ic_unknown), error = painterResource(R.drawable.ic_unknown))

                Column()
                {
                    Text(args.GameName,modifier = Modifier.padding(2.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Column(modifier = Modifier.padding(2.dp))
                    {
                        Box(modifier = Modifier
                            .background(Color(0xFFA9A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp)
                        )

                        {
                            Text(args.gameversion, color = Color.White, fontSize = 14.sp)
                        }
                        Box(Modifier.padding(2.dp)){}
//
                        val rj =  ReadJson<SaveConfigList>(File("${cont.filesDir}/${SAVECONFIGNAME}").readText()).GameIndex
                        if(rj.indexOf(args) == ReadJson<LauncherConfig>(File("${cont.filesDir}/${LAUNCHERCONFIGNAME}").readText()).CurrentGameIndex)
                        {
                            Box(modifier = Modifier.background(Color.Red, RoundedCornerShape(7.5.dp))
                                .padding(4.dp,2.dp))
                            {
                                Text("活动", color = Color.White, fontSize = 14.sp)
                            }
                        }


                    }
                }
            }



            if(IsButtonEnable)
            {
                TextButton(onClick = {
                    onBack()
                }, modifier = Modifier.align(Alignment.CenterEnd).padding(5.dp).size(48.dp),contentPadding = PaddingValues(0.dp),
                    shape = CircleShape
                )
                {

                    Icon(imageVector = Icons.Default.ArrowForward,"检测更新", modifier = Modifier.size(32.dp))


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

fun XW_LoadingMask(context: Context, loadingText: String): Dialog {
    val dialog = Dialog(context).apply {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    val composeView = ComposeView(context).apply {
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFF303030)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = androidx.compose.ui.graphics.Color.White,
                            strokeWidth = 4.dp
                        )

                        Text(
                            text = loadingText,
                            color = androidx.compose.ui.graphics.Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }

    if (context is ComponentActivity) {
        composeView.setViewTreeLifecycleOwner(context)
        composeView.setViewTreeViewModelStoreOwner(context)
        composeView.setViewTreeSavedStateRegistryOwner(context)
    }

    dialog.setContentView(composeView)

    dialog.window?.apply {
        setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    return dialog
}

public fun XW_simpledialog(title: String,content:String,onConfirm: () -> Unit,onDismiss: () -> Unit,context : Context)
{
    val m3BaseThemeRes = com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar
    val baseContext = ContextThemeWrapper(context, m3BaseThemeRes)
    val m3Context = ContextThemeWrapper(baseContext, R.style.XW_DialogTheme)

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
                        Row(Modifier.padding(2.dp)){}
                        Box(modifier = Modifier
                            .background(Color(0xFFA9A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp)
                        )

                        {
                            Text(content[i].VersionVer,color = Color.White)
                        }
                        Row(Modifier.padding(2.dp)){}
                        Box(modifier = Modifier.background(Color(0xFF31A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp))
                        {
                            Text(content[i].VersionSize,color = Color.White)
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




