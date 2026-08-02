package com.pvzlauncher.pvzlauncher.controls

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.utils.DownloadConfig
import com.pvzlauncher.pvzlauncher.utils.DownloadCount
import com.pvzlauncher.pvzlauncher.utils.GameConfig

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
                        Box(modifier = Modifier.padding(2.5.dp)) { }
                        Row()
                        {
                            if(args.supportVersion.SupportSystem <= Build.VERSION.SDK_INT)
                            {
                                outer@for(i in args.supportVersion.SupportPlatform)
                                {
                                    for(j in Build.SUPPORTED_ABIS)
                                    {
                                        if(i == j)
                                        {
                                            Box(modifier = Modifier
                                                .background(Color(0xff2b30b4), RoundedCornerShape(7.5.dp))
                                                .padding(4.dp,2.dp)
                                            )

                                            {

                                                Text("兼容", color = Color.White, fontSize = 14.sp)

                                            }
                                            break@outer
                                        }
                                        if(args.supportVersion.SupportPlatform.indexOf(i) == args.supportVersion.SupportPlatform.count() - 1 && Build.SUPPORTED_ABIS.indexOf(j) == Build.SUPPORTED_ABIS.count() - 1 && i != j)
                                        {
                                            Box(modifier = Modifier
                                                .background(Color(0xff3c3c3c), RoundedCornerShape(7.5.dp))
                                                .padding(4.dp,2.dp)
                                            )

                                            {

                                                Text("可能不兼容", color = Color.White, fontSize = 14.sp)

                                            }
                                        }
                                    }
                                }

                            }
                            else
                            {
                                Box(modifier = Modifier
                                    .background(Color(0xff3c3c3c), RoundedCornerShape(7.5.dp))
                                    .padding(4.dp,2.dp)
                                )

                                {

                                    Text("不兼容", color = Color.White, fontSize = 14.sp)

                                }
                            }
                            Box(modifier = Modifier.padding(2.5.dp)) { }
                            if(args.recommend)
                            {
                                Box(modifier = Modifier
                                    .background(Color(0xff3fbc2b), RoundedCornerShape(7.5.dp))
                                    .padding(4.dp,2.dp)
                                )

                                {

                                    Text("推荐", color = Color.White, fontSize = 14.sp)

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