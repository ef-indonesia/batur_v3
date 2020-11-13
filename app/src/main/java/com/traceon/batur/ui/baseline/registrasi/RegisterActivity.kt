package com.traceon.batur.ui.baseline.registrasi

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.NavHostFragment
import com.traceon.batur.R
import com.traceon.batur.utils.Helper
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_register)) { _, insets ->
            stepper.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        supportActionBar?.setHomeAsUpIndicator(
            Helper.setToolbarIconColor(
                this,
                R.drawable.ic_arrow_back
            )
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean =
        NavHostFragment.findNavController(fm_register).navigateUp()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(
                    R.anim.enter,
                    R.anim.exit
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setTitle(s: String) {
        supportActionBar?.title = s
    }

    private fun View.setMarginTop(marginTop: Int) {
        val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        menuLayoutParams.setMargins(0, marginTop, 0, 0)
        this.layoutParams = menuLayoutParams
    }
}