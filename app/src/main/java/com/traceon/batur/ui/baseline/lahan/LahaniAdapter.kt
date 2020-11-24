package com.traceon.batur.ui.baseline.lahan

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.traceon.batur.R
import com.traceon.batur.data.model.Lahan
import com.traceon.batur.databinding.ItemLahanBinding
import javax.inject.Inject

class LahaniAdapter @Inject constructor(
    private val listLahan: List<Lahan>,
    private val onItemCheckListener: OnItemCheckListener,
    private val listener: (Lahan) -> Unit
) : RecyclerView.Adapter<LahaniAdapter.ViewHolder>() {

    interface OnItemCheckListener {
        fun onHapus(lahan: Lahan)
        fun onEdit(lahan: Lahan)
        fun onNomor(lahan: Lahan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLahanBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listLahan[position], listener, onItemCheckListener)
    }

    override fun getItemCount(): Int = listLahan.size

    class ViewHolder(private val holder: ItemLahanBinding) : RecyclerView.ViewHolder(holder.root) {
        fun bind(
            lahan: Lahan,
            listener: (Lahan) -> Unit,
            onItemCheckListener: OnItemCheckListener
        ) {
            holder.tvLahan.text = lahan.kode
            holder.tvKeterangan.text =
                itemView.context.getString(R.string.total_luas, lahan.luas_lahan).plus(" m")

            itemView.setOnClickListener {
                listener(lahan)
            }

            holder.ivMore.setOnClickListener { v ->
                val popupMenu = PopupMenu(itemView.context, v)
                popupMenu.menuInflater.inflate(R.menu.lahan_nav, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menu ->
                    return@setOnMenuItemClickListener when (menu.itemId) {
                        R.id.nav_edit_no_lahan -> {
                            onItemCheckListener.onNomor(lahan)
                            true
                        }
                        R.id.nav_edit_lahan -> {
                            onItemCheckListener.onEdit(lahan)
                            true
                        }
                        R.id.nav_hapus_lahan -> {
                            onItemCheckListener.onHapus(lahan)
                            true
                        }
                        else -> true
                    }
                }
            }
        }
    }
}