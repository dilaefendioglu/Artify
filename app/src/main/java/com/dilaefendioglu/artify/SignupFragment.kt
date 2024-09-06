package com.dilaefendioglu.artify

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dilaefendioglu.artify.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener {
            val userName = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (TextUtils.isEmpty(userName)) {
                binding.etUsername.setError("Username is required")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("E-mail adress is required")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError("Password is required")
                return@setOnClickListener
            }

            if (userName.length > 10) {
                binding.etUsername.setError("Maximum lengt is 10..")
            } else if (userName.length < 3) {
                binding.etUsername.setError("Minumum lengt is 4..")
            }

            if (password.length > 15) {
                binding.etPassword.setError("Maximum lengt is 10..")
            } else if (userName.length < 8) {
                binding.etPassword.setError("Minumum lengt is 4..")
            }


            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "User created successfully", Toast.LENGTH_SHORT).show()

                        val user = hashMapOf(
                            "username" to userName,
                            "e-mail" to email,
                            "password" to password
                        )

                        val userID = auth.currentUser?.uid
                        if (userID != null) {
                            db.collection("users").document(userID).set(user)
                                .addOnSuccessListener {
                                    Log.d(TAG, "User profile created for userID: $userID")
                                }
                        }

                    } else {
                        Toast.makeText(requireContext(), "Error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        return binding.root
    }
}