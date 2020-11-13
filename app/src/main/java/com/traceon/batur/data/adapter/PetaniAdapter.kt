package com.traceon.batur.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.traceon.batur.R
import com.traceon.batur.data.model.Petani
import com.traceon.batur.databinding.ItemPetaniBinding
import javax.inject.Inject

class PetaniAdapter @Inject constructor(
    private val listPetani: List<Petani>,
    private val listener: (Petani) -> Unit
) : RecyclerView.Adapter<PetaniAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPetaniBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPetani[position], listener)
    }

    override fun getItemCount(): Int = R.layout.item_petani

    class ViewHolder(private val holder: ItemPetaniBinding) : RecyclerView.ViewHolder(holder.root) {
        fun bind(petani: Petani, listener: (Petani) -> Unit) {
            holder.tvPetani.text = petani.nama
            holder.tvAlamat.text = petani.alamat
            holder.tvLand.text = petani.lahan_total.toString()

            Glide.with(itemView.context)
                .load(petani.foto_petani)
                .into(holder.ivPhoto)
        }
    }
}