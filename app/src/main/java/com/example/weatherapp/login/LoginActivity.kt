package com.example.weatherapp.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = loginViewModel
        binding.activity = this
        binding.lifecycleOwner = this

        setObserve()
    }

    fun setObserve() {
        loginViewModel.showInputNumberActivity.observe(this) {
            if(it) {
                finish()
                startActivity(Intent(this,InputNumberActivity::class.java))
            }
        }

        loginViewModel.showFindIdActivity.observe(this) {
            if(it) {
                startActivity(Intent(this,FindIdActivity::class.java))
            }
        }

        loginViewModel.showMainActivity.observe(this) {
            if(it) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }



    fun findId() {
        loginViewModel.showFindIdActivity.value = true
    }

    var googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->

        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        account.idToken
        loginViewModel.firebaseAuthWithGoogle(account.idToken)
    }
}