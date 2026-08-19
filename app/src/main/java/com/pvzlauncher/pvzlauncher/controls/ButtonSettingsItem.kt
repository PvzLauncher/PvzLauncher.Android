package com.pvzlauncher.pvzlauncher.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
public fun XW_Button(icon:ImageVector,title : String,desc :String,buttontext : String,modifier: Modifier,OnClicked : () -> Unit) {
    Box(Modifier.fillMaxWidth())
    {
        ListItem(
            headlineContent = {Text(title, fontWeight = FontWeight.Bold)},
            supportingContent = {Text(desc)},
            leadingContent = {Icon(imageVector = icon,"检测更新")},
            modifier = Modifier.clickable {
                OnClicked()
            }
        )
    }
}