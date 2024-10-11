package com.example.mad_android_kotlin1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SparePartsShopFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mainAdapter: MainAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var floatingActionButton: FloatingActionButton
    private var currentUserRole: String? = null // To store the user's role

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Enable options menu
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_spare_parts_shop, container, false)

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("spareparts")

        // Set up FirebaseRecyclerOptions
        val options = FirebaseRecyclerOptions.Builder<MainModel>()
            .setQuery(databaseReference, MainModel::class.java)
            .build()

        // Initialize and set the adapter
        mainAdapter = MainAdapter(options)
        recyclerView.adapter = mainAdapter

        // Initialize floating action button
        floatingActionButton = view.findViewById(R.id.floatingActionButton)

        // Handle the floating action button click
        floatingActionButton.setOnClickListener {
            startActivity(Intent(requireContext(), AddActivity::class.java))
        }

        // Retrieve the current user's role from Firebase
        getCurrentUserRole()

        // Handle window insets if needed
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return view
    }

    private fun getCurrentUserRole() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.uid)
            userRef.child("role").get().addOnSuccessListener { dataSnapshot ->
                currentUserRole = dataSnapshot.getValue(String::class.java)
                // Update adapter to show or hide edit/delete buttons based on role
                if (currentUserRole != null) {
                    mainAdapter.setUserRole(currentUserRole == "Spare Part Seller") // Check for Spare Part Seller role
                }
            }.addOnFailureListener {
                // Handle the error
                Toast.makeText(requireContext(), "Error fetching user role", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mainAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mainAdapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search, menu)
        val item: MenuItem = menu.findItem(R.id.search)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                txtSearch(newText ?: "")
                return false
            }
        })
    }

    private fun txtSearch(str: String) {
        // Create new FirebaseRecyclerOptions with the search query
        val newOptions = FirebaseRecyclerOptions.Builder<MainModel>()
            .setQuery(databaseReference.orderByChild("name").startAt(str).endAt(str + "~"), MainModel::class.java)
            .build()

        Log.d("FirebaseQuery", "Query: ${databaseReference.orderByChild("name").startAt(str).endAt(str + "~")}")

        // Update the adapter with new options
        mainAdapter.updateOptions(newOptions)
        recyclerView.adapter = mainAdapter
    }
}
