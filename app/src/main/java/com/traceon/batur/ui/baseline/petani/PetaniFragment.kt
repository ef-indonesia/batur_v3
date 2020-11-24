package com.traceon.batur.ui.baseline.petani

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModel
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.db.queryAll
import com.traceon.batur.data.model.Petani
import com.traceon.batur.databinding.FragmentPetaniBinding
import com.traceon.batur.ui.MainActivity
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.ui.baseline.pilih.AreaFragment
import com.traceon.batur.ui.baseline.lahan.LahanActivity
import com.traceon.batur.ui.baseline.registrasi.RegistrasiActivity
import com.traceon.batur.utils.AppConstant

class PetaniFragment : BaseFragment<FragmentPetaniBinding, ViewModel>() {
    private lateinit var skeletonScreen: SkeletonScreen
    private lateinit var adapter: PetaniAdapter
    private var listPetani: ArrayList<Petani> = ArrayList()

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_petani

    override fun init() {
        setHasOptionsMenu(true)

        adapter = PetaniAdapter(listPetani) { petani ->
            val i = Intent(context, LahanActivity::class.java)
            i.putExtra(AppConstant.PETANI, petani)
            activity?.startActivity(i)
            activity?.overridePendingTransition(
                R.anim.enter,
                R.anim.exit
            )
        }

        skeletonScreen = Skeleton.bind(getDataBinding().rvPetani)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_petani_skeleton)
            .show()

        try {
            loadData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getDataBinding().fabPetani.setOnClickListener {
            val i = Intent(context, RegistrasiActivity::class.java)
            startActivity(i)
        }

        getDataBinding().slPetani.setOnRefreshListener {
            loadData()
        }

        (activity as MainActivity).setTitleActionBar(getString(R.string.daftar_petani))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.top_nav, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_baseline -> {
                (activity as MainActivity).loadFragment(AreaFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        Log.d("PETANI", "" + listPetani[0].nama)
        if (listPetani.size > 0) {
            skeletonScreen.hide()
            getDataBinding().slPetani.isRefreshing = false
        }
        getDataBinding().tvJmlPetani.text = listPetani.size.toString()
        getDataBinding().tvBelumTerdata.text = blmTerdata.toString()
    }

}