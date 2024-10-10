package com.example.mad_android_kotlin1

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_android_kotlin1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var selectedUserType: String = "Customer" // Default user type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Realtime Database reference
        database = FirebaseDatabase.getInstance().getReference("users")

        // Set up the user type Spinner
        val userTypes = arrayOf("Customer", "Car Company", "Garage Owner", "Spare Part Seller", "Admin")
        val userTypeSpinner: Spinner = binding.userTypeSpinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userTypeSpinner.adapter = adapter

        userTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                selectedUserType = userTypes[position] // Set the selected user type
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Default selection can be set here if needed
            }
        }

        // Set up the click listener for the sign-up button
        binding.signUpButton.setOnClickListener {
            val username = binding.usernameInputSignup.text.toString().trim() // Get the username input
            val email = binding.emailInputSignup.text.toString().trim()
            val password = binding.passwordInputSignup.text.toString().trim()
            val confirmPassword = binding.ConfirmPassword.text.toString().trim()

            // Validate inputs
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                // Create user with email and password
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = firebaseAuth.currentUser
                            val uid = firebaseUser?.uid
                            val userMap = hashMapOf(
                                "uid" to uid,
                                "email" to email,
                                "username" to username, // Add the username to the user data
                                "role" to selectedUserType
                            )

                            // Store user data in Firebase Realtime Database
                            uid?.let {
                                database.child(it).setValue(userMap)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, SignIn::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Database Error: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter username, email, password, and ensure passwords match", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to Sign In activity
        binding.signInLink.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }

        // Toggle password visibility for the password field
        binding.togglePasswordVisibility.setOnClickListener {
            togglePasswordVisibility(
                binding.passwordInputSignup,
                binding.togglePasswordVisibility,
                R.drawable.baseline_remove_red_eye_24 // Change to the correct resource if needed
            )
        }

        // Toggle password visibility for the confirm password field
        binding.togglePasswordVisibility1.setOnClickListener {
            togglePasswordVisibility(
                binding.ConfirmPassword,
                binding.togglePasswordVisibility1,
                R.drawable.baseline_remove_red_eye_24 // Change to the correct resource if needed
            )
        }
    }

    private fun togglePasswordVisibility(editText: EditText, toggleButton: ImageView, iconResId: Int) {
        if (editText.transformationMethod == PasswordTransformationMethod.getInstance()) {
            // Show password
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            toggleButton.setImageResource(iconResId) // Update the icon if you have a different one for visibility
        } else {
            // Hide password
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            toggleButton.setImageResource(iconResId) // Update the icon if you have a different one for hiding
        }
        // Move the cursor to the end of the text
        editText.setSelection(editText.text.length)
    }
}
