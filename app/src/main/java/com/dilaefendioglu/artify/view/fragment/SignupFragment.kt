package com.dilaefendioglu.artify.view.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.dilaefendioglu.artify.R
import com.dilaefendioglu.artify.databinding.FragmentSignupBinding
import com.dilaefendioglu.artify.utils.Constants
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

        binding.animationView.setAnimation(R.raw.signup_anim)
        binding.animationView.playAnimation()

        binding.btnRegister.setOnClickListener {
            val userName = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (TextUtils.isEmpty(userName)) {
                binding.etUsername.setError(Constants.USERNAME_REQUIRED)
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError(Constants.ERROR_EMAIL_REQUIRED)
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError(Constants.ERROR_PASSWORD_REQUIRED)
                return@setOnClickListener
            }

            if (userName.length > 10) {
                binding.etUsername.setError(Constants.USERNAME_MAX_LENGTH)
                return@setOnClickListener
            } else if (userName.length < 3) {
                binding.etUsername.setError(Constants.USERNAME_MIN_LENGTH)
                return@setOnClickListener
            }

            if (password.length > 15) {
                binding.etPassword.setError(Constants.PASSWORD_MAX_LENGTH)
                return@setOnClickListener
            } else if (password.length < 6) {
                binding.etPassword.setError(Constants.PASSWORD_MIN_LENGTH)
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            Constants.SUCCESS_USER_CREATED,
                            Toast.LENGTH_SHORT
                        ).show()

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

                                    findNavController().navigate(R.id.action_signupFragment_to_listFragment)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        requireContext(),
                                        "Error: ${e.localizedMessage}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        return binding.root
    }
}