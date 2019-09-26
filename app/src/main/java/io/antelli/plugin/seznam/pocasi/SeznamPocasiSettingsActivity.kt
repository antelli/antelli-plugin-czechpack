package io.antelli.plugin.seznam.pocasi

import android.Manifest
import android.os.Bundle

import framework.view.MvvmActivity
import com.tbruyelle.rxpermissions2.RxPermissions

import io.antelli.plugin.czechpack.R
import io.antelli.plugin.czechpack.databinding.ActivitySeznamPocasiSettingsBinding

class SeznamPocasiSettingsActivity : MvvmActivity<ActivitySeznamPocasiSettingsBinding, SeznamPocasiSettingsViewModel>(), SeznamPocasiSettingsContract {

    override val layoutId = R.layout.activity_seznam_pocasi_settings
    override val viewModelClass = SeznamPocasiSettingsViewModel::class

    private var permissions: RxPermissions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissions = RxPermissions(this)
        setSupportActionBar(binding.toolbar)
        binding.v = this
    }

    override fun gpsCheckedChanged(checked: Boolean) {
        if (checked) {
            permissions?.request(Manifest.permission.ACCESS_COARSE_LOCATION)?.subscribe { checked -> viewModel.isGps.set(checked) }
        }
    }
}
