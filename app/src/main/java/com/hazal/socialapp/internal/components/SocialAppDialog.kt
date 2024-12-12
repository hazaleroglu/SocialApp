package com.hazal.socialapp.internal.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialAppDialogBox(
    onDismissRequest: () -> Unit,
    content: SocialAppDialogBoxModel,
    positiveButtonClickAction: (() -> Unit)? = null,
    negativeButtonClickAction: (() -> Unit)? = null,
) {
    AlertDialog(onDismissRequest = { onDismissRequest.invoke() }, content = {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(content.mainColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.height(50.dp),
                        painter = rememberVectorPainter(image = Icons.Default.WarningAmber),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.Red)
                    )
                }
                Text(
                    text = content.title ?: "",
                    modifier = Modifier.padding(top = 20.dp),
                    fontSize = 18.sp,
                    color = content.mainColor
                )
                Text(
                    text = content.description ?: "",
                    modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(top = 20.dp, start = 45.dp, end = 45.dp),
                    horizontalArrangement = if (negativeButtonClickAction != null) Arrangement.SpaceBetween else Arrangement.Center
                ) {
                    if (content.negativeButtonText != null) {
                        Button(
                            modifier = Modifier
                                .width(100.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(width = 1.dp, color = content.mainColor),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            onClick = { negativeButtonClickAction?.invoke() }) {
                            Text(
                                text = content.negativeButtonText,
                                fontSize = 12.sp,
                                color = content.mainColor
                            )
                        }
                    }
                    Button(
                        modifier = Modifier.width(100.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = content.mainColor),
                        onClick = { positiveButtonClickAction?.invoke() }) {
                        Text(text = content.positiveButtonText ?: "", fontSize = 12.sp)
                    }
                }
            }
        }
    })
}

data class SocialAppDialogBoxModel(
    val mainColor: Color = Color.Red,
    val title: String? = "",
    val description: String? = "",
    val positiveButtonText: String? = null,
    val negativeButtonText: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SocialAppDialogBoxPreview() {
    SocialAppDialogBox(onDismissRequest = { }, content = SocialAppDialogBoxModel())
}