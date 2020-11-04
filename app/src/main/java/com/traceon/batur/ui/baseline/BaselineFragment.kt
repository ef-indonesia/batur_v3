package com.traceon.batur.ui.baseline

import android.annotation.SuppressLint
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bytcode.lib.spinner.multiselectspinner.data.KeyPairBoolData
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.ResponseArea
import com.traceon.batur.data.response.ResponseDesa
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.data.response.ResponseManagementUnit
import com.traceon.batur.databinding.FragmentBaselineBinding
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.utils.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BaselineFragment : BaseFragment<FragmentBaselineBinding, BaselineViewModel>() {
    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private lateinit var dialog: SweetAlertDialog
    private lateinit var idDesa: String
    private lateinit var mDesa: String
    private lateinit var mMu: String
    private lateinit var mArea: String
    private lateinit var skeletonScreen: SkeletonScreen

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_baseline

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

        getDataBinding().btSubmitBaseline.setOnClickListener {v->
            if (v.isEnabled){

            }
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

    private fun setManagementUnit(managementUnit: List<ResponseManagementUnit>?) {
        val dataManagementUnit: ArrayList<KeyPairBoolData> = ArrayList()
        managementUnit?.forEach { mu ->
            val h = KeyPairBoolData()
            h.id = mu.iD.toString().toLong()
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

    private fun setArea(area: List<ResponseArea>?) {
        val dataArea: ArrayList<KeyPairBoolData> = ArrayList()
        area?.forEach { ar ->
            val h = KeyPairBoolData()
            h.id = ar.iD.toString().toLong()
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

    private fun setDesa(desa: List<ResponseDesa>?) {
        val dataDesa: ArrayList<KeyPairBoolData> = ArrayList()
        desa?.forEach { ds ->
            val h = KeyPairBoolData()
            h.id = ds.iD.toString().toLong()
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
                    getDataBinding().btSubmitBaseline.isEnabled = true
                } else {
                    getDataBinding().btSubmitBaseline.isEnabled = false
                }
            }
        }
    }

}