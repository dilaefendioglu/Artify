package com.dilaefendioglu.artify.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dilaefendioglu.artify.R
import com.dilaefendioglu.artify.databinding.FragmentLoginBinding
import com.dilaefendioglu.artify.utils.Constants
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

        binding.animationView.setAnimation(R.raw.login_anim)
        binding.animationView.playAnimation()

        binding.btnLogin.setOnClickListener {
            val userEmail = binding.logEmail.text.toString().trim()
            val userPass = binding.logPassword.text.toString().trim()

            if (TextUtils.isEmpty(userEmail)) {
                binding.logEmail.setError(Constants.ERROR_EMAIL_REQUIRED)
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(userPass)) {
                binding.logPassword.setError(Constants.ERROR_PASSWORD_REQUIRED)
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener { task ->
                try {
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                           Constants.SUCCESS_SIGN_IN,
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_loginFragment_to_listFragment)
                        val email = binding.logEmail.text
                        val sharedPref = getActivity()?.getPreferences(Context.MODE_PRIVATE)
                            ?: return@addOnCompleteListener
                        with(sharedPref.edit()) {
                            putString(Constants.USER_EMAIL, email.toString())
                            apply()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error! ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), Constants.ERROR_SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        binding.forgotPassword.setOnClickListener {
            val email = binding.logEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), Constants.ERROR_PASSWORD_RESET, Toast.LENGTH_SHORT)
                    .show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                Constants.SUCCESS_PASSWORD_RESET,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${task.exception?.message ?: Constants.ERROR_UNKNOWN}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        return binding.root
    }
}