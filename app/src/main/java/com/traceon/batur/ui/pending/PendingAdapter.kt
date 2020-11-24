package com.traceon.batur.ui.pending

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.traceon.batur.data.model.Pending
import com.traceon.batur.databinding.ItemPendingBinding
import javax.inject.Inject

class PendingAdapter @Inject constructor(
    private val listPending: List<Pending>,
    private val onItemCheckListener: OnItemCheckListener,
    private val listener: (Pending) -> Unit
) : RecyclerView.Adapter<PendingAdapter.ViewHolder>() {

    interface OnItemCheckListener {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPendingBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPending[position], listener, onItemCheckListener)
    }

    override fun getItemCount(): Int = listPending.size

    class ViewHolder(private val holder: ItemPendingBinding) : RecyclerView.ViewHolder(holder.root) {
        fun bind(
            pending: Pending,
            listener: (Pending) -> Unit,
            onItemCheckListener: OnItemCheckListener
        ) {
            holder.tvLahan.text = pending.desc
            holder.tvKeterangan.text = ""

            itemView.setOnClickListener {
                listener(pending)
            }

//            holder.ivMore.setOnClickListener { v ->
//                val popupMenu = PopupMenu(itemView.context, v)
//                popupMenu.menuInflater.inflate(R.menu.lahan_nav, popupMenu.menu)
//                popupMenu.setOnMenuItemClickListener { menu ->
//                    return@setOnMenuItemClickListener when (menu.itemId) {
//                        R.id.nav_edit_no_lahan -> {
//                            onItemCheckListener.onNomor(lahan)
//                            true
//                        }
//                        R.id.nav_edit_lahan -> {
//                            onItemCheckListener.onEdit(lahan)
//                            true
//                        }
//                        R.id.nav_hapus_lahan -> {
//                            onItemCheckListener.onHapus(lahan)
//                            true
//                        }
//                        else -> true
//                    }
//                }
//            }
        }
    }
}