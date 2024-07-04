package com.example.triangle

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import com.example.triangle.ui.theme.TriangleTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

val tag = "TriangleRotation"
@Composable
fun TriangleRotation(orientationZ: MutableState<Double>) {
    var rotationAngle by remember { orientationZ }

    BoxWithConstraints {
        Canvas(
            modifier = Modifier
                .size(maxWidth)
        ){
            val xCenter = size.width/2
            val yCenter = size.height/2

            val center = Offset(xCenter, yCenter)

            drawCircle(
                color = Color.Red,
                center = center,
                radius = 3.dp.toPx()
            )

            val p1 = Offset(center.x, center.y*.5f)
            val p2 = Offset(center.x*1.5f, center.y*1.5f)
            val p3 = Offset(center.x*.5f, center.y*1.5f)

            val vertices = listOf(
                p1,
                p2,
                p3
            )

            Log.d(tag, "$rotationAngle")
            drawTriangle(rotateVertices(vertices, center, -rotationAngle))
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun TriangleRotationPreview() {
    var _orientationZ = mutableDoubleStateOf(.0)
    TriangleTheme {
        TriangleRotation(_orientationZ)
    }
}

fun rotateVertices(vertices: List<Offset>, center: Offset, rARadians: Double): List<Offset>{
    return vertices.map { vertex ->
        val dx = vertex.x-center.x
        val dy = vertex.y-center.y
        val rotatedX = dx * cos(rARadians) - dy * sin(rARadians)
        val rotatedY = dx * sin(rARadians) + dy * cos(rARadians)
        Offset(rotatedX.toFloat() + center.x, rotatedY.toFloat() + center.y)
    }
}

fun DrawScope.drawTriangle(vertices: List<Offset>){
    drawLine(
        start = vertices[0],
        end = vertices[1],
        color = Color.Black,
        strokeWidth = 4f
    )
    drawLine(
        start = vertices[1],
        end = vertices[2],
        color = Color.Black,
        strokeWidth = 4f
    )
    drawLine(
        start = vertices[2],
        end = vertices[0],
        color = Color.Black,
        strokeWidth = 4f
    )
}