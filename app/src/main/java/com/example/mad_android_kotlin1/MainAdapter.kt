package com.example.mad_android_kotlin1
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder

class MainAdapter(options: FirebaseRecyclerOptions<MainModel>) :
    FirebaseRecyclerAdapter<MainModel, MainAdapter.MyViewHolder>(options) {

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: MainModel) {
        holder.name.text = model.name
        holder.price.text = model.price.toString()
        holder.phone.text = model.phone.toString()

        Glide.with(holder.img.context)
            .load(model.turl)
            .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
            .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
            .into(holder.img)

        holder.btnEdit.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(holder.img.context)
                .setContentHolder(ViewHolder(R.layout.update_popup))
                .setExpanded(true, 1300)
                .create()

            val view = dialogPlus.holderView
            val nameEditText: EditText = view.findViewById(R.id.txtName)
            val priceEditText: EditText = view.findViewById(R.id.txtCourse)
            val phoneEditText: EditText = view.findViewById(R.id.txtEmail)
            val turlEditText: EditText = view.findViewById(R.id.txtImageUrl)
            val btnUpdate: Button = view.findViewById(R.id.btnUpdate)


            nameEditText.setText(model.name)
            priceEditText.setText(model.price.toString())
            phoneEditText.setText(model.phone.toString())
            turlEditText.setText(model.turl)

            dialogPlus.show()

            btnUpdate.setOnClickListener {
                val name = nameEditText.text.toString()
                val price = priceEditText.text.toString().toIntOrNull() ?: model.price
                val phone = phoneEditText.text.toString().toIntOrNull() ?: model.phone
                val turl = turlEditText.text.toString()

                val map = HashMap<String, Any>()
                map["name"] = name
                map["price"] = price
                map["phone"] = phone
                map["turl"] = turl

                val ref = FirebaseDatabase.getInstance().reference.child("spareparts").child(getRef(position).key!!)

                ref.updateChildren(map)
                    .addOnSuccessListener {
                        Log.d("Update", "Update Successful")
                        Toast.makeText(holder.name.context, "Update Successful", Toast.LENGTH_SHORT).show()
                        dialogPlus.dismiss()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Update", "Update Error: ${e.message}")
                        Toast.makeText(holder.name.context, "Update Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        dialogPlus.dismiss()
                    }
            }
        }

        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(holder.name.context)
                .setTitle("Are you sure?")
                .setMessage("Deleted data can't be undone.")
                .setPositiveButton("Delete") { _, _ ->
                    val ref = FirebaseDatabase.getInstance().reference.child("spareparts").child(getRef(position).key!!)
                    ref.removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(holder.name.context, "Delete Successful", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(holder.name.context, "Delete Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(holder.name.context, "Cancelled", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
        return MyViewHolder(view)
    }

    override fun updateOptions(options: FirebaseRecyclerOptions<MainModel>) {
        super.updateOptions(options)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.img1)
        val name: TextView = itemView.findViewById(R.id.nametext)
        val price: TextView = itemView.findViewById(R.id.coursetext)
        val phone: TextView = itemView.findViewById(R.id.emailtext)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }
}
