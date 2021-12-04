package com.mangofriends.mangoappnewest.presentation.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(

    h1 = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.W900,
        fontSize = 24.sp,
        color = LightPink
    ),

    body1 = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.W300,
        fontSize = 16.sp,
        color = MountBattenPink
    ),
    button = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
        color = Color.White
    )
    /* Other default text styles to override

    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
