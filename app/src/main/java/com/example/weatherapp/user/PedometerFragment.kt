package com.example.weatherapp.user

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.weatherapp.databinding.FragmentPedometerBinding
import com.example.weatherapp.network.UserObject
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Response

class PedometerFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentPedometerBinding? = null
    private val binding get() = _binding!!

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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPedometerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorManager = ContextCompat.getSystemService(requireContext(), SensorManager::class.java) as SensorManager
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        checkPermissionAndRegisterSensor()

        binding.resetButton.setOnClickListener {
            currentSteps = 0
            updateUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermissionAndRegisterSensor() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        sensorManager.unregisterListener(this)
        _binding = null
    }

    private fun saveStepCount() {
        var auth = FirebaseAuth.getInstance()
        val call = UserObject.getRetrofitService().saveStepCount(auth.currentUser?.email!!.toString(),2)
        //여기다가 stepCount 넣어주시면 됩니다.

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<Void> {
            // 응답 성공 시
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }
}