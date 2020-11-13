package com.traceon.batur.ui.baseline

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bytcode.lib.spinner.multiselectspinner.data.KeyPairBoolData
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.google.gson.Gson
import com.traceon.batur.R
import com.traceon.batur.data.adapter.BaselineAdapter
import com.traceon.batur.data.model.*
import com.traceon.batur.data.repo.RemoteRepository
import com.traceon.batur.data.response.*
import com.traceon.batur.databinding.FragmentBaselineBinding
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.saveAll
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable

class BaselineFragment : BaseFragment<FragmentBaselineBinding, ViewModel>() {
    private lateinit var repo: RemoteRepository
    private var responseLogin: ResponseLogin? = null
    private lateinit var dialog: SweetAlertDialog
    private lateinit var idDesa: String
    private lateinit var mDesa: String
    private lateinit var mMu: String
    private lateinit var mArea: String
    private lateinit var skeletonScreen: SkeletonScreen
    private lateinit var adapter: BaselineAdapter
    private lateinit var disposable: Disposable
    private var listFarmer: ArrayList<Farmer> = ArrayList()
    private var listPetani: ArrayList<Petani> = ArrayList()
    private var listReferral: ArrayList<Referral> = ArrayList()
    private var listKomoditas: ArrayList<TempKomoditas> = ArrayList()
    private var listLahan: ArrayList<Lahan> = ArrayList()
    private var listBaseline: ArrayList<Baseline> = ArrayList()
    private var listPilihan: ArrayList<Petani> = ArrayList()

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

        adapter = BaselineAdapter(listFarmer, object : BaselineAdapter.OnItemCheckListener {
            override fun onItemCheck(item: Farmer) {
                hitungTotal()
            }

            override fun onItemUnCheck(item: Farmer) {
                hitungTotal()
            }
        })

        skeletonScreen = Skeleton.bind(getDataBinding().rvBaseline)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_farmer_skeleton)
            .show()

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
                        startActivity(i)
                        activity?.overridePendingTransition(
                            R.anim.enter,
                            R.anim.exit
                        )
                    }
                    .show()
            }
        }

        getDataBinding().slBaseline.setOnRefreshListener {
            loadPetani(idDesa)
        }

        getDataBinding().swImage.setOnCheckedChangeListener { _, isChecked ->
            listFarmer.forEach { f ->
                if (isChecked) {
                    f.dipilih = isChecked
                    getDataBinding().cbAll.isChecked = isChecked
                }
                f.foto = isChecked
            }
            hitungTotal()
            adapter.notifyDataSetChanged()
        }

        getDataBinding().cbAll.setOnCheckedChangeListener { _, isChecked ->
            listFarmer.forEach { f ->
                if (!isChecked) {
                    f.foto = isChecked
                    getDataBinding().swImage.isChecked = isChecked
                }
                f.dipilih = isChecked
            }
            hitungTotal()
            adapter.notifyDataSetChanged()
        }

        getDataBinding().btSynchronize.setOnClickListener {
            onSinkronisai()
        }

        getDataBinding().ivMode.setOnClickListener { v ->
            v.visibility = View.GONE
            v.setBackgroundResource(R.drawable.ic_arrow_up)
            getDataBinding().clChoice.visibility = View.VISIBLE
        }
    }

    private fun onSinkronisai() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val idPilihan = ArrayList<String>()

        listFarmer.forEach { f ->
            val petani = listPetani[listFarmer.indexOf(f)]
            if (f.dipilih) listPilihan.add(petani)
        }

        listPilihan.forEach { p ->
            idPilihan.add(p.ID.toString())
        }

        Referral().deleteAll()
        Lahan().deleteAll()
        Baseline().deleteAll()
        Komoditas().deleteAll()

        getTerpilih(idPilihan.joinToString())
    }

    private fun getTerpilih(idPilihan: String) {
        dialog.show()
        disposable = Observable.zip(
            repo.getReferral(responseLogin?.database.toString(), responseLogin?.user_ID.toString()),
            repo.getKomoditas(responseLogin?.database.toString(), null),
            repo.getLahan(responseLogin?.database.toString(), idPilihan),
            repo.getBaseline(responseLogin?.database, idPilihan),
            { t1, t2, t3, t4 ->
                listReferral.addAll(t1)
                listKomoditas.addAll(t2)
                listLahan.addAll(t3)
                listBaseline.addAll(t4)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { }
            .doOnTerminate { }
            .subscribe(
                { response ->
                    if (response) {
                        setReferral(listReferral)
                        setKomoditas(listKomoditas)
                        setLahan(listLahan)
                        setBaseline(listBaseline)
                    }
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    onFinish()
                }
            )
    }

    private fun setBaseline(data: ArrayList<Baseline>) {
        data.forEach { baseline ->
            listOf(baseline).saveAll()
        }
    }

    private fun setLahan(data: ArrayList<Lahan>) {
        data.forEach { lahan ->
            listOf(lahan).saveAll()
        }
    }

    private fun setKomoditas(data: ArrayList<TempKomoditas>) {
        data.forEach { komo ->
            val komoditas = Komoditas()
            komoditas.ID = komo.ID
            komoditas.nama = komo.nama
            komoditas.kode_satuan = komo.kode_satuan
            komoditas.satuan = Gson().toJson(komo.satuan)
            listOf(komoditas).saveAll()
        }
    }

    private fun setReferral(data: ArrayList<Referral>) {
        data.forEach { referral ->
            listOf(referral).saveAll()
        }
    }

    @SuppressLint("CheckResult")
    private fun loadPetani(desaId: String) {
        skeletonScreen.show()
        dialog.show()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        repo.getPetani(responseLogin?.database.toString(), desaId, null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    setPetani(response)
                    skeletonScreen.hide()
                },
                { t ->
                    t.printStackTrace()
                },
                {
                    dialog.dismiss()
                    getDataBinding().slBaseline.isRefreshing = false
                    getDataBinding().clPetani.visibility = View.VISIBLE
                    getDataBinding().clChoice.visibility = View.GONE
                    getDataBinding().ivMode.visibility = View.VISIBLE
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            )
    }

    private fun setPetani(response: ResponsePetani?) {
        var sizeAll = 0f
        response?.forEach { p ->
            listFarmer.add(
                Farmer(
                    p.ID.toString(),
                    p.nama.toString(),
                    p.alamat.toString(),
                    p.ukuran_text,
                    p.ukuran_total,
                    dipilih = false,
                    foto = false,
                    lahanTotal = response.indexOf(p).plus(1).toString(),
                    lahanKunci = response.indexOf(p).toString()
                )
            )
            listPetani.add(p)
            sizeAll += p.ukuran_total?.toFloat() ?: 0f
        }
        adapter.notifyDataSetChanged()
        getDataBinding().tvTotal.text = Helper.formatCapacity(sizeAll.toDouble(), 2)
        getDataBinding().tvMemory.text = Helper.spaceDisk(context)
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
        getDataBinding().btSubmitBaseline.isEnabled = false
        getDataBinding().tvDesa.visibility = View.GONE
        getDataBinding().spDesa.isEnabled = true
        getDataBinding().spDesa.setItems(dataDesa, -1) { items ->
            items.forEach { keyPairBoolData ->
                if (keyPairBoolData?.isSelected == true) {
                    idDesa = keyPairBoolData.id.toString()
                    mDesa = keyPairBoolData.name
                    getDataBinding().btSubmitBaseline.isEnabled = true
                }
            }
        }
    }

    private fun hitungTotal() {
        var total = 0.0
        listFarmer.forEach { f ->
            if (f.dipilih) {
                total += if (f.foto) {
                    f.ukuranTotal.toString().toDouble()
                } else {
                    f.ukuranText.toString().toDouble()
                }
            }
        }
        getDataBinding().tvTerpilih.text = Helper.formatCapacity(total, 2)
    }

    private fun onFinish() {
        disposable.dispose()
        dialog.dismiss()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Helper.saveBool(AppConstant.BASELINE, true, context)
        Toast.makeText(context, getString(R.string.synchronize), Toast.LENGTH_SHORT).show()
    }

}