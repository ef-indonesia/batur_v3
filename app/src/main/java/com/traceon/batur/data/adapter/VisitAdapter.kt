package com.traceon.batur.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.traceon.batur.R
import com.traceon.batur.data.model.Visit
import com.traceon.batur.databinding.ItemVisitBinding
import com.traceon.batur.ui.visit.VisitViewModel
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class VisitAdapter @Inject constructor(
    private val visitList: List<Visit>,
    private val viewModel: VisitViewModel
) : RecyclerView.Adapter<VisitAdapter.VisitViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VisitViewHolder {
        return VisitViewHolder(
            ItemVisitBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: VisitViewHolder, position: Int) {
        holder.bind(visitList[position], viewModel)
    }

    override fun getItemCount(): Int = visitList.size

    class VisitViewHolder(private val holder: ItemVisitBinding) :
        RecyclerView.ViewHolder(holder.root) {

        fun bind(visit: Visit, viewModel: VisitViewModel) {
            holder.tvPetani.text = visit.petani
            holder.tvNoLahan.text = "Nomor Lahan : ".plus(visit.kode_lahan)
            holder.tvKomoditas.text = "Komoditas : ".plus(visit.komoditas)
            holder.tvNo.text = visit.no_urut.toString()
            holder.clVisit.setBackgroundColor(holder.root.context.getColor(visit.warna))
        }
    }

}