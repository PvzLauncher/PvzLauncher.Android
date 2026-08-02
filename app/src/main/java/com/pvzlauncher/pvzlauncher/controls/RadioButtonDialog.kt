package com.pvzlauncher.pvzlauncher.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pvzlauncher.pvzlauncher.utils.VersionConfig

@Composable
public fun RadioDialog(visible : Boolean,content : List<VersionConfig>,onDismiss: () -> Unit,onConfirm: (Int) -> Unit)
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


                            RadioButton(
                                selected = i == oncheckeditem,
                                onClick = {
                                    oncheckeditem = i
                                },

                                )


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

@Composable
public fun RadioDialog(visible : Boolean,title : String,content : String,items : List<String>,onDismiss: () -> Unit,onConfirm: (Int) -> Unit)
{
    if(visible)
    {
        var oncheckeditem by rememberSaveable { mutableStateOf(0) }

        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title, fontWeight = FontWeight.Bold) },
            text = { val scrollState = rememberScrollState()
                Column(Modifier.horizontalScroll(scrollState)
                ){
                Text(content, fontWeight = FontWeight.Bold)
                for(i in 0 until items.count())
                {
                    Row(Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically)
                    {



                            if(items[i] != "默认收藏夹" || content == "请选择要移入的收藏夹" )
                            {
                                RadioButton(
                                    selected = i == oncheckeditem,
                                    onClick = {
                                        oncheckeditem = i
                                    },

                                    )
                                Text(items[i])
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