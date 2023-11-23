package com.example.weatherapp.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.data.ModelUser
import com.example.weatherapp.databinding.ActivityModifyInformationBinding
import com.example.weatherapp.databinding.ActivityUserInformationBinding
import com.google.firebase.auth.FirebaseAuth

class UserInformationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityUserInformationBinding
    private lateinit var viewModel : UserInformationViewModel
    private lateinit var mainviewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInformationBinding.inflate(layoutInflater)
        val app = application as MyApplication
        viewModel = app.userInformationViewModel
        mainviewModel = app.mainViewModel
        setContentView(binding.root)



        // Firebase 인증 및 Firestore 초기화
        auth = FirebaseAuth.getInstance()
        viewModel.User = mainviewModel.User

        Log.e("onCreateUserInfo", viewModel.User.userName)
        binding.goToEditInfo.setOnClickListener {
            val intent = Intent(this@UserInformationActivity, ModifyInfomationActivity::class.java)
            intent.putExtra("user", viewModel.User)
            startActivity(intent)
        }
        displayUserInfo()

    }

    override fun onResume() {
        super.onResume()
        displayUserInfo()
    }

    private fun displayUserInfo() {
        binding.emailText.text = viewModel.User.accountId
        binding.userNameText.setText(viewModel.User.userName)
        binding.introduction.setText(viewModel.User.introduction)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}