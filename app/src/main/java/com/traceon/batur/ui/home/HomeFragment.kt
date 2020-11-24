package com.traceon.batur.ui.home

import androidx.lifecycle.ViewModel
import com.traceon.batur.R
import com.traceon.batur.data.response.ResponseLogin
import com.traceon.batur.databinding.FragmentHomeBinding
import com.traceon.batur.ui.MainActivity
import com.traceon.batur.ui.base.BaseFragment
import com.traceon.batur.utils.Helper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, ViewModel>() {

    private var responseLogin: ResponseLogin? = null

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun init() {
        responseLogin = Helper.getSesiLogin(context ?: return)

        getDataBinding().tvName.text = responseLogin?.nama_lengkap
        getDataBinding().tvDescription.text = responseLogin?.client_name

        getDataBinding().clTopLeft.setOnClickListener {
            (activity as MainActivity).setMenu(1)
        }
        getDataBinding().clTopRight.setOnClickListener {
            (activity as MainActivity).setMenu(2)
        }
    }

}