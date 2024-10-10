package com.example.mad_android_kotlin1

import android.os.Bundle
import android.util.Patterns
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

class UserDetailFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_detail_form, container, false)

        // Initialize views
        val headerCard: CardView = view.findViewById(R.id.headerCard)
        val profileImage: ImageView = view.findViewById(R.id.profileImage)
        val balanceAmount: TextView = view.findViewById(R.id.balanceAmount)
        val balanceLabel: TextView = view.findViewById(R.id.balanceLabel)
        val bookingsCount: TextView = view.findViewById(R.id.bookingsCount)
        val bookingsLabel: TextView = view.findViewById(R.id.bookingsLabel)
        val fullNameEditText: EditText = view.findViewById(R.id.udInputFullName)
        val emailEditText: EditText = view.findViewById(R.id.udInputEmail)
        val addressEditText: EditText = view.findViewById(R.id.udInputAddress)
        val mobileNumberEditText: EditText = view.findViewById(R.id.udInputMobileNumber)
        val submitButton: Button = view.findViewById(R.id.btnSave)

        // Set up initial values
        profileImage.setImageResource(R.drawable.baseline_supervised_user_circle_24) // Ensure this drawable exists
        balanceAmount.text = "$123"
        balanceLabel.text = "Balance"
        bookingsCount.text = "12"
        bookingsLabel.text = "Bookings"

        // Set up the button click listener
        submitButton.setOnClickListener {
            // Handle button click
            val fullName = fullNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val address = addressEditText.text.toString()
            val mobileNumber = mobileNumberEditText.text.toString()

            // Validate inputs
            if (fullName.isEmpty() || email.isEmpty() || address.isEmpty() || mobileNumber.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Optional: Validate mobile number format here

            // Print input values for debugging
            println("Full Name: $fullName")
            println("Email: $email")
            println("Address: $address")
            println("Mobile Number: $mobileNumber")

            // Optionally store data or show success message
            Toast.makeText(requireContext(), "Details submitted successfully", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
