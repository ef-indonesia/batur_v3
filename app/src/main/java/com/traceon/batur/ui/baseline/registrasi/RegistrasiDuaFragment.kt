package com.traceon.batur.ui.baseline.registrasi

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.tayfuncesur.stepper.Stepper
import com.thekhaeng.pushdownanim.PushDownAnim
import com.traceon.batur.R
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.FragmentRegister2Binding
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper

class RegistrasiDuaFragment : BaseFragment<FragmentRegister2Binding, ViewModel>() {
    private var responseLogin: ResponseLogin? = null

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_register_2

    @SuppressLint("ClickableViewAccessibility")
    override fun init() {
        setHasOptionsMenu(true)
        responseLogin = Helper.getSesiLogin(context ?: return)

        getDataBinding().tvManagementUnit.text = responseLogin?.manajemen_unit
        getDataBinding().tvArea.text = responseLogin?.area
        getDataBinding().tvDesa.text = responseLogin?.desa
        getDataBinding().etTanggalLahir.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    getDataBinding().etTanggalLahir.setText(Helper.getDate(context))
                }
            }
            true
        }


        PushDownAnim.setPushDownAnimTo(getDataBinding().btLanjutDua)
            .setScale(PushDownAnim.MODE_STATIC_DP, 5f)
            .setOnClickListener { setNextPage(it) }

        (activity as RegistrasiActivity).setTitle(getString(R.string.profil_petani))
    }

    private fun setNextPage(view: View?) {
        val nik = getDataBinding().etNik.text.toString()
        val nama = getDataBinding().etNama.text.toString()
        val alamat = getDataBinding().etAlamat.text.toString()

        if (nik.isBlank() || nama.isBlank() || alamat.isBlank()) {
            Helper.pesanBox(
                context ?: return,
                getString(R.string.error),
                getString(R.string.data_belum_lengkap)
            )
        } else {
            val bundle = Bundle()
            bundle.putString(AppConstant.ARG_NIK, nik)
            bundle.putString(AppConstant.ARG_NAMA, nama)
            bundle.putString(AppConstant.ARG_ALAMAT, alamat)
            view?.findNavController()?.navigate(R.id.step_personal, bundle)
            activity?.findViewById<Stepper>(R.id.stepper)?.forward()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.findViewById<Stepper>(R.id.stepper)?.back()
                activity?.onBackPressed()
                activity?.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
                true
            }
            else -> false
        }
    }

}