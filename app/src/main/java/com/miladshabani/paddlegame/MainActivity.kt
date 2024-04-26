package com.miladshabani.paddlegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.miladshabani.paddlegame.ui.theme.PaddleGameTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaddleGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen()
                }
            }
        }
    }
}

@Composable
fun GameScreen() {

    Column {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val maxWidth = with(LocalDensity.current) { constraints.maxWidth.toDp().toPx() }
            val maxHeight = with(LocalDensity.current) { constraints.maxHeight.toDp().toPx() }

            var position by remember { mutableStateOf(Offset(maxWidth / 2, maxHeight / 2)) }
            val radius = with(LocalDensity.current) { 24.dp.toPx() }
            val velocity = remember { mutableStateOf(Offset(10f, 10f)) }

            LaunchedEffect(key1 = true) {
                while (true) {
                    delay(16L)  // Approximates 60 FPS
                    position += velocity.value
                    if (position.x !in radius..maxWidth - radius || position.y !in radius..maxHeight - radius) {
                        if (position.x !in radius..maxWidth - radius) {
                            velocity.value = velocity.value.copy(x = -velocity.value.x)
                        }
                        if (position.y !in radius..maxHeight - radius) {
                            velocity.value = velocity.value.copy(y = -velocity.value.y)
                        }
                    }
                }
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.Red,
                    radius = radius,
                    center = position
                )
            }
        }
        var offset by remember { mutableStateOf(Offset.Zero) }
        Paddle(
            modifier = Modifier
                .width(300.dp)
                .pointerInput(Unit) {
                    detectDragGestures(onDrag = { change, dragAmount ->
                        change.consume()
                        offset += Offset(dragAmount.x, 0f)
                    })
                }
                .offset { IntOffset(offset.x.roundToInt(), 0) }
        )
    }

}


@Composable
fun Paddle(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .height(24.dp)
            .defaultMinSize(minWidth = 300.dp)
            .then(modifier)
    ) {
        drawRoundRect(
            color = Color.Green,
            topLeft = Offset(0f, 0f),
            size = size,
            cornerRadius = CornerRadius(x = 100f, y = 100f)
        )
    }
}

@Preview
@Composable
private fun PaddlePreview() {
    MaterialTheme {
        Box {
            Paddle(modifier = Modifier.fillMaxSize())
        }
    }
}