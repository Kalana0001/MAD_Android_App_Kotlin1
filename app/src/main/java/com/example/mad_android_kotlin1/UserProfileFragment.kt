package com.example.mad_android_kotlin1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserProfileFragment : Fragment() {

    private lateinit var headerCard: CardView
    private lateinit var profileImage: ImageView
    private lateinit var balanceAmount: TextView
    private lateinit var balanceLabel: TextView
    private lateinit var bookingsCount: TextView
    private lateinit var bookingsLabel: TextView
    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var updateButton: Button

    // Firebase variables
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Initialize views
        headerCard = view.findViewById(R.id.headerCard)
        profileImage = view.findViewById(R.id.profileImage)
        balanceAmount = view.findViewById(R.id.balanceAmount)
        balanceLabel = view.findViewById(R.id.balanceLabel)
        bookingsCount = view.findViewById(R.id.bookingsCount)
        bookingsLabel = view.findViewById(R.id.bookingsLabel)
        fullNameEditText = view.findViewById(R.id.fullNameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText)
        locationEditText = view.findViewById(R.id.locationEditText)
        updateButton = view.findViewById(R.id.updateButton)

        // Initialize Firebase Auth and Database Reference
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users") // Adjust according to your DB structure

        // Load user details
        loadUserProfile()

        // Set up the update button click listener
        updateButton.setOnClickListener {
            updateProfile()
        }

        // Make the email EditText non-editable
        emailEditText.isFocusable = false
        emailEditText.isClickable = false

        return view
    }

    private fun loadUserProfile() {
        // Get current user's ID
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            // Retrieve user data from Firebase Realtime Database
            databaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val fullName = snapshot.child("username").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java)
                        val location = snapshot.child("location").getValue(String::class.java)

                        // Update UI with user data
                        fullNameEditText.setText(fullName)
                        emailEditText.setText(email) // Display email but do not allow editing
                        phoneNumberEditText.setText(phoneNumber)
                        locationEditText.setText(location)

                        // Set example balance and bookings
                        balanceAmount.text = "$123" // Replace with real data if available
                        bookingsCount.text = "12" // Replace with real data if available
                    } else {
                        Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error fetching user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "User not signed in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProfile() {
        val fullName = fullNameEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()
        val location = locationEditText.text.toString()

        // Get current user's ID
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            // Prepare the data to update
            val userUpdates = mapOf(
                "username" to fullName,
                "phoneNumber" to phoneNumber,
                "location" to location
            )

            // Update the user data in Firebase
            databaseReference.child(userId).updateChildren(userUpdates)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { error ->
                    Toast.makeText(requireContext(), "Update failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not signed in", Toast.LENGTH_SHORT).show()
        }
    }
}
