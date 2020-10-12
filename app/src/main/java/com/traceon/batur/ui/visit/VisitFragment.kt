package com.traceon.batur.ui.visit

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.SingleSpinnerListener
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.traceon.batur.R
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.ResponseArea
import com.traceon.batur.data.response.ResponseDesa
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.data.response.ResponseManagementUnit
import com.traceon.batur.databinding.FragmentVisitBinding
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.utils.Helper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitFragment : BaseFragment<FragmentVisitBinding, VisitViewModel>() {
    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private lateinit var dialog: SweetAlertDialog

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_visit

    @SuppressLint("CheckResult")
    override fun init() {
        repo = RemoteRepository()
        responseLogin = Helper.getSesiLogin(context ?: return)
        dialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = getString(R.string.loading)
        dialog.setCancelable(false)

        getDataBinding().spDesa.isEnabled = false
        getDataBinding().spArea.isEnabled = false
        getDataBinding().spMu.isEnabled = false

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
        getDataBinding().fabVisit.setOnClickListener {
            val i = Intent(context, VisitActivity::class.java)
            startActivity(i)
            activity?.overridePendingTransition(
                R.anim.enter,
                R.anim.exit
            )
        }
    }

    private fun getManagementUnit() {
        dialog.show()
        repo.getManagementUnit(responseLogin?.database ?: return, responseLogin?.ID.toString())
            .enqueue(object : Callback<List<ResponseManagementUnit>> {
                override fun onResponse(
                    call: Call<List<ResponseManagementUnit>>,
                    response: Response<List<ResponseManagementUnit>>
                ) {
                    response.let { res ->
                        if (res.isSuccessful) {
                            setManagementUnit(res.body())
                            dialog.dismiss()
                        }
                    }
                }

                override fun onFailure(call: Call<List<ResponseManagementUnit>>, t: Throwable) {
                    dialog.dismiss()
                    t.printStackTrace()
                }

            })
    }

    private fun setManagementUnit(managementUnit: List<ResponseManagementUnit>?) {
        val dataManagementUnit: ArrayList<KeyPairBoolData> = ArrayList()
        managementUnit?.forEach { mu ->
            val h = KeyPairBoolData()
            h.id = mu.iD?.toLong() ?: return
            h.name = mu.nama
            h.isSelected = false
            dataManagementUnit.add(h)
        }
        getDataBinding().tvMu.visibility = View.GONE
        getDataBinding().spMu.isEnabled = true
        getDataBinding().spMu.setItems(dataManagementUnit, object : SingleSpinnerListener {
            override fun onItemsSelected(selectedItem: KeyPairBoolData?) {
                if (selectedItem?.isSelected == true) getArea(selectedItem.id.toString())
            }

            override fun onClear() {

            }

        })
    }

    private fun getArea(id: String) {
        dialog.show()
        repo.getArea(responseLogin?.database ?: return, id, responseLogin?.ID.toString())
            .enqueue(object : Callback<List<ResponseArea>> {
                override fun onResponse(
                    call: Call<List<ResponseArea>>,
                    response: Response<List<ResponseArea>>
                ) {
                    response.let { res ->
                        if (res.isSuccessful) {
                            setArea(res.body())
                            dialog.dismiss()
                        }
                    }
                }

                override fun onFailure(call: Call<List<ResponseArea>>, t: Throwable) {
                    t.printStackTrace()
                    dialog.dismiss()
                }
            })
    }

    private fun setArea(area: List<ResponseArea>?) {
        val dataArea: ArrayList<KeyPairBoolData> = ArrayList()
        area?.forEach { ar ->
            val h = KeyPairBoolData()
            h.id = ar.iD?.toLong() ?: return
            h.name = ar.nama
            h.isSelected = false
            dataArea.add(h)
        }
        getDataBinding().tvArea.visibility = View.GONE
        getDataBinding().spArea.isEnabled = true
        getDataBinding().spArea.setItems(dataArea, object : SingleSpinnerListener {
            override fun onItemsSelected(selectedItem: KeyPairBoolData?) {
                if (selectedItem?.isSelected == true) getDesa(selectedItem.id.toString())
            }

            override fun onClear() {
//                getDataBinding().spDesa.set
            }

        })
    }

    private fun getDesa(id: String) {
        dialog.show()
        repo.getDesa(responseLogin?.database ?: return, id, responseLogin?.ID.toString())
            .enqueue(object : Callback<List<ResponseDesa>> {
                override fun onResponse(
                    call: Call<List<ResponseDesa>>,
                    response: Response<List<ResponseDesa>>
                ) {
                    response.let { res ->
                        if (res.isSuccessful) setDesa(res.body())
                        dialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<List<ResponseDesa>>, t: Throwable) {
                    t.printStackTrace()
                    dialog.dismiss()
                }

            })
    }

    private fun setDesa(desa: List<ResponseDesa>?) {
        val dataDesa: ArrayList<KeyPairBoolData> = ArrayList()
        desa?.forEach { ds ->
            val h = KeyPairBoolData()
            h.id = desa.indexOf(ds).toLong()
            h.name = ds.kelurahan
            h.isSelected = false
            dataDesa.add(h)
        }
        getDataBinding().tvDesa.visibility = View.GONE
        getDataBinding().spDesa.isEnabled = true
        getDataBinding().spDesa.setLimit(2) {
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.error))
                .setContentText("Anda hanya boleh memilih maximal 2 desa")
                .show()
        }
        getDataBinding().spDesa.setItems(dataDesa) { items ->
            items.forEach { ds ->
                if (ds.isSelected) getDataBinding().fabVisit.visibility = View.VISIBLE
            }
        }
    }

}