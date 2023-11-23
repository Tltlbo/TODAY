package com.example.weatherapp.user

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.weatherapp.MyApplication
import com.example.weatherapp.databinding.ActivityPedometerBinding
import com.example.weatherapp.network.UserObject
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Response

class PedometerActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityPedometerBinding

    private lateinit var sensorManager: SensorManager
    private var stepCountSensor: Sensor? = null
    private var currentSteps = 0

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            registerSensor()
        } else {
            // 권한 거부 처리
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedometerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        sensorManager = ContextCompat.getSystemService(this, SensorManager::class.java) as SensorManager
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        checkPermissionAndRegisterSensor()

        binding.resetButton.setOnClickListener {
            currentSteps = 0
            updateUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermissionAndRegisterSensor() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
            registerSensor()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    private fun registerSensor() {
        stepCountSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        } ?: run {
            // 센서가 없는 경우 처리
        }
    }

    private fun updateUI() {
        binding.stepCountView.text = currentSteps.toString()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            currentSteps++
            updateUI()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // 센서 정확도 변경 처리 (선택적)
    }

    override fun onDestroy() {
        super.onDestroy()
        saveStepCount()
        sensorManager.unregisterListener(this)
    }

    private fun saveStepCount() {
        var auth = FirebaseAuth.getInstance()
        val call = UserObject.getRetrofitService().saveStepCount(auth.currentUser?.email!!.toString(), currentSteps)
        val app = application as MyApplication

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<Void> {
            // 응답 성공 시
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    if(app.userInfoflag) {
                        val userinfoviewmodel = app.userInformationViewModel
                        userinfoviewmodel.User.stepCount = currentSteps
                    }
                    val mainViewModel = app.mainViewModel
                    mainViewModel.User.stepCount = currentSteps
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }
}