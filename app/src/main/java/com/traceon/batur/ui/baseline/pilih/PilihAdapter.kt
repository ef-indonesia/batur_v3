package com.traceon.batur.ui.baseline.pilih

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.traceon.batur.data.model.Pilih
import com.traceon.batur.databinding.ItemPilihBinding
import com.traceon.batur.utils.Helper
import javax.inject.Inject

class PilihAdapter @Inject constructor(
    private val listPetani: List<Pilih>,
    private val listener: OnItemCheckListener
) :
    RecyclerView.Adapter<PilihAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPilihBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPetani[position], listener)
    }

    override fun getItemCount(): Int = listPetani.size

    class ViewHolder(private val holder: ItemPilihBinding) :
        RecyclerView.ViewHolder(holder.root) {
        fun bind(pilih: Pilih, listener: OnItemCheckListener) {
            holder.tvNama.text = pilih.namaPetani
            holder.tvAlamat.text = pilih.alamatPetani

            holder.tvSize.text =
                Helper.formatCapacity(pilih.ukuranTotal.toString().toDouble(), 2)

            holder.checkboxPetani.setOnCheckedChangeListener { _, isChecked ->
                pilih.dipilih = isChecked
                if (isChecked) {
                    listener.onItemCheck(pilih)
                } else {
                    pilih.foto = isChecked
                    holder.switchFoto.isChecked = isChecked
                    listener.onItemUnCheck(pilih)
                }
            }

            holder.switchFoto.setOnCheckedChangeListener { _, isChecked ->
                pilih.foto = isChecked
                if (isChecked) {
                    holder.checkboxPetani.isChecked = isChecked
                    pilih.dipilih = isChecked
                    holder.tvSize.text =
                        Helper.formatCapacity(pilih.ukuranTotal.toString().toDouble(), 2)
                    listener.onItemCheck(pilih)
                } else {
                    holder.tvSize.text =
                        Helper.formatCapacity(pilih.ukuranText.toString().toDouble(), 2)
                    listener.onItemUnCheck(pilih)
                }
            }
            holder.checkboxPetani.isChecked = pilih.dipilih
            holder.switchFoto.isChecked = pilih.foto
        }
    }

    interface OnItemCheckListener {
        fun onItemCheck(item: Pilih)
        fun onItemUnCheck(item: Pilih)
    }

}