package com.example.petmatcher.imageutil

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import javax.inject.Inject

/**
 * @author Lisa Watkins
 *
 * Abstraction layer over image loading mechanisms. This way, if we change how we load images, we don't need to change
 * any other code.
 */
class ImageLoader @Inject constructor(val context: Context) {

    fun loadImageIntoCustomTarget(url: String, target: CustomTarget<Drawable>) {
        Glide.with(context)
            .load(url)
            .into(target)
    }

    /**
     * Load image into [ImageView] with optional placeholder and error images
     */
    fun loadImageIntoView(url: String, imageView: ImageView) {
        val circularProgressDrawable = createImageLoadingSpinner()

        val options = RequestOptions()
            .centerCrop()
            .placeholder(circularProgressDrawable)

        Glide.with(context).load(url).apply(options).into(imageView)
    }

    private fun createImageLoadingSpinner(): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }
}