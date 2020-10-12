package com.traceon.batur.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<T : ViewDataBinding, V : ViewModel> : AppCompatActivity() {
    val NO_VIEW_MODEL_BINDING_VARIABLE = -1
    private lateinit var vModel: V
    private lateinit var vdBinding: T

    abstract fun getViewModelBindingVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun getViewModel(): V = vModel
    fun getDataBinding(): T = vdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performDataBinding()
    }

    private fun performDataBinding() {
        vdBinding = DataBindingUtil.setContentView(this, getLayoutId())
        if (getViewModelBindingVariable() != NO_VIEW_MODEL_BINDING_VARIABLE) {
            setViewModelBindingVariable()
        }
        init()
    }

    private fun setViewModelBindingVariable() {
        vdBinding.setVariable(getViewModelBindingVariable(), vModel)
        vdBinding.executePendingBindings()
    }

    private fun getViewModelClass(aClass: Class<*>): Class<V> {
        val tyoe = aClass.genericSuperclass
        return if (tyoe is ParameterizedType) {
            tyoe.actualTypeArguments[1] as Class<V>
        } else {
            getViewModelClass(aClass.superclass as Class<*>)
        }
    }

    abstract fun init()
}