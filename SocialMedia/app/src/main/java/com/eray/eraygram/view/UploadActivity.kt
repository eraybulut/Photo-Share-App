package com.eray.eraygram.view

import android.Manifest

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eray.eraygram.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.IOException
import java.util.*

class UploadActivity : AppCompatActivity() {


    var selectedPicture : Uri? = null
    var selectedBitmap : Bitmap? = null
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var auth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage:FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        registerLauncher()

        auth=Firebase.auth
        firestore=Firebase.firestore
        storage=Firebase.storage



        //image resize
        binding.imageViewUploadMage.getLayoutParams().width = 550
        binding.imageViewUploadMage.getLayoutParams().height = 600
        binding.imageViewUploadMage.setAdjustViewBounds(true)

        binding.toolbarUpload.setTitle("Upload Post")
        setSupportActionBar(binding.toolbarUpload)


    }

    fun postUpload(view: View){

        //save pictures with different names
        val uuid= UUID.randomUUID()
        val imageName="$uuid.jpg"

        val reference=storage.reference
        val imageReference=reference.child("images").child(imageName)


        if (selectedPicture!=null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {

                val uploadPictureReference=storage.reference.child("images").child(imageName)

                uploadPictureReference.downloadUrl.addOnSuccessListener {

                  
                    val dowloadUrl=it.toString()
                    val postMap= hashMapOf<String,Any>()
                    postMap.put("dowloadUrl",dowloadUrl)
                    postMap.put("userEmail",auth.currentUser!!.email!!)
                    postMap.put("comment",binding.editTextUploadCommet.text.toString().trim())
                    postMap.put("date",Timestamp.now())

                    
                    firestore.collection("Posts").add(postMap).addOnSuccessListener {
                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_SHORT).show()
                    }



                }

            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun selectİmage(view: View){

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",){
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedPicture = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(
                                this@UploadActivity.contentResolver,
                                selectedPicture!!
                            )
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageViewUploadMage.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                this@UploadActivity.contentResolver,
                                selectedPicture
                            )
                            binding.imageViewUploadMage.setImageBitmap(selectedBitmap)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(this@UploadActivity, "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

}
