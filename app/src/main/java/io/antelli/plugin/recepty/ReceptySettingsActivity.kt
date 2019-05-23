package io.antelli.plugin.recepty

import android.os.Bundle
import framework.view.MvvmActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.antelli.plugin.czechpack.R
import io.antelli.plugin.czechpack.databinding.ActivityReceptySettingsBinding

class ReceptySettingsActivity : MvvmActivity<ActivityReceptySettingsBinding, ReceptySettingsViewModel>() {
    override val layoutId = R.layout.activity_recepty_settings
    override val viewModelClass = ReceptySettingsViewModel::class

    private var permissions: RxPermissions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        permissions = RxPermissions(this)
    }
}
