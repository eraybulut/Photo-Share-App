package com.eray.eraygram.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eray.eraygram.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val currentUser=auth.currentUser

        if (currentUser!=null){
            startActivity(Intent(this@LoginActivity, FeedActivity::class.java))
            finish()
        }


        binding.buttonLogin.setOnClickListener(){

            var mail = binding.editTextLoginMail.text.toString().trim()
            var password = binding.editTextLoginPassword.text.toString().trim()


            if (mail.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "Enter Email and password", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(mail,password).addOnSuccessListener {

                    val intent = Intent(this@LoginActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()

                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.textViewGoToRegister.setOnClickListener(){
            startActivity(Intent(this@LoginActivity, SiginActivity::class.java))
        }

    }
}