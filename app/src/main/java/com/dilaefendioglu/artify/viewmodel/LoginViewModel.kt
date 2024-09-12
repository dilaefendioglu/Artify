package com.dilaefendioglu.artify.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dilaefendioglu.artify.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signInSuccess = MutableLiveData<Boolean>()
    val signInSuccess: LiveData<Boolean> get() = _signInSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    _signInSuccess.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: Constants.ERROR_UNKNOWN
                }
            } catch (e: Exception) {
                _errorMessage.value = Constants.ERROR_SOMETHING_WENT_WRONG
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _signInSuccess.value = true
            } else {
                _errorMessage.value = task.exception?.message ?: Constants.ERROR_UNKNOWN
            }
        }
    }

    fun saveUserEmail(email: String) {
        val sharedPref =
            getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(Constants.USER_EMAIL, email)
            apply()
        }
    }
}
