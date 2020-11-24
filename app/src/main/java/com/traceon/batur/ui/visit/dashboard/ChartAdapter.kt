package com.traceon.batur.ui.visit.dashboard

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.traceon.batur.R
import com.traceon.batur.data.response.DashboardChart
import com.traceon.batur.databinding.ItemChartVisitBinding
import javax.inject.Inject

class ChartAdapter @Inject constructor(
    private val listData: List<DashboardChart>
) : RecyclerView.Adapter<ChartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemChartVisitBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    class ViewHolder(private val holder: ItemChartVisitBinding) :
        RecyclerView.ViewHolder(holder.root) {

        private lateinit var data: PieData
        private lateinit var dataSet: PieDataSet

        fun bind(dataDashboard: DashboardChart) {

            holder.tvKomoditas.text =
                itemView.context.getString(R.string.data_petani_komoditas, dataDashboard.komoditas)
            holder.tvTotal.text =
                itemView.context.getString(
                    R.string.jumlah_petani,
                    dataDashboard.jumlah_petani_total
                )
            holder.tvPersiapanDescription.text =
                ("(" + dataDashboard.detail_fase_komoditas[0].luas_lahan_fase + " Ha - " + dataDashboard.detail_fase_komoditas[0].jumlah_petani_fase).plus(
                    " "
                ).plus(
                    itemView.context.getString(
                        R.string.petani
                    )
                ).plus(")")

            holder.tvPembibitanDescription.text =
                ("(" + dataDashboard.detail_fase_komoditas[1].luas_lahan_fase + " Ha - " + dataDashboard.detail_fase_komoditas[1].jumlah_petani_fase).plus(
                    " "
                ).plus(
                    itemView.context.getString(
                        R.string.petani
                    )
                ).plus(")")

            holder.tvPemerliharaanDescription.text =
                ("(" + dataDashboard.detail_fase_komoditas[2].luas_lahan_fase + " Ha - " + dataDashboard.detail_fase_komoditas[2].jumlah_petani_fase).plus(
                    " "
                ).plus(
                    itemView.context.getString(
                        R.string.petani
                    )
                ).plus(")")

            holder.tvPanenDescription.text =
                ("(" + dataDashboard.detail_fase_komoditas[3].luas_lahan_fase + " Ha - " + dataDashboard.detail_fase_komoditas[3].jumlah_petani_fase).plus(
                    " "
                ).plus(
                    itemView.context.getString(
                        R.string.petani
                    )
                ).plus(")")

            holder.ivPersiapan.setBackgroundColor(Color.parseColor(dataDashboard.detail_fase_komoditas[0].color_hexa))
            holder.ivPembibitan.setBackgroundColor(Color.parseColor(dataDashboard.detail_fase_komoditas[1].color_hexa))
            holder.ivPemerliharaan.setBackgroundColor(Color.parseColor(dataDashboard.detail_fase_komoditas[2].color_hexa))
            holder.ivPanen.setBackgroundColor(Color.parseColor(dataDashboard.detail_fase_komoditas[3].color_hexa))

            holder.tvPersiapan.text = dataDashboard.detail_fase_komoditas[0].nama
            holder.tvPembibitan.text = dataDashboard.detail_fase_komoditas[1].nama
            holder.tvPemerliharaan.text = dataDashboard.detail_fase_komoditas[2].nama
            holder.tvPanen.text = dataDashboard.detail_fase_komoditas[3].nama

            val entries: ArrayList<PieEntry> = ArrayList()
            val colors: ArrayList<Int> = ArrayList()
            dataDashboard.detail_fase_komoditas.forEach { dataChart ->
                entries.add(PieEntry(dataChart.jumlah_petani_fase.toFloat()))
                colors.add(Color.parseColor(dataChart.color_hexa))
            }

            dataSet = PieDataSet(entries, dataDashboard.komoditas)
            dataSet.colors = colors

            dataSet.valueLinePart1OffsetPercentage = 60f
            dataSet.valueLinePart1Length = 0.2f
            dataSet.valueLinePart2Length = 0.4f
            dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

            data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.BLACK)

            holder.pieChart.setUsePercentValues(true)
            holder.pieChart.description.isEnabled = false
            holder.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
            holder.pieChart.dragDecelerationFrictionCoef = 0.95f
            holder.pieChart.isDrawHoleEnabled = true
            holder.pieChart.setHoleColor(Color.TRANSPARENT)
            holder.pieChart.holeRadius = 60f
            holder.pieChart.rotationAngle = 0f
            holder.pieChart.isRotationEnabled = true
            holder.pieChart.isHighlightPerTapEnabled = true
            holder.pieChart.animateY(1400, Easing.EaseOutQuad)
            holder.pieChart.legend.isEnabled = false
            holder.pieChart.data = data
            holder.pieChart.highlightValue(null)
            holder.pieChart.invalidate()
        }
    }
}