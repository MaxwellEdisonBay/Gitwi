package com.mangofriends.mangoappnewest.presentation.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mangofriends.mangoappnewest.common.normalTouchableHeight
import com.mangofriends.mangoappnewest.common.primaryColor
import com.mangofriends.mangoappnewest.common.primaryTextColor
import com.mangofriends.mangoappnewest.common.smallSpace
import com.mangofriends.mangoappnewest.presentation.ui.theme.LightPink
import com.mangofriends.mangoappnewest.presentation.ui.theme.MediumGray
import com.mangofriends.mangoappnewest.presentation.ui.theme.Pink

@Composable
fun MngTextField(
    value: String,
    onValueChange: ((String) -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.medium,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
//        textColor = Color.Gray,
//        backgroundColor = Color.Black.copy(alpha = 0.12f),
    )

) {
    TextField(
        value,
        onValueChange,
        modifier,
        enabled,
        readOnly,
        textStyle,
        label,
        placeholder,
        leadingIcon,
        trailingIcon,
        isError,
        visualTransformation,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        interactionSource,
        shape,
        colors
    )
}


@Composable
fun MngButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Pink,
            contentColor = Color.White
            /* Other colors use values from MaterialTheme */
        ),
        onClick = onClick,
        modifier = modifier,
        content = content
    )
}

@Composable
fun MngSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }


) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Pink,
            uncheckedThumbColor = MediumGray,
            checkedTrackColor = LightPink,
            uncheckedTrackColor = Color.LightGray
        )
    )
}

@Preview
@Composable
fun TestNiceButton() {
    NiceButton(Icons.Filled.Favorite) { }
}

@Composable
fun NiceButton(
    icon: ImageVector,
    textColor: Color = primaryTextColor,
    backgroundColor: Color = primaryColor,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(smallSpace)
            .size(normalTouchableHeight),
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(normalTouchableHeight),
            shape = CircleShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = textColor,
                backgroundColor = backgroundColor
            ),
        ) {}
        Icon(
            icon, "Like",
            Modifier
                .align(Center)
                .size(20.dp), tint = Color.White
        )

    }

}