package com.eray.eraygram.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.eray.eraygram.R
import com.eray.eraygram.adapter.PostAdapter
import com.eray.eraygram.databinding.ActivityFeedBinding
import com.eray.eraygram.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var postAdapter:PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth=Firebase.auth
        db=Firebase.firestore
        getData()

        postArrayList=ArrayList<Post>()
        postAdapter= PostAdapter(this,postArrayList)

        binding.toolbarFeed.setTitle("Home")
        setSupportActionBar(binding.toolbarFeed)

        binding.recylerView.setHasFixedSize(true)
        binding.recylerView.layoutManager=LinearLayoutManager(this)

        binding.recylerView.adapter=postAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.action_addPost ->{
                startActivity(Intent(this@FeedActivity, UploadActivity::class.java))
                return true
            }
            R.id.action_signOut ->{

                auth.signOut()
                startActivity(Intent(this@FeedActivity, LoginActivity::class.java))
                finish()

                return true
            }
        }
        return false
    }

    fun getData(){

        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error!==null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_SHORT).show()
            }else{
                if (value!=null){
                    if(!value.isEmpty){

                        val document=value.documents
                        postArrayList.clear()
                        for(doc in document){
                            val commet=doc.get("comment") as String
                            val userEmail=doc.get("userEmail") as String
                            val dowloadUrl=doc.get("dowloadUrl") as String

                            var post=Post(userEmail,commet,dowloadUrl)
                            postArrayList.add(post)
                        }
                        postAdapter.notifyDataSetChanged()

                    }
                }
            }
        }


    }
}