package com.example.scalio.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.scalio.R
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(source: String) {
    Picasso.get().load(source).placeholder(R.drawable.ic_image_placeholder).into(this)
}