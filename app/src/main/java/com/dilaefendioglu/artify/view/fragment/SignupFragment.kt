package com.dilaefendioglu.artify.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dilaefendioglu.artify.R
import com.dilaefendioglu.artify.databinding.FragmentSignupBinding
import com.dilaefendioglu.artify.utils.Constants
import com.dilaefendioglu.artify.viewmodel.SignupViewModel

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        binding.animationView.setAnimation(R.raw.signup_anim)
        binding.animationView.playAnimation()

        binding.btnRegister.setOnClickListener {
            val userName = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(userName, email, password)) {
                signupViewModel.createUser(userName, email, password)
            }
        }

        signupViewModel.signupSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), Constants.SUCCESS_USER_CREATED, Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_signupFragment_to_listFragment)
            }
        })

        signupViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    private fun validateInput(userName: String, email: String, password: String): Boolean {
        if (TextUtils.isEmpty(userName)) {
            binding.etUsername.error = Constants.USERNAME_REQUIRED
            return false
        }
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.error = Constants.ERROR_EMAIL_REQUIRED
            return false
        }
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.error = Constants.ERROR_PASSWORD_REQUIRED
            return false
        }
        if (userName.length > 10 || userName.length < 3) {
            binding.etUsername.error =
                if (userName.length > 10) Constants.USERNAME_MAX_LENGTH else Constants.USERNAME_MIN_LENGTH
            return false
        }
        if (password.length > 15 || password.length < 6) {
            binding.etPassword.error =
                if (password.length > 15) Constants.PASSWORD_MAX_LENGTH else Constants.PASSWORD_MIN_LENGTH
            return false
        }
        return true
    }
}
