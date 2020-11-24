package com.traceon.batur.ui.baseline.petani

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.traceon.batur.data.model.Petani
import com.traceon.batur.databinding.ItemPetaniBinding
import com.traceon.batur.utils.Helper
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

    override fun getItemCount(): Int = listPetani.size

    class ViewHolder(private val holder: ItemPetaniBinding) : RecyclerView.ViewHolder(holder.root) {
        fun bind(petani: Petani, listener: (Petani) -> Unit) {
            holder.tvPetani.text = petani.nama
            holder.tvAlamat.text = petani.alamat
            holder.tvLand.text = petani.lahan_total.toString()

            Glide.with(itemView.context)
                .load(petani.foto_petani)
                .apply(Helper.requestSmall)
                .into(holder.ivPhoto)

            itemView.setOnClickListener {
                listener(petani)
            }
        }
    }
}