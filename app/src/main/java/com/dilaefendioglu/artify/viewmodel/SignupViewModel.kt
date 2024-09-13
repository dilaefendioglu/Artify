package com.dilaefendioglu.artify.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dilaefendioglu.artify.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _signupSuccess = MutableLiveData<Boolean>()
    val signupSuccess: LiveData<Boolean> = _signupSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun createUser(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userID = auth.currentUser?.uid
                    if (userID != null) {
                        val user = hashMapOf(
                            "username" to userName,
                            "e-mail" to email,
                            "password" to password
                        )

                        db.collection("users").document(userID).set(user)
                            .addOnSuccessListener {
                                Log.d("SignupViewModel", "User profile created for userID: $userID")
                                _signupSuccess.value = true
                            }
                            .addOnFailureListener { e ->
                                _errorMessage.value = e.localizedMessage ?: Constants.GENERIC_ERROR
                            }
                    }
                } else {
                    _errorMessage.value =
                        task.exception?.localizedMessage ?: Constants.GENERIC_ERROR
                }
            }
    }
}
