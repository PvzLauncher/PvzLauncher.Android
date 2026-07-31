package com.pvzlauncher.pvzlauncher.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
public fun XW_CheckBoxDialog(showDialog : Boolean,title : String,content : String,require : String,onDismiss: () -> Unit,onConfirm: (Boolean) -> Unit)
{
    if(showDialog)
    {
        var onSelect by rememberSaveable { mutableStateOf(false) }

        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Column(
            ){
                Text(content,Modifier.padding(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically)
                {
                    Checkbox(onSelect,{ i -> onSelect = i })
                    Text(require)
                }
            } },
            confirmButton = {
                TextButton(
                    onClick = {

                        onConfirm(onSelect)
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