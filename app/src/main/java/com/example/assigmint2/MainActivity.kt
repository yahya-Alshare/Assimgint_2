package com.example.firebasestorgeass2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.assigmint2.R
import com.example.assigmint2.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val storageRef = Firebase.storage.reference
    private lateinit var uploadButton: Button
    private lateinit var download_bt: Button

    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        uploadButton = findViewById(R.id.upload_btn)
        download_bt = findViewById(R.id.download_button)
        progressBar = findViewById(R.id.progress_bar)

        download_bt.setOnClickListener {
            val intent = Intent(this, MainActivity2::class)
            startActivity(intent)
        }
        uploadButton.setOnClickListener {
            selectFile()

        }

    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/pdf"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun uploadFile(fileUri: Uri) {
        val fileName = fileUri.lastPathSegment!!
        val fileRef = storageRef.child(fileName)

        val uploadTask = fileRef.putFile(fileUri)

        uploadTask.addOnSuccessListener {
            progressBar.visibility = View.GONE
            statusText.text = "Upload successful"
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            statusText.text = "Upload failed"
        }.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
            progressBar.progress = progress.toInt()
        }
    }

    companion object {
        const val REQUEST_CODE = 101
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { fileUri ->
                progressBar.visibility = View.VISIBLE
                statusText.text = "Uploading..."
                uploadFile(fileUri)
            }
        }
    }

}