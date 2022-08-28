package com.eray.eraygram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.eray.eraygram.R
import com.eray.eraygram.model.Post
import com.squareup.picasso.Picasso

class PostAdapter(var mContext: Context,var incomingPostList: List<Post>)
    :RecyclerView.Adapter<PostAdapter.cardViewHolder>() {


    inner class cardViewHolder(view: View):RecyclerView.ViewHolder(view){
        var cardView:CardView
        var textViewRvUsername:TextView
        var textViewRvComment:TextView
        var imageViewRvPost:ImageView

        init {
            cardView=view.findViewById(R.id.cardView)
            textViewRvUsername=view.findViewById(R.id.textViewRvUsername)
            textViewRvComment=view.findViewById(R.id.textViewRvComment)
            imageViewRvPost=view.findViewById(R.id.imageViewRvPost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cardViewHolder {
        var desing=LayoutInflater.from(mContext).inflate(R.layout.custom_rv,parent,false)

        return cardViewHolder(desing)
    }

    override fun onBindViewHolder(holder: cardViewHolder, position: Int) {
        var posts=incomingPostList.get(position)

        holder.textViewRvComment.text=posts.commet
        holder.textViewRvUsername.text=posts.email

        Picasso.get().load(posts.dowloadUrl).into(holder.imageViewRvPost)

        holder.cardView.setOnClickListener(){
            Toast.makeText(mContext,"Create by ${posts.email}",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return incomingPostList.size
    }

}