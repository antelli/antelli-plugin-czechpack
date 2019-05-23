package framework.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import framework.viewmodel.BaseViewModel
import framework.event.LiveEvent
import kotlin.reflect.KClass
import com.androidxtend.kit.BR

abstract class MvvmDialogFragment<B : ViewDataBinding, VM : BaseViewModel> : DialogFragment() {

    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    protected abstract val viewModelClass: KClass<VM>?
    @get:LayoutRes
    protected abstract val layoutId: Int

    protected open fun initBinding(binding: B) {
        if (viewModel != null) {
            binding.setVariable(BR.vm, viewModel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cls = viewModelClass
        if (cls != null) {
            viewModel = ViewModelProviders.of(this).get(cls.java)
            lifecycle.addObserver(viewModel)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.setLifecycleOwner(this)
        initBinding(binding)
        return binding.root
    }

    override fun onDestroy() {
        if (viewModel != null) {
            lifecycle.removeObserver(viewModel!!)
        }
        super.onDestroy()
    }

    protected fun <T : LiveEvent> subscribe(eventClass: KClass<T>, eventObserver: Observer<T>) {
        viewModel.subscribe(this, eventClass, eventObserver)
    }
}