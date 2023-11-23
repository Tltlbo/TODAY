package com.example.weatherapp.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.MyApplication
import com.example.weatherapp.data.ModelUser
import com.example.weatherapp.databinding.ActivityModifyInformationBinding
import com.example.weatherapp.network.UserObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Response

class ModifyInfomationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityModifyInformationBinding
    private lateinit var user : ModelUser
    private lateinit var viewModel : UserInformationViewModel
    private lateinit var mainviewModel : MainViewModel
    var name : MutableLiveData<String> = MutableLiveData("")
    var introduction : MutableLiveData<String> = MutableLiveData("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = application as MyApplication

        viewModel = app.userInformationViewModel
        mainviewModel = app.mainViewModel
        binding.emailText.setText(viewModel.User.accountId)
        binding.userNameText.setText(viewModel.User.userName)
        binding.introduction.setText(viewModel.User.introduction)
        // Firebase 인증 및 Firestore 초기화
        auth = FirebaseAuth.getInstance()
        user = intent.getParcelableExtra("user") ?: ModelUser(auth.currentUser?.email.toString(), 0.0, 0.0, "입력해주세요", 0.0, 0.0, 0, "Unknown")

        // 버튼 클릭 리스너 설정
        binding.btnEditInfo.setOnClickListener {
            updateUserInformation()
        }
    }

    private fun updateUserInformation() {

        val introduct = binding.introduction.text.toString()
        val tempname = binding.userNameText.text.toString()
        val call = UserObject.getRetrofitService().modifyUserInfo(accountId = auth.currentUser?.email!!, endX = user.endX, endY = user.endY, introduction = introduct, startX = user.startX, startY = user.startY, stepCount = user.stepCount, userName = tempname)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<Void> {
            // 응답 성공 시
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    viewModel.User.userName = tempname
                    viewModel.User.introduction = introduct
                    mainviewModel.User.userName = tempname
                    mainviewModel.User.introduction = introduct
                    finish()
                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })

    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
