package com.dilaefendioglu.artify

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dilaefendioglu.artify.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val userEmail = binding.logEmail.text.toString().trim()
            val userPass = binding.logPassword.text.toString().trim()

            if (TextUtils.isEmpty(userEmail)) {
                binding.logEmail.setError("E-mail adress is required")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(userPass)) {
                binding.logPassword.setError("Password is required")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "User login successful", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error! ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }
}