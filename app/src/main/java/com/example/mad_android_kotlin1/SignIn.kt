package com.example.mad_android_kotlin1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_android_kotlin1.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SignIn : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userTypeSpinner: Spinner
    private lateinit var userTypes: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Realtime Database reference
        database = FirebaseDatabase.getInstance().getReference("users")

        // Initialize Spinner and User Types
        userTypeSpinner = binding.userTypeSpinner
        userTypes = arrayOf("Customer", "Car Company", "Garage Owner", "Spare Part Seller", "Admin")

        // Set up Spinner Adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userTypeSpinner.adapter = adapter

        // Add a listener to the email input field to detect when the user finishes typing
        binding.emailInputSignIn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                if (email.isNotEmpty()) {
                    getUserTypeByEmail(email)
                }
            }
        })

        // Sign-in button logic remains unchanged
        binding.signInButton.setOnClickListener {
            val email = binding.emailInputSignIn.text.toString().trim()
            val password = binding.passwordInputSignIn.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in successful
                            Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Sign In Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signInLink.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }

    // Method to query Firebase and retrieve user type based on email
    private fun getUserTypeByEmail(email: String) {
        database.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val role = userSnapshot.child("role").getValue(String::class.java)
                            if (role != null) {
                                // Set the spinner to the correct role
                                val position = userTypes.indexOf(role)
                                if (position >= 0) {
                                    userTypeSpinner.setSelection(position)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@SignIn, "No user found with this email", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SignIn, "Error fetching user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
