package io.antelli.plugin.idnes.tv

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager

import framework.view.MvvmActivity
import io.antelli.plugin.czechpack.R
import io.antelli.plugin.czechpack.databinding.ActivityIdnesTvSettingsBinding

class IdnesTvSettingsActivity : MvvmActivity<ActivityIdnesTvSettingsBinding, IdnesTvSettingsViewModel>() {

    override val layoutId = R.layout.activity_idnes_tv_settings
    override val viewModelClass = IdnesTvSettingsViewModel::class

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.getChannels()
    }
}
