package com.traceon.batur.ui.baseline.registrasi

import android.view.MenuItem
import androidx.lifecycle.ViewModel
import com.tayfuncesur.stepper.Stepper
import com.traceon.batur.R
import com.traceon.batur.databinding.FragmentRegister4Binding
import com.traceon.batur.ui.base.BaseFragment

class RegistrasiEmpatFragment : BaseFragment<FragmentRegister4Binding, ViewModel>() {
    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_register_4

    override fun init() {
        setHasOptionsMenu(true)

        (activity as RegistrasiActivity).setTitle(getString(R.string.data_rekening))
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