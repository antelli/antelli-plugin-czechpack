package framework.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ObservableArrayList

import framework.viewmodel.BaseViewModel
import framework.viewmodel.RecyclerItem

/**
 * Handcrafted by Štěpán Šonský on 15.01.2018.
 */

class SingleTypeRecyclerAdapter<T> : BaseRecyclerAdapter<T> {

    @LayoutRes
    private var layoutId: Int = 0

    constructor(items: ObservableArrayList<T>, viewModel: BaseViewModel?, itemLaoyutId: Int) : super(items, viewModel) {
        this.layoutId = itemLaoyutId
    }

    constructor(items: ObservableArrayList<T>, itemLaoyutId: Int) : super(items) {
        this.layoutId = itemLaoyutId
    }

    override fun getLayoutId(itemType: Int): Int {
        return layoutId
    }
}
