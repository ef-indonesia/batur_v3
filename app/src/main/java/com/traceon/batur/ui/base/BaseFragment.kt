package com.traceon.batur.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<T : ViewDataBinding, V : ViewModel> : Fragment() {

    val NO_VIEW_MODEL_BINDING_VARIABLE = -1
    private lateinit var vModel: V
    private lateinit var vdBinding: T

    abstract fun getViewModelBindingVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun getViewModel(): V = vModel
    fun getDataBinding(): T = vdBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vdBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return vdBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        performDataBinding()
    }

    abstract fun init()

    private fun performDataBinding() {
        vdBinding.lifecycleOwner = viewLifecycleOwner
        if (getViewModelBindingVariable() != NO_VIEW_MODEL_BINDING_VARIABLE) {
            vdBinding.setVariable(getViewModelBindingVariable(), vModel)
            vdBinding.executePendingBindings()
        }
        init()
    }
}