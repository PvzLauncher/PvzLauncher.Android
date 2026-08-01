package com.pvzlauncher.pvzlauncher.controls

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
public fun XW_InputDialog(
    showDialog: Boolean,
    title: String,
    content : String,
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
                val scrollState = rememberScrollState()
                Column(Modifier.horizontalScroll(scrollState)
                ) {
                    Text(content)


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
                        if(inputText != "")
                        {
                            onConfirm(inputText)
                            onDismiss()
                        }
                        else
                        {
                            XW_ToastMessage("请输入内容！")
                        }
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