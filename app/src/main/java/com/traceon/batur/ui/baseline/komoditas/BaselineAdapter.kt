package com.traceon.batur.ui.baseline.komoditas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.traceon.batur.R
import com.traceon.batur.data.model.Baseline
import com.traceon.batur.databinding.ItemBaselineBinding
import javax.inject.Inject

class BaselineAdapter @Inject constructor(
    private val listBaseline: List<Baseline>,
    private val listener: (Baseline) -> Unit
) : RecyclerView.Adapter<BaselineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBaselineBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listBaseline[position], listener)
    }

    override fun getItemCount(): Int = listBaseline.size

    class ViewHolder(private val holder: ItemBaselineBinding) :
        RecyclerView.ViewHolder(holder.root) {

        fun bind(baseline: Baseline, listener: (Baseline) -> Unit) {
            holder.tvKomoditas.text = baseline.komoditas
            holder.tvKeterangan.text =
                itemView.context.getString(R.string.total_luas, baseline.luas_lahan).plus(" m")
            itemView.setOnClickListener {
                listener(baseline)
            }
        }
    }
}