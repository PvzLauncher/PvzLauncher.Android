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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pvzlauncher.pvzlauncher.AppDestinations
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
public fun XW_GameInformationCard(args : GameConfig,currentDestination : AppDestinations)
{
    return Card(modifier = Modifier.padding(5.dp).fillMaxWidth())
    {
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth())
        {
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(10.dp), verticalAlignment = Alignment.CenterVertically)
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
                            Text(args.GameVersion)
                        }
                        Box(modifier = Modifier.padding(2.5.dp)) { }
                        Box(modifier = Modifier.background(Color(0xFF31A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp))
                        {
                            Text(args.GameSize)
                        }
                    }
                }
            }
            Button(onClick = {
                DownloadConfig = args
                currentDestination = AppDestinations.DownloadDetailPage
                currentDestination
            }, modifier = Modifier.align(Alignment.CenterEnd).padding(5.dp).size(48.dp),contentPadding = PaddingValues(0.dp),
                shape = CircleShape
            )
            {

                Icon(imageVector = Icons.Default.ArrowRightAlt,"检测更新", modifier = Modifier.size(32.dp))

            }


        }
    }
}

@Composable
public fun XW_ManageInformationCard(headImage : String,gametitle : String,gameversion : String,gamesize : String,args : Objects?,)
{
    return Card(modifier = Modifier.padding(5.dp).fillMaxWidth())
    {
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth())
        {
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(10.dp), verticalAlignment = Alignment.CenterVertically)
            {
                val cont = LocalContext.current
                AsyncImage(model = headImage,"", modifier = Modifier.padding(10.dp,5.dp).size(48.dp),placeholder = painterResource(
                    R.drawable.ic_unknown), error = painterResource(R.drawable.ic_unknown),onError = { state -> XW_ToastMessage("获取头图时发生错误：${state.result.throwable.message}",cont)})
                Column()
                {
                    Text(gametitle,modifier = Modifier.padding(2.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Row(modifier = Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically)
                    {
                        Box(modifier = Modifier
                            .background(Color(0xFFA9A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp)
                        )

                        {
                            Text(gameversion)
                        }
                        Box(modifier = Modifier.padding(2.5.dp)) { }
                        Box(modifier = Modifier.background(Color(0xFF31A9A9), RoundedCornerShape(7.5.dp))
                            .padding(4.dp,2.dp))
                        {
                            Text(gamesize)
                        }
                    }
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

public fun XW_ToastMessage(message:String,context: Context) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT,).show()


}