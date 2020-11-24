package com.traceon.batur.ui.pending

import android.view.View
import androidx.lifecycle.ViewModel
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.traceon.batur.R
import com.traceon.batur.data.db.query
import com.traceon.batur.data.model.Pending
import com.traceon.batur.data.model.PendingFields
import com.traceon.batur.databinding.FragmentPendingBinding
import com.traceon.batur.ui.MainActivity
import com.traceon.batur.ui.base.BaseFragment

class PendingFragment : BaseFragment<FragmentPendingBinding, ViewModel>(),
    PendingAdapter.OnItemCheckListener {
    private var listPending: ArrayList<Pending> = ArrayList()
    private lateinit var adapter: PendingAdapter
    private lateinit var skeletonScreen: SkeletonScreen

    override fun getViewModelBindingVariable(): Int = NO_VIEW_MODEL_BINDING_VARIABLE

    override fun getLayoutId(): Int = R.layout.fragment_pending

    override fun init() {
        adapter = PendingAdapter(listPending, this) {

        }
        skeletonScreen = Skeleton.bind(getDataBinding().rvPending)
            .adapter(adapter)
            .color(R.color.skeleton_shimer)
            .load(R.layout.item_pending_skeleton)
            .show()

        try {
            loadData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getDataBinding().slPending.setOnRefreshListener {
            loadData()
        }

        (activity as MainActivity).setTitleActionBar("Pending")
    }

    private fun loadData() {
        listPending.clear()
        listPending.addAll(Pending().query { equalTo(PendingFields.STATUS, "0") })
        adapter.notifyDataSetChanged()
        getDataBinding().slPending.isRefreshing = false
        skeletonScreen.hide()
        if (adapter.itemCount > 0) {
            getDataBinding().empty.visibility = View.GONE
        } else {
            getDataBinding().empty.visibility = View.VISIBLE
        }
    }
}