package com.example.chat.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chat.R
import com.example.chat.ui.activities.MainActivity
import com.example.chat.databinding.FragmentLoginBinding
import com.example.chat.utils.isValidEmailAndPassword
import com.example.chat.utils.toast
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.loginButton.setOnClickListener {
            handleLogin()
        }

        binding.signUpTextBtn.setOnClickListener {
            goToSignUpFragment()
        }

        return binding.root
    }

    private fun handleLogin() {
        val email = binding.emailField.text.toString().trim()
        val password = binding.passwordField.text.toString().trim()
        login(email, password)
    }


    private fun login(email: String, password: String) {
        if (!isValidEmailAndPassword(requireContext(), email, password)) return

        showProgressBar()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateUI()
                } else {
                    context?.toast(it.exception?.message
                        ?: getString(R.string.some_error_occurred))
                    hideProgressBar()
                }
            }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.layout.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.layout.visibility = View.VISIBLE
    }

    private fun goToSignUpFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            updateUI()
        }
    }

    private fun updateUI() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}