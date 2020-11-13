package com.traceon.batur.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.traceon.batur.data.model.Farmer
import com.traceon.batur.databinding.ItemFarmerBinding
import com.traceon.batur.utils.Helper
import javax.inject.Inject

class BaselineAdapter @Inject constructor(
    private val listPetani: List<Farmer>,
    private val listener: OnItemCheckListener
) :
    RecyclerView.Adapter<BaselineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFarmerBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPetani[position], listener)
    }

    override fun getItemCount(): Int = listPetani.size

    class ViewHolder(private val holder: ItemFarmerBinding) :
        RecyclerView.ViewHolder(holder.root) {
        fun bind(farmer: Farmer, listener: OnItemCheckListener) {
            holder.tvNama.text = farmer.namaPetani
            holder.tvAlamat.text = farmer.alamatPetani

            holder.tvSize.text =
                Helper.formatCapacity(farmer.ukuranTotal.toString().toDouble(), 2)

            holder.checkboxPetani.setOnCheckedChangeListener { _, isChecked ->
                farmer.dipilih = isChecked
                if (isChecked) {
                    listener.onItemCheck(farmer)
                } else {
                    farmer.foto = isChecked
                    holder.switchFoto.isChecked = isChecked
                    listener.onItemUnCheck(farmer)
                }
            }

            holder.switchFoto.setOnCheckedChangeListener { _, isChecked ->
                farmer.foto = isChecked
                if (isChecked) {
                    holder.checkboxPetani.isChecked = isChecked
                    farmer.dipilih = isChecked
                    holder.tvSize.text =
                        Helper.formatCapacity(farmer.ukuranTotal.toString().toDouble(), 2)
                    listener.onItemCheck(farmer)
                } else {
                    holder.tvSize.text =
                        Helper.formatCapacity(farmer.ukuranText.toString().toDouble(), 2)
                    listener.onItemUnCheck(farmer)
                }
            }
            holder.checkboxPetani.isChecked = farmer.dipilih
            holder.switchFoto.isChecked = farmer.foto
        }
    }

    interface OnItemCheckListener {
        fun onItemCheck(item: Farmer)
        fun onItemUnCheck(item: Farmer)
    }

}