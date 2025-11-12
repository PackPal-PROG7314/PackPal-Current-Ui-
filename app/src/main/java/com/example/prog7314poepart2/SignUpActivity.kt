package com.example.prog7314poepart2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        val emailField = findViewById<EditText>(R.id.editTextSignupEmail)
        val passwordField = findViewById<EditText>(R.id.editTextSignupPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.editTextSignupConfirmPassword)
        val signupButton = findViewById<Button>(R.id.btnCreateAccount)

        auth = FirebaseAuth.getInstance()
        sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        signupButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    // 🔹 Create Firebase account
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                user?.sendEmailVerification()

                                // 🔹 Save locally (for offline or cached login)
                                with(sharedPref.edit()) {
                                    putString("user_email", email)
                                    putString("user_password", password)
                                    apply()
                                }

                                Toast.makeText(
                                    this,
                                    "Account created! Please verify your email and then login.",
                                    Toast.LENGTH_LONG
                                ).show()

                                // Return to LoginActivity
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            } else {
                                val error = task.exception?.message ?: "Unknown error"
                                Toast.makeText(this, "Signup failed: $error", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
