package com.traceon.batur.ui.baseline.pilih

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bytcode.lib.spinner.multiselectspinner.data.KeyPairBoolData
import com.traceon.batur.R
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.*
import com.traceon.batur.databinding.FragmentAreaBinding
import com.traceon.batur.ui.MainActivity
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AreaFragment : BaseFragment<FragmentAreaBinding, ViewModel>() {
    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private lateinit var idDesa: String
    private lateinit var mDesa: String
    private lateinit var mMu: String
    private lateinit var mArea: String
    private lateinit var mKode: String
    private lateinit var progressDialog: ProgressDialog

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_area

    @SuppressLint("CheckResult")
    override fun init() {
        repo = RemoteRepository()
        responseLogin = Helper.getSesiLogin(context ?: return)
        getDataBinding().spDesa.isEnabled = false
        getDataBinding().spArea.isEnabled = false
        getDataBinding().spMu.isEnabled = false

        progressDialog = ProgressDialog(context)

        try {
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

        getDataBinding().btSubmitBaseline.setOnClickListener { v ->
            if (v.isEnabled) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.konfirmasi))
                    .setContentText(getString(R.string.desa_terpilih, mDesa))
                    .setConfirmText(getString(R.string.ya))
                    .setCancelText(getString(R.string.tidak))
                    .setConfirmClickListener {
                        it.dismissWithAnimation()
                        val i = Intent(context, PilihActivity::class.java)
                        i.putExtra(AppConstant.ID_DESA, idDesa)
                        i.putExtra(AppConstant.NAMA_DESA, mDesa)
                        i.putExtra(AppConstant.KODE, mKode)
                        startActivityForResult(i, 1)
                        activity?.overridePendingTransition(
                            R.anim.enter,
                            R.anim.exit
                        )
                    }
                    .show()
            }
        }
        (activity as MainActivity).setTitleActionBar(getString(R.string.pilih_desa))
    }

    @SuppressLint("CheckResult")
    private fun getManagementUnit() {
        Helper.showProgressDialogWithTitle(progressDialog, null, getString(R.string.loading))
        repo.getManagementUnit(responseLogin?.database.toString(), responseLogin?.ID.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setManagementUnit(response)
                },
                { t ->
                    Helper.hideProgressDialogWithTitle(progressDialog)
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
                    Helper.hideProgressDialogWithTitle(progressDialog)
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
        Helper.showProgressDialogWithTitle(progressDialog, null, getString(R.string.loading))
        repo.getArea(responseLogin?.database.toString(), id, responseLogin?.ID.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setArea(response)
                },
                { t ->
                    Helper.hideProgressDialogWithTitle(progressDialog)
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
                    Helper.hideProgressDialogWithTitle(progressDialog)
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
        Helper.showProgressDialogWithTitle(progressDialog, null, getString(R.string.loading))
        repo.getDesa(responseLogin?.database.toString(), id, responseLogin?.ID.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setDesa(response)
                },
                { t ->
                    Helper.hideProgressDialogWithTitle(progressDialog)
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
                    Helper.hideProgressDialogWithTitle(progressDialog)
                }
            )
    }

    private fun setDesa(desa: ResponseDesa) {
        val dataDesa: ArrayList<KeyPairBoolData> = ArrayList()
        desa.forEach { ds ->
            val h = KeyPairBoolData()
            h.id = desa.indexOf(ds).toLong()
            h.name = ds.kelurahan
            h.isSelected = false
            dataDesa.add(h)
        }
        getDataBinding().btSubmitBaseline.isEnabled = false
        getDataBinding().tvDesa.visibility = View.GONE
        getDataBinding().spDesa.isEnabled = true
        getDataBinding().spDesa.setItems(dataDesa, -1) { items ->
            items.forEach { keyPairBoolData ->
                if (keyPairBoolData?.isSelected == true) {
                    idDesa = desa[keyPairBoolData.id.toInt()].ID
                    mDesa = keyPairBoolData.name
                    mKode = desa[keyPairBoolData.id.toInt()].kode
                    getDataBinding().btSubmitBaseline.isEnabled = true
                }
            }
        }
    }

}