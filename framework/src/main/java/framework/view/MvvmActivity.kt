package framework.view

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.androidxtend.kit.BR
import framework.event.LiveEvent
import framework.viewmodel.BaseViewModel
import kotlin.reflect.KClass

/**
 * Created by Stepan on 06.10.2016.
 */

abstract class MvvmActivity<B : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    protected lateinit var viewModel: VM
    protected lateinit var binding: B

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected abstract val viewModelClass: KClass<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setLifecycleOwner(this)
        binding.setVariable(BR.vm, viewModel)
    }

    private fun initViewModel() {
        val cls = viewModelClass
        viewModel = ViewModelProviders.of(this).get(cls.java)
        lifecycle.addObserver(viewModel)
    }

    protected fun <T : LiveEvent> subscribe(eventClass: KClass<T>, eventObserver: Observer<T>) {
        viewModel.subscribe(this, eventClass, eventObserver)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
    }

    override public fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
