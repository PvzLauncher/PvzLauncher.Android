package com.pvzlauncher.pvzlauncher.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.align(Alignment.CenterStart).padding(5.dp,5.dp,64.dp,5.dp)) {
            Icon(imageVector = icon,"检测更新", modifier = Modifier.size(48.dp).padding(5.dp))
            Column(Modifier.padding(5.dp,5.dp,50.dp,5.dp))
            {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(2.dp))
                Text(desc, fontSize = 14.sp)
            }

        }
        OutlinedButton({
            OnClicked()
        },modifier = modifier.align(Alignment.CenterEnd)) {
            Text(buttontext)
        }
    }
}