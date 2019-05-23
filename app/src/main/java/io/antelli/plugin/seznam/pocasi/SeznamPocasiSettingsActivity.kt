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
        setSupportActionBar(binding.toolbar)
        binding.v = this
        permissions = RxPermissions(this)
    }

    override fun gpsCheckedChanged(checked: Boolean) {
        if (checked) {
            permissions!!.request(Manifest.permission.ACCESS_COARSE_LOCATION).subscribe { aBoolean -> viewModel.isGps.set(aBoolean) }
        }
    }
}
