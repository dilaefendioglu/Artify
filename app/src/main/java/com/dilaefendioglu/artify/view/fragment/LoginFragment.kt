package com.dilaefendioglu.artify.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dilaefendioglu.artify.R
import com.dilaefendioglu.artify.databinding.FragmentLoginBinding
import com.dilaefendioglu.artify.utils.Constants
import com.dilaefendioglu.artify.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

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

            loginViewModel.signInWithEmailAndPassword(userEmail, userPass)

            loginViewModel.signInSuccess.observe(viewLifecycleOwner) { success ->
                if (success) {
                    Toast.makeText(
                        requireContext(),
                        Constants.SUCCESS_SIGN_IN,
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_loginFragment_to_listFragment)
                    loginViewModel.saveUserEmail(userEmail)
                }
            }

            loginViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT)
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
                loginViewModel.sendPasswordResetEmail(email)

                loginViewModel.signInSuccess.observe(viewLifecycleOwner) { success ->
                    if (success) {
                        Toast.makeText(
                            requireContext(),
                            Constants.SUCCESS_PASSWORD_RESET,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                loginViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                    if (errorMessage != null) {
                        Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        return binding.root
    }
}