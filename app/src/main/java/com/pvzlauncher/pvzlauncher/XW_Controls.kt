package com.pvzlauncher.pvzlauncher

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Objects

@Composable
public fun XW_Switch(text : String,modifier: Modifier) {
    var checked by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(text,modifier = Modifier.padding(10.dp,5.dp), fontSize = 16.sp)

        Switch(
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}

@Composable
public fun XW_GameInformationCard(headImage : Painter,gametitle : String,gamedescription : String,args : Objects?)
{
    return Card(modifier = Modifier.padding(5.dp).fillMaxWidth())
    {
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth())
        {
            Row(modifier = Modifier.align(Alignment.CenterStart), verticalAlignment = Alignment.CenterVertically)
            {
                Image(painter = headImage,"", modifier = Modifier.padding(5.dp).size(64.dp))
                Column(modifier = Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text(gametitle,modifier = Modifier.padding(2.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(gamedescription,modifier = Modifier.padding(2.dp), fontSize = 14.sp)
                }
            }
            Button(onClick = {

            }, modifier = Modifier.align(Alignment.CenterEnd).padding(5.dp).size(48.dp),contentPadding = PaddingValues(0.dp),
                shape = CircleShape
            )
            {

                Icon(imageVector = Icons.Default.ArrowRightAlt,"检测更新", modifier = Modifier.size(32.dp))

            }


        }
    }
}