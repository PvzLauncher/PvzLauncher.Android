package com.pvzlauncher.pvzlauncher.controls

import android.content.res.Configuration
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pvzlauncher.pvzlauncher.R
import com.pvzlauncher.pvzlauncher.utils.DefaultGameConfig
import com.pvzlauncher.pvzlauncher.utils.DefaultLauncherConfig
import com.pvzlauncher.pvzlauncher.utils.DefaultSaveConfig
import com.pvzlauncher.pvzlauncher.utils.GameListConfig
import com.pvzlauncher.pvzlauncher.utils.GetWebSiteContent
import com.pvzlauncher.pvzlauncher.utils.LAUNCHERCONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.LauncherConfig
import com.pvzlauncher.pvzlauncher.utils.ReadJson
import com.pvzlauncher.pvzlauncher.utils.ReadJsonfromText
import com.pvzlauncher.pvzlauncher.utils.SAVECONFIGNAME
import com.pvzlauncher.pvzlauncher.utils.SaveConfig
import com.pvzlauncher.pvzlauncher.utils.SaveConfigList
import com.pvzlauncher.pvzlauncher.utils.globalContext
import java.io.File

@Composable
public fun XW_ManageInformationCard(args : SaveConfig, onBack: () -> Unit, IsButtonEnable :  Boolean,icon : ImageVector = Icons.Default.ArrowForward,canLongPress : Boolean = false)
{
    var rj by remember { mutableStateOf(DefaultSaveConfig()) }
    var lj by remember { mutableStateOf(DefaultLauncherConfig()) }
    var gindex by remember { mutableStateOf(DefaultGameConfig()) }
    LaunchedEffect(Unit) {
        rj = ReadJson<SaveConfigList>(File("${globalContext.filesDir}/${SAVECONFIGNAME}"))
        lj = ReadJson<LauncherConfig>(File("${globalContext.filesDir}/${LAUNCHERCONFIGNAME}"))
        gindex = ReadJsonfromText(GetWebSiteContent("https://raw.giteeusercontent.com/Wang120229/PvzLauncher.Service.Android/raw/main/GameIndex.json"))
    }
    return OutlinedCard(modifier = Modifier.padding(5.dp).fillMaxWidth())
    {
        Box(modifier = Modifier.padding(5.dp).fillMaxWidth())
        {
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(10.dp,10.dp,64.dp,10.dp), verticalAlignment = Alignment.CenterVertically)
            {
                val cont = LocalContext.current

                AsyncImage(model = args.headImage,"", modifier = Modifier.padding(10.dp,5.dp).size(48.dp),placeholder = painterResource(R.drawable.ic_unknown), error = painterResource(R.drawable.ic_unknown))
                val orientation = LocalConfiguration.current.orientation

                when (orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        Column()
                        {
                            Text(args.GameName,modifier = Modifier.padding(2.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Row(modifier = Modifier.padding(2.dp))
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


                                for(ij in 0 until rj.ListIndex.count())
                                {
                                    if(ij == lj.CurrentGameIndex.ListIndex)
                                    {
                                        if(rj.ListIndex[ij].GameIndex.indexOf(args) == lj.CurrentGameIndex.GameIndex)
                                        {
                                            Box(modifier = Modifier.background(Color.Red, RoundedCornerShape(7.5.dp))
                                                .padding(4.dp,2.dp))
                                            {
                                                Text("活动", color = Color.White, fontSize = 14.sp)
                                            }
                                        }
                                    }


                                }

                                Box(Modifier.padding(2.dp)){}



                                for(i in gindex.ListIndex)
                                {
                                    for(j in i.GameIndex)
                                    {
                                        for(k in j.GameLink)
                                        {
                                            if(args.PackageName == k.VersionPackageName && k.VersionNeedUpdate)
                                            {
                                                if(args.gameversion != k.VersionVer)
                                                {
                                                    Box(modifier = Modifier.background(Color(0xFF18cb2b), RoundedCornerShape(7.5.dp))
                                                        .padding(4.dp,2.dp))
                                                    {
                                                        Text("更新可用", color = Color.White, fontSize = 14.sp)
                                                    }
                                                }


                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Configuration.ORIENTATION_PORTRAIT -> {
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


                                for(ij in 0 until rj.ListIndex.count())
                                {
                                    if(ij == lj.CurrentGameIndex.ListIndex)
                                    {
                                        if(rj.ListIndex[ij].GameIndex.indexOf(args) == lj.CurrentGameIndex.GameIndex)
                                        {
                                            Box(modifier = Modifier.background(Color.Red, RoundedCornerShape(7.5.dp))
                                                .padding(4.dp,2.dp))
                                            {
                                                Text("活动", color = Color.White, fontSize = 14.sp)
                                            }
                                        }
                                    }


                                }

                                Box(Modifier.padding(2.dp)){}



                                for(i in gindex.ListIndex)
                                {
                                    for(j in i.GameIndex)
                                    {
                                        for(k in j.GameLink)
                                        {
                                            if(args.PackageName == k.VersionPackageName && k.VersionNeedUpdate)
                                            {
                                                if(args.gameversion != k.VersionVer)
                                                {
                                                    Box(modifier = Modifier.background(Color(0xFF18cb2b), RoundedCornerShape(7.5.dp))
                                                        .padding(4.dp,2.dp))
                                                    {
                                                        Text("更新可用", color = Color.White, fontSize = 14.sp)
                                                    }
                                                }


                                            }
                                        }
                                    }
                                }
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

                    Icon(imageVector = icon,"检测更新", modifier = Modifier.size(32.dp))


                }
            }
            if(args.like && icon != Icons.Default.Star)
            {
                Icon(imageVector = Icons.Default.Star,"检测更新", modifier = Modifier.size(18.dp).align(Alignment.TopEnd))
            }

        }
    }
}