package framework.binding

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter


@BindingAdapter("textColorResource")
fun setTextColor(editText: TextView, resId: Int) {
    editText.setTextColor(ContextCompat.getColor(editText.context, resId))
}