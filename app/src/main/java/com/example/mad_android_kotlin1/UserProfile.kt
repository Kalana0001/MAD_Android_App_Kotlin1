package com.example.mad_android_kotlin1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        // Initialize views
        val headerCard: CardView = findViewById(R.id.headerCard)
        val profileImage: ImageView = findViewById(R.id.profileImage)
        val balanceAmount: TextView = findViewById(R.id.balanceAmount)
        val balanceLabel: TextView = findViewById(R.id.balanceLabel)
        val bookingsCount: TextView = findViewById(R.id.bookingsCount)
        val bookingsLabel: TextView = findViewById(R.id.bookingsLabel)
        val fullNameEditText: EditText = findViewById(R.id.fullNameEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val phoneNumberEditText: EditText = findViewById(R.id.phoneNumberEditText)
        val locationEditText: EditText = findViewById(R.id.locationEditText)
        val updateButton: Button = findViewById(R.id.updateButton)

        // Example setup code (modify according to your needs)
        profileImage.setImageResource(R.drawable.baseline_supervised_user_circle_24) // Ensure this drawable exists
        balanceAmount.text = "$123"
        balanceLabel.text = "Balance"
        bookingsCount.text = "12"
        bookingsLabel.text = "Bookings"


        updateButton.setOnClickListener {

        }
    }
}
