package com.example.weatherapp.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.weatherapp.databinding.ActivityMyInformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyInfomationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityMyInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase 인증 및 Firestore 초기화
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        displayUserInfo()

        // 버튼 클릭 리스너 설정
        binding.btnEditInfo.setOnClickListener {
            updateUserInformation()
        }
    }

    // Firestore에서 사용자 정보 업데이트
    private fun updateUserInformation() {
        val user = auth.currentUser
        val newPhone = binding.phoneText.text.toString() // 새로운 전화번호

        user?.email?.let { userEmail ->
            firestore.collection("findIds")
                .whereEqualTo("id", userEmail)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val documentId = documents.documents[0].id // 첫 번째 문서의 ID
                        firestore.collection("findIds").document(documentId)
                            .update("phoneNumber", newPhone)
                            .addOnSuccessListener {
                                showToast("정보가 업데이트되었습니다.")
                            }
                            .addOnFailureListener {
                                showToast("정보 업데이트 실패: ${it.message}")
                            }
                    } else {
                        showToast("사용자 문서를 찾을 수 없습니다.")
                    }
                }
                .addOnFailureListener { e ->
                    showToast("문서 조회 실패: ${e.message}")
                }
        }
    }

    private fun displayUserInfo() {
        val user = auth.currentUser

        user?.let {
            binding.emailText.setText(user.email ?: "이메일 없음")

            user.email?.let { userEmail ->
                firestore.collection("findIds")
                    .whereEqualTo("id", userEmail)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val phone = documents.documents[0].getString("phoneNumber")
                            binding.phoneText.setText(phone ?: "전화번호 없음")
                        } else {
                            binding.phoneText.setText("전화번호 없음")
                        }
                    }
                    .addOnFailureListener { e ->
                        binding.phoneText.setText("전화번호 가져오기 실패")
                    }
            } ?: binding.phoneText.setText("전화번호 없음")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
