package com.practical.firebaseimageupload

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.practical.firebaseimageupload.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var storageRef: StorageReference
    private lateinit var binding: ActivityMainBinding
    private lateinit var imgUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageRef = FirebaseStorage.getInstance().reference

        binding.btnBrowse.setOnClickListener() {
            startForResult.launch("image/*")
        }

        binding.btnUpload.setOnClickListener() {
            val ref = storageRef.child("image/*")

            ref.putFile(imgUri)
                .addOnSuccessListener {
                    binding.tvDisplay.text = "Upload Successfully"
                    binding.imgProfile.setImageURI(null)
                }
                .addOnFailureListener { ex->
                    binding.tvDisplay.text = ex.message
                }
        }

        binding.btnGet.setOnClickListener() {
            val imgID = binding.txtInput.text.toString()

            val ref = storageRef.child("image/${imgID}.jpg")
            val file = File.createTempFile("temp","jpg")

            binding.pgBar.visibility = View.VISIBLE

            ref.getFile(file)
                .addOnSuccessListener {
                    val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
                    binding.imgProfile.setImageBitmap(myBitmap)
                    binding.pgBar.visibility = View.GONE
                }
                .addOnFailureListener { ex->
                    binding.tvDisplay.text = ex.message
                }
        }
    }

    val startForResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imgUri = uri
        binding.imgProfile.setImageURI(uri)
    }
}