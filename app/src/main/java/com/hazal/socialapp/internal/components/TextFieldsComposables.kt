package com.hazal.socialapp.internal.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    value: String,
    shape: RoundedCornerShape,
    onNewValue: (String) -> Unit,
    isVisible: Boolean = false,
    visibilityClick: () -> Unit
) {
    val icon =
        if (isVisible) rememberVectorPainter(Icons.Default.Visibility)
        else rememberVectorPainter(Icons.Outlined.VisibilityOff)

    val visualTransform =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        shape = shape,
        value = value,
        onValueChange = onNewValue,
        trailingIcon = {
            IconButton(onClick = visibilityClick) {
                Icon(painter = icon, "Visibility", tint = Color.Gray)
            }
        },
        leadingIcon = { Icon(Icons.Default.Lock, "Lock", tint = Color.Gray) },
        maxLines = 1,
        visualTransformation = visualTransform
    )
}

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape,
    value: String,
    onNewValue: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        shape = shape,
        value = value,
        onValueChange = onNewValue,
        leadingIcon = { Icon(Icons.Default.Email, "Email", tint = Color.Gray) },
        maxLines = 1
    )
}