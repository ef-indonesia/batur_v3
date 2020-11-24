package com.traceon.batur.ui.visit.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bytcode.lib.spinner.multiselectspinner.data.KeyPairBoolData
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.*
import com.traceon.batur.databinding.FragmentVisitBinding
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.ui.visit.pilih.VisitActivity
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class VisitFragment : BaseFragment<FragmentVisitBinding, ViewModel>() {
    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private lateinit var dialog: SweetAlertDialog
    private lateinit var idDesa: String
    private lateinit var mDesa: String
    private lateinit var mMu: String
    private lateinit var mArea: String
    private lateinit var adapter: ChartAdapter
    private var listDashboard: ArrayList<DashboardChart> = ArrayList()
    private lateinit var skeletonScreen: SkeletonScreen

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_visit

    @SuppressLint("CheckResult")
    override fun init() {
        repo = RemoteRepository()
        responseLogin = Helper.getSesiLogin(context ?: return)
        dialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = getString(R.string.loading)
        dialog.setCancelable(false)

        adapter = ChartAdapter(listDashboard)

        getDataBinding().spDesa.isEnabled = false
        getDataBinding().spArea.isEnabled = false
        getDataBinding().spMu.isEnabled = false

        skeletonScreen = Skeleton.bind(getDataBinding().rvChartVisit)
            .adapter(adapter)
            .color(R.color.light_transparent)
            .load(R.layout.item_chart_visit_skeleton)
            .show()

        try {
            dialog.show()
            Helper.hasInternetConnection().subscribe { hasInternet ->
                if (hasInternet) {
                    getManagementUnit()
                } else {
                    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getString(R.string.error))
                        .setContentText(getString(R.string.cek_koneksi))
                        .setContentText(getString(R.string.coba_lagi))
                        .setConfirmClickListener {
                            it.dismiss()
                            getManagementUnit()
                        }
                        .show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        getDataBinding().btVisit.setOnClickListener {
            val i = Intent(context, VisitActivity::class.java)
            i.putExtra(AppConstant.ID_DESA, idDesa)
            i.putExtra(AppConstant.NAMA_DESA, mDesa)
            i.putExtra(AppConstant.MU, mMu)
            i.putExtra(AppConstant.AREA, mArea)
            startActivity(i)
            activity?.overridePendingTransition(
                R.anim.enter,
                R.anim.exit
            )
        }

        getDataBinding().slChart.setOnRefreshListener {
            getDashboard(idDesa)
        }
    }

    @SuppressLint("CheckResult")
    private fun getManagementUnit() {
        dialog.show()
        repo.getManagementUnit(responseLogin?.database.toString(), responseLogin?.ID.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setManagementUnit(response)
                },
                { t ->
                    dialog.dismiss()
                    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getString(R.string.error))
                        .setContentText(getString(R.string.gagal_ambil_data))
                        .setConfirmText(getString(R.string.ya))
                        .setCancelText(getString(R.string.tidak))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                            getManagementUnit()
                        }
                        .show()
                    t.printStackTrace()
                }, {
                    dialog.dismiss()
                }
            )
    }

    private fun setManagementUnit(managementUnit: ResponseManagementUnit) {
        val dataManagementUnit: ArrayList<KeyPairBoolData> = ArrayList()
        managementUnit.forEach { mu ->
            val h = KeyPairBoolData()
            h.id = mu.ID.toString().toLong()
            h.name = mu.nama
            h.isSelected = false
            dataManagementUnit.add(h)
        }
        getDataBinding().tvMu.visibility = View.GONE
        getDataBinding().spMu.isEnabled = true
        getDataBinding().spMu.setItems(dataManagementUnit, -1) { items ->
            items.forEach { keyPairBoolData ->
                if (keyPairBoolData?.isSelected == true) {
                    getArea(keyPairBoolData.id.toString())
                    mMu = keyPairBoolData.name.toString()
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getArea(id: String) {
        dialog.show()
        repo.getArea(responseLogin?.database.toString(), id, responseLogin?.ID.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setArea(response)
                },
                { t ->
                    dialog.dismiss()
                    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getString(R.string.error))
                        .setContentText(getString(R.string.gagal_ambil_data))
                        .setConfirmText(getString(R.string.ya))
                        .setCancelText(getString(R.string.tidak))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()
                    t.printStackTrace()
                },
                {
                    dialog.dismiss()
                }
            )
    }

    private fun setArea(area: ResponseArea) {
        val dataArea: ArrayList<KeyPairBoolData> = ArrayList()
        area.forEach { ar ->
            val h = KeyPairBoolData()
            h.id = ar.ID.toString().toLong()
            h.name = ar.nama
            h.isSelected = false
            dataArea.add(h)
        }
        getDataBinding().tvArea.visibility = View.GONE
        getDataBinding().spArea.isEnabled = true
        getDataBinding().spArea.setItems(dataArea, -1) { items ->
            items.forEach { keyPairBoolData ->
                if (keyPairBoolData?.isSelected == true) {
                    getDesa(keyPairBoolData.id.toString())
                    mArea = keyPairBoolData.name.toString()
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getDesa(id: String) {
        dialog.show()
        repo.getDesa(responseLogin?.database.toString(), id, responseLogin?.ID.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setDesa(response)
                },
                { t ->
                    dialog.dismiss()
                    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getString(R.string.error))
                        .setContentText(getString(R.string.gagal_ambil_data))
                        .setConfirmText(getString(R.string.ya))
                        .setCancelText(getString(R.string.tidak))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()
                    t.printStackTrace()
                },
                {
                    dialog.dismiss()
                }
            )
    }

    private fun setDesa(desa: ResponseDesa) {
        val dataDesa: ArrayList<KeyPairBoolData> = ArrayList()
        desa.forEach { ds ->
            val h = KeyPairBoolData()
            h.id = ds.ID.toString().toLong()
            h.name = ds.kelurahan
            h.isSelected = false
            dataDesa.add(h)
        }
        getDataBinding().tvDesa.visibility = View.GONE
        getDataBinding().spDesa.isEnabled = true
        getDataBinding().spDesa.setItems(dataDesa, -1) { items ->
            items.forEach { keyPairBoolData ->
                if (keyPairBoolData?.isSelected == true) {
                    idDesa = keyPairBoolData.id.toString()
                    mDesa = keyPairBoolData.name
                    getDataBinding().btVisit.visibility = View.VISIBLE
                    getDataBinding().btRiwayat.visibility = View.VISIBLE
                    getDataBinding().slChart.visibility = View.VISIBLE
                    getDashboard(idDesa)
                } else {
                    getDataBinding().btVisit.visibility = View.GONE
                    getDataBinding().btRiwayat.visibility = View.GONE
                    getDataBinding().slChart.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getDashboard(desa_id: String) {
        repo.showDashboardVisit(responseLogin?.database.toString(), desa_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setDashboard(response)
                },
                { t ->
                    dialog.dismiss()
                    getDataBinding().slChart.isRefreshing = false
                    t.printStackTrace()
                },
                {
                    getDataBinding().slChart.isRefreshing = false
                    dialog.dismiss()
                }
            )
    }

    private fun setDashboard(response: ResponseDashboardVisit?) {
        listDashboard.clear()
        if (response?.result == true) {
            listDashboard.addAll(response.data.list_detail_komoditas)
            getDataBinding().empty.visibility = View.GONE
        } else {
            getDataBinding().empty.visibility = View.VISIBLE
        }
        adapter.notifyDataSetChanged()
        skeletonScreen.hide()
    }
}