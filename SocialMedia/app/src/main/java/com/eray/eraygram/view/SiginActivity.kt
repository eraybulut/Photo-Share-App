package com.eray.eraygram.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eray.eraygram.databinding.ActivitySiginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SiginActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySiginBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.buttonRegister.setOnClickListener() {
            auth = Firebase.auth

            var mail = binding.editTextRegisterMail.text.toString().trim()
            var password = binding.editTextRegisterPassword.text.toString().trim()

            if (mail.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "Enter Email and password", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener {

                    val intent = Intent(this@SiginActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()

                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

}