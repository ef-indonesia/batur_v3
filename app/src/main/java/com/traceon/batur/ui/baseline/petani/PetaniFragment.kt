package com.traceon.batur.ui.baseline.petani

import androidx.lifecycle.ViewModel
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.adapter.PetaniAdapter
import com.traceon.batur.data.model.Petani
import com.traceon.batur.databinding.FragmentPetaniBinding
import com.traceon.batur.ui.base.BaseFragment
import com.vicpin.krealmextensions.queryAll

class PetaniFragment : BaseFragment<FragmentPetaniBinding, ViewModel>() {
    private lateinit var skeletonScreen: SkeletonScreen
    private lateinit var adapter: PetaniAdapter
    private var listPetani: ArrayList<Petani> = ArrayList()

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_petani

    override fun init() {
        setHasOptionsMenu(true)

        adapter = PetaniAdapter(listPetani) {

        }
        skeletonScreen = Skeleton.bind(getDataBinding().rvPetani)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_farmer_skeleton)
            .show()

        try {
            loadData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadData() {
        listPetani.clear()
        listPetani.addAll(Petani().queryAll())
        adapter.notifyDataSetChanged()

        var blmTerdata = 0
        listPetani.forEach { p ->
            if (p.lahan_total == 0) blmTerdata++
        }

        getDataBinding().tvJmlPetani.text = listPetani.size.toString()
        getDataBinding().tvBelumTerdata.text = blmTerdata.toString()
    }

}