package com.traceon.batur.ui.baseline.registrasi

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.tayfuncesur.stepper.Stepper
import com.thekhaeng.pushdownanim.PushDownAnim
import com.traceon.batur.R
import com.traceon.batur.databinding.FragmentRegister3Binding
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.utils.AppConstant

class RegistrasiTigaFragment : BaseFragment<FragmentRegister3Binding, ViewModel>() {
    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_register_3

    override fun init() {
        setHasOptionsMenu(true)

        PushDownAnim.setPushDownAnimTo(getDataBinding().btLanjutTiga)
            .setScale(PushDownAnim.MODE_STATIC_DP, 5f)
            .setOnClickListener { setNextPage(it) }

        (activity as RegistrasiActivity).setTitle(getString(R.string.profil_petani))
    }

    private fun setNextPage(view: View?) {
        val pendapatan = getDataBinding().etPendapatan.text.toString()
        val suku = getDataBinding().etSuku.text.toString()
        val kelompokTani = getDataBinding().etKelompokTani.text.toString()
        val posisiKelompok = getDataBinding().etPosisiKelompok.text.toString()
        val pendidikan = getDataBinding().etPendidikanTerakhir.text.toString()
        val pendidikanNonFormal = getDataBinding().etPendidikanNonFormal.text.toString()
        val email = getDataBinding().etEmail.text.toString()

        val bundle = Bundle()
        bundle.putString(AppConstant.ARG_PENDAPATAN, pendapatan)
        bundle.putString(AppConstant.ARG_SUKU, suku)
        bundle.putString(AppConstant.ARG_KELOMPOK_TANI, kelompokTani)
        bundle.putString(AppConstant.ARG_POSISI_KELOMPOK, posisiKelompok)
        bundle.putString(AppConstant.ARG_PENDIDIKAN, pendidikan)
        bundle.putString(AppConstant.ARG_PENDIDIKAN_NON_FORMAL, pendidikanNonFormal)
        bundle.putString(AppConstant.ARG_EMAIL, email)

        view?.findNavController()?.navigate(R.id.step_dokumen, bundle)
        activity?.findViewById<Stepper>(R.id.stepper)?.forward()
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