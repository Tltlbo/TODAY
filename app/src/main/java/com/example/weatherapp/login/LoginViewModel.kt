package com.example.weatherapp.login

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var auth = FirebaseAuth.getInstance()
    var id : MutableLiveData<String> = MutableLiveData("")
    var password : MutableLiveData<String> = MutableLiveData("")

    var showInputNumberActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    var showFindIdActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    var showMainActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val context = getApplication<Application>().applicationContext

    var googleSignInAccount : GoogleSignInClient

    init {
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInAccount = GoogleSignIn.getClient(context,gso)
    }

    fun loginWithSignUpEmail() {
        auth.createUserWithEmailAndPassword(id.value.toString(), password.value.toString()).addOnCompleteListener {
            if(it.isSuccessful) {
                showInputNumberActivity.value = true
            } else {
                loginEmail()
            }
        }
    }

    fun loginEmail() {
        auth.signInWithEmailAndPassword(id.value.toString(),password.value.toString()).addOnCompleteListener {
            if(it.isSuccessful) {
                if(it.result.user?.isEmailVerified == true) { // 여기서 true로 안넘어오는듯
                    showMainActivity.value = true
                } else {
                    showInputNumberActivity.value = true
                }
            }
        }
    }

    fun loginGoogle(view : View) {
        var i = googleSignInAccount.signInIntent
        (view.context as? LoginActivity)?.googleLoginResult?.launch(i)
    }

    fun firebaseAuthWithGoogle(idToken : String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful) {
                if(it.result.user?.isEmailVerified == true) {
                    showMainActivity.value = true
                } else {
                    showInputNumberActivity.value = true
                }
            }
        }
    }
}