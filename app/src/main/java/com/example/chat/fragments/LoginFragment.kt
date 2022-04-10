package com.example.chat.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chat.R
import com.example.chat.activities.MainActivity
import com.example.chat.databinding.FragmentLoginBinding
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
        // Inflate the layout for this fragment
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

    /**
     * Checks whether name and email are valid or not
     */
    private fun isValidCredential(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                context?.toast(R.string.enter_your_email)
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                context?.toast(R.string.email_not_valid)
                false
            }
            password.isEmpty() -> {
                context?.toast(R.string.enter_your_password)
                false
            }
            password.length < 6 -> {
                context?.toast(R.string.password_must_be_long)
                false
            }
            else -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.layout.visibility = View.GONE
                true
            }
        }
    }

    private fun login(email: String, password: String) {
        if (!isValidCredential(email, password)) return
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateUI()
                } else {
                    context?.toast(it.exception?.message
                        ?: getString(R.string.some_error_occurred))
                    binding.progressBar.visibility = View.GONE
                    binding.layout.visibility = View.VISIBLE
                }
            }
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