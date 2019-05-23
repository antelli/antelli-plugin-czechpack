package framework.adapter

import androidx.databinding.ObservableArrayList
import framework.viewmodel.BaseViewModel
import framework.viewmodel.RecyclerItem

class MultiTypeRecyclerAdapter<T : RecyclerItem> :
        BaseRecyclerAdapter<T> {

    constructor(items: ObservableArrayList<T>, vm: BaseViewModel?) : super(items, vm)

    constructor(items: ObservableArrayList<T>) : super(items)

    override fun getLayoutId(itemType: Int): Int {
        return itemType
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }

}
