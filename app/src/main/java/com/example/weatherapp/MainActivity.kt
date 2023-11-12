package com.example.weatherapp

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    lateinit var viewModel : MainViewModel

    private val fl: FrameLayout by lazy {
        findViewById(R.id.fl_)
    }

    private val bn: BottomNavigationView by lazy {
        findViewById(R.id.bn_)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app = application as MyApplication

        viewModel = app.mainViewModel


        supportFragmentManager.beginTransaction().add(fl.id, HomeFragment()).commit()



        val permissionList = arrayOf<String>(
            // 위치 권한
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        // 권한 요청
        ActivityCompat.requestPermissions(this@MainActivity, permissionList, 1)



        // 문제점 날씨 정보를 보여주는 Fragment에서 탭 변경 시 다시 API를 요청하고 그림, 유지 할 수는 없나?
        bn.setOnNavigationItemSelectedListener {
            replaceFragment(
                when (it.itemId) {
                    R.id.menu_home -> HomeFragment()
                    R.id.menu_finedust -> FineDustFragment()
                    else -> RecommendFragment()
                }
            )
            true
        }
    }



    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(fl.id, fragment).commit()
    }
}