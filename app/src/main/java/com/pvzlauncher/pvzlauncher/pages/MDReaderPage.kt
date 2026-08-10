package com.pvzlauncher.pvzlauncher.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvzlauncher.pvzlauncher.AppDestinations
import com.pvzlauncher.pvzlauncher.utils.CurrentDestination
import com.pvzlauncher.pvzlauncher.utils.GetWebSiteContent
import com.pvzlauncher.pvzlauncher.utils.MDR_FileName
import com.pvzlauncher.pvzlauncher.utils.MDR_MDContent
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MDReaderPage()
{
    var content by remember { mutableStateOf("正在获取信息……") }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            content = GetWebSiteContent(MDR_MDContent)
        }
    }
    TopAppBar(
        title = {
            Text(
                text = MDR_FileName,
                fontWeight = Bold,
                modifier = Modifier.padding(5.dp),
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            TextButton(
                onClick = {
                    CurrentDestination = AppDestinations.AboutPage
                },
                modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp)
                    .background(Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape

            )
            {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    "返回",
                    modifier = Modifier.size(32.dp)
                )

            }
        }
    )
    MarkdownText(
        content.trimIndent(), modifier = Modifier
            .padding(10.dp, 90.dp, 10.dp, 10.dp)
            .fillMaxSize()
    )
}