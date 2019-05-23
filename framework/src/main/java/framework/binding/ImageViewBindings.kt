package framework.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image_resource")
fun setSrc(view: ImageView, resId: Int) {
    view.setImageResource(resId)
}

@BindingAdapter("url")
fun setUrl(view: ImageView, url: String?) {
    Glide.with(view.context).load(url).into(view)
}



