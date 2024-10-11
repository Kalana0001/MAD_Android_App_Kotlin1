package com.example.mad_android_kotlin1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase

class AddActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var price: EditText
    private lateinit var phone: EditText
    private lateinit var turl: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        name = findViewById(R.id.txtName)
        price = findViewById(R.id.txtCourse)
        phone = findViewById(R.id.txtEmail)
        turl = findViewById(R.id.txtImageUrl)

        btnAdd = findViewById(R.id.btnAdd)
        btnBack = findViewById(R.id.btnBack)

        btnAdd.setOnClickListener {
            if (validateInputs()) {
                insertData()
                clearAll()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        if (name.text.toString().isEmpty()) {
            name.error = "Name is required"
            return false
        }
        if (price.text.toString().isEmpty()) {
            price.error = "Price is required"
            return false
        }
        if (phone.text.toString().isEmpty()) {
            phone.error = "Phone number is required"
            return false
        }
        if (turl.text.toString().isEmpty()) {
            turl.error = "Image URL is required"
            return false
        }
        return true
    }

    private fun insertData() {
        val priceValue = price.text.toString().toIntOrNull()
        val phoneValue = phone.text.toString().toIntOrNull()

        if (priceValue == null) {
            price.error = "Price must be a valid number"
            return
        }

        if (phoneValue == null) {
            phone.error = "Phone number must be a valid number"
            return
        }

        val map = HashMap<String, Any>()
        map["name"] = name.text.toString()
        map["price"] = priceValue // Store  as an Int
        map["phone"] = phoneValue
        map["turl"] = turl.text.toString()

        FirebaseDatabase.getInstance().reference.child("spareparts").push()
            .setValue(map)
            .addOnSuccessListener {
                Toast.makeText(this, "Data Insert Successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error While Insertion: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearAll() {
        name.setText("")
        price.setText("")
        phone.setText("")
        turl.setText("")
    }
}
