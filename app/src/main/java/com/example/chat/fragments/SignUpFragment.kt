package com.example.chat.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chat.R
import com.example.chat.activities.MainActivity
import com.example.chat.daos.UserDao
import com.example.chat.databinding.FragmentSignUpBinding
import com.example.chat.models.User
import com.example.chat.utils.toast
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
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
        binding = FragmentSignUpBinding.inflate(inflater)

        binding.signUpButton.setOnClickListener {
            handleSignUp()
        }

        binding.signInTextBtn.setOnClickListener {
            goToLoginFragment()
        }

        return binding.root
    }

    private fun handleSignUp() {
        val name = binding.nameField.text.toString().trim()
        val email = binding.emailField.text.toString().trim()
        val password = binding.passwordField.text.toString().trim()
        signUp(name, email, password)
    }

    private fun goToLoginFragment() {
        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
    }

    private fun signUp(name: String, email: String, password: String) {
        if (!isValidCredential(name, email, password)) return
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val firebaseUser = mAuth.currentUser!!
                    val user = User(firebaseUser.uid, name, email, null)
                    val userDao = UserDao()
                    userDao.addUser(user)
                    updateUI()
                } else {
                    context?.toast(it.exception?.message
                        ?: getString(R.string.some_error_occurred))
                    binding.progressBar.visibility = View.GONE
                    binding.layout.visibility = View.VISIBLE
                }
            }
    }

    /**
     * Checks whether name, email and password are valid or not
     */
    private fun isValidCredential(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                context?.toast(R.string.enter_your_name)
                false
            }
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

    private fun updateUI() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}