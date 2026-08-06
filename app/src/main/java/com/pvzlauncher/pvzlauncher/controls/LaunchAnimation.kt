package com.pvzlauncher.pvzlauncher.controls

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch


@Composable
fun LaunchAnimation(
    onFinished: () -> Unit
) {

    val circleAlpha = remember {
        Animatable(0f)
    }


    val circle1Scale = remember {
        Animatable(1.8f)
    }

    val circle2Scale = remember {
        Animatable(2.2f)
    }

    val circle3Scale = remember {
        Animatable(2.6f)
    }


    val logoScale = remember {
        Animatable(1.5f)
    }

    val logoAlpha = remember {
        Animatable(0f)
    }



    LaunchedEffect(Unit) {


        // ======================
        // 圆 + Logo 同时进入
        // ======================

        coroutineScope {

            listOf(

                launch {
                    circleAlpha.animateTo(
                        0.35f,
                        tween(600)
                    )
                },


                launch {
                    circle1Scale.animateTo(
                        1f,
                        tween(
                            1000,
                            easing = EaseOutQuart
                        )
                    )
                },


                launch {
                    circle2Scale.animateTo(
                        1f,
                        tween(
                            1000,
                            easing = EaseOutQuart
                        )
                    )
                },


                launch {
                    circle3Scale.animateTo(
                        1f,
                        tween(
                            1000,
                            easing = EaseOutQuart
                        )
                    )
                },


                launch {
                    logoScale.animateTo(
                        1f,
                        tween(
                            800,
                            easing = EaseOutBack
                        )
                    )
                },


                launch {
                    logoAlpha.animateTo(
                        1f,
                        tween(800)
                    )
                }

            ).joinAll()

        }



        //停留

        delay(1200)



        // ======================
        // 整体退出
        // ======================

        coroutineScope {

            listOf(

                launch {
                    circleAlpha.animateTo(
                        0f,
                        tween(400)
                    )
                },


                launch {
                    circle1Scale.animateTo(
                        3f,
                        tween(
                            800,
                            easing = EaseOutQuart
                        )
                    )
                },


                launch {
                    circle2Scale.animateTo(
                        3f,
                        tween(
                            800,
                            easing = EaseOutQuart
                        )
                    )
                },


                launch {
                    circle3Scale.animateTo(
                        3f,
                        tween(
                            800,
                            easing = EaseOutQuart
                        )
                    )
                },


                launch {
                    logoScale.animateTo(
                        3f,
                        tween(
                            800,
                            easing = EaseOutQuart
                        )
                    )
                },


                launch {
                    logoAlpha.animateTo(
                        0f,
                        tween(400)
                    )
                }

            ).joinAll()

        }


        onFinished()

    }



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {


        // 外圆

        Box(
            modifier = Modifier
                .size(400.dp)
                .graphicsLayer {
                    scaleX = circle1Scale.value
                    scaleY = circle1Scale.value
                    alpha = circleAlpha.value
                }
                .background(
                    Color(0x660064FF),
                    CircleShape
                )
        )



        // 中圆

        Box(
            modifier = Modifier
                .size(350.dp)
                .graphicsLayer {
                    scaleX = circle2Scale.value
                    scaleY = circle2Scale.value
                    alpha = circleAlpha.value
                }
                .background(
                    Color(0x550064FF),
                    CircleShape
                )
        )



        // 内圆

        Box(
            modifier = Modifier
                .size(250.dp)
                .graphicsLayer {
                    scaleX = circle3Scale.value
                    scaleY = circle3Scale.value
                    alpha = circleAlpha.value
                }
                .background(
                    Color(0x440064FF),
                    CircleShape
                )
        )



        // Logo

        Icon(
            imageVector = Icons.Default.RocketLaunch,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {

                    scaleX = logoScale.value
                    scaleY = logoScale.value
                    alpha = logoAlpha.value

                }
        )

    }
}