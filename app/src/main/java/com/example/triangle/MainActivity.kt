package com.example.triangle

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import com.example.triangle.ui.theme.TriangleTheme
import java.util.Arrays

class MainActivity : ComponentActivity(), SensorEventListener {

    private val tag = "MainActivity"

    private lateinit var sensorManager: SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var _orientationZ = mutableDoubleStateOf(.0)
    private val orientationZ: State<Double> = _orientationZ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriangleTheme {
                TriangleRotation(_orientationZ)
            }
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Thread {
            while (true) {
                try {
                    // Thread.sleep(500)
                    updateOrientationAngles()
                    Log.d("SensorData", "Accelerometer Reading: ${accelerometerReading.contentToString()}")
                    Log.d("SensorData", "Magnetometer Reading: ${magnetometerReading.contentToString()}")
                    Log.d("SensorData", "Rotation Matrix: ${rotationMatrix.contentToString()}")
                    Log.d("SensorData", "Orientation Angles: ${orientationAngles.contentToString()}")
                    Log.d("SensorData", "Orientation Angles Z: ${Math.toDegrees(orientationAngles[0].toDouble())}")
                    Log.d("SensorData", "Orientation Angles X: ${Math.toDegrees(orientationAngles[1].toDouble())}")
                    Log.d("SensorData", "Orientation Angles Y: ${Math.toDegrees(orientationAngles[2].toDouble())}")
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        _orientationZ.value = orientationAngles[0].toDouble()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

