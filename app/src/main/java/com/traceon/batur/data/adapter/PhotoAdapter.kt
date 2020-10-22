package com.traceon.batur.data.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.traceon.batur.databinding.ItemVisitPhotoBinding
import javax.inject.Inject

class PhotoAdapter @Inject constructor(private val listFoto: List<Uri>) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemVisitPhotoBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFoto[position])
    }

    override fun getItemCount(): Int = listFoto.size

    class ViewHolder(private val holder: ItemVisitPhotoBinding) :
        RecyclerView.ViewHolder(holder.root) {
        fun bind(uri: Uri) {
            Glide.with(itemView.context)
                .load(uri)
                .into(holder.ivPhoto)

            holder.ivDelete.setOnClickListener {

            }
        }
    }
}