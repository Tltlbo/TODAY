package com.example.weatherapp

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.ModelUser
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.detailuv.DetailuvActivity
import com.example.weatherapp.detailweather.WeatherListActivity
import com.example.weatherapp.user.ModifyInfomationActivity
import com.example.weatherapp.user.UserInformationActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    lateinit var viewModel : MainViewModel
    lateinit var binding : ActivityMainBinding

    private val fl: FrameLayout by lazy {
        findViewById(R.id.fl_)
    }

    private val bn: BottomNavigationView by lazy {
        findViewById(R.id.bn_)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar)
        setContentView(binding.root)

        val app = application as MyApplication

        viewModel = app.mainViewModel
        viewModel.loadlocationInfo()
        viewModel.getUserInfo()

        Log.e("user", viewModel.User.userName)
        binding.btnGotoUserInfo.setOnClickListener {
            val intent = Intent(this@MainActivity, UserInformationActivity::class.java)
            intent.putExtra("user", viewModel.User)
            startActivity(intent)
        }



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
                    R.id.menu_UV -> UVFragment()
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