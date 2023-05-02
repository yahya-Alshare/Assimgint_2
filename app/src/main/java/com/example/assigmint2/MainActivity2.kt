package com.example.firebasestorgeass2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.assigmint2.R
import com.example.assigmint2.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val storage = FirebaseStorage.getInstance()
    private val filesList = mutableListOf<StorageReference>()
    private lateinit var progressBar: ProgressBar
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        getAllFiles()
    }
    private fun getAllFiles() {
        progressBar  = findViewById(R.id.progressBar)
        listView = findViewById(R.id.filesList)

        val storageRef = storage.reference
        val listAllTask = storageRef.listAll()


        progressBar.visibility = View.VISIBLE

        listAllTask.addOnSuccessListener { result ->

            filesList.addAll(result.items)

            val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                filesList.map { it.name })

            // Set the adapter to the ListView
            listView.adapter = adapter

            listView.setOnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as String
                val fileRef = storageRef.child(selectedItem)


                progressBar.visibility = View.VISIBLE

                // download the file and show the progress in the progress bar
                val file = File(getExternalFilesDir(null), selectedItem)
                fileRef.getFile(file).addOnSuccessListener {

                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "File downloaded successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {

                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show()
                }.addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    progressBar.progress = progress.toInt()
                }
            }

            progressBar.visibility = View.GONE
        }.addOnFailureListener {

        }
    }


}