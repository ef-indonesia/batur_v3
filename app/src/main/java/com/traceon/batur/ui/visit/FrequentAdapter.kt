package com.traceon.batur.ui.visit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.traceon.batur.R
import com.traceon.batur.data.model.TempFrequent
import com.traceon.batur.databinding.ItemFrekuensiBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class FrequentAdapter @Inject constructor(
    private val listFrequent: List<TempFrequent>,
    private val listener: (TempFrequent) -> Unit
) : RecyclerView.Adapter<FrequentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFrekuensiBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFrequent[position], listener)
    }

    override fun getItemCount(): Int = listFrequent.size

    class ViewHolder(private val holder: ItemFrekuensiBinding) :
        RecyclerView.ViewHolder(holder.root) {

        fun bind(tempFrequent: TempFrequent, listener: (TempFrequent) -> Unit) {
            holder.tvFrequent.text = tempFrequent.no
            holder.tvJmlFrequent.text =
                itemView.context.getString(R.string.jumlah_petani, tempFrequent.poin)
            itemView.setOnClickListener {
                listener(tempFrequent)
            }
        }
    }
}