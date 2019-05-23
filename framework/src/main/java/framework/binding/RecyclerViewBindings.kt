package framework.binding

import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import framework.adapter.MultiTypeRecyclerAdapter
import framework.adapter.BaseRecyclerAdapter
import framework.adapter.SingleTypeRecyclerAdapter
import framework.viewmodel.BaseViewModel
import framework.viewmodel.RecyclerItem


/**
 * Created by Stepan on 23.11.2016.
 */

@BindingAdapter(value = ["viewModel", "items", "itemLayoutId", "orientation", "lifecycleOwner"], requireAll = false)
fun <T>bindItems(view: RecyclerView, vm: BaseViewModel?, items: ObservableArrayList<T>, itemLayoutId: Int?, orientation: Int?, lifecycleOwner: LifecycleOwner?) {
    if (view.adapter == null) {
        if (view.layoutManager == null) {
            view.layoutManager = LinearLayoutManager(view.context, orientation
                    ?: RecyclerView.VERTICAL, false)
        }
        if (itemLayoutId != null) {
            view.adapter = SingleTypeRecyclerAdapter(items, vm, itemLayoutId)
        } else {
            view.adapter = MultiTypeRecyclerAdapter(items as ObservableArrayList<RecyclerItem>, vm)
        }
    } else {
        (view.adapter as BaseRecyclerAdapter<*>).setItems(items)
    }
    (view.adapter as BaseRecyclerAdapter<*>).lifecycleOwner = lifecycleOwner
}
