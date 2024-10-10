package com.example.mad_android_kotlin1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class UserDetailsForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details_form) // Ensure this matches your XML layout file name

        // Initialize views
        val headerCard: CardView = findViewById(R.id.headerCard)
        val profileImage: ImageView = findViewById(R.id.profileImage)
        val balanceAmount: TextView = findViewById(R.id.balanceAmount)
        val balanceLabel: TextView = findViewById(R.id.balanceLabel)
        val bookingsCount: TextView = findViewById(R.id.bookingsCount)
        val bookingsLabel: TextView = findViewById(R.id.bookingsLabel)
        val fullNameEditText: EditText = findViewById(R.id.udInputFullName)
        val emailEditText: EditText = findViewById(R.id.udInputEmail)
        val addressEditText: EditText = findViewById(R.id.udInputAddress)
        val mobileNumberEditText: EditText = findViewById(R.id.udInputMobileNumber)
        val submitButton: Button = findViewById(R.id.submit_button)


        profileImage.setImageResource(R.drawable.baseline_supervised_user_circle_24) // Ensure this drawable exists
        balanceAmount.text = "$123"
        balanceLabel.text = "Balance"
        bookingsCount.text = "12"
        bookingsLabel.text = "Bookings"

        // Set up listeners or other logic
        submitButton.setOnClickListener {
            // Handle button click
            val fullName = fullNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val address = addressEditText.text.toString()
            val mobileNumber = mobileNumberEditText.text.toString()


            println("Full Name: $fullName")
            println("Email: $email")
            println("Address: $address")
            println("Mobile Number: $mobileNumber")
        }
    }
}
