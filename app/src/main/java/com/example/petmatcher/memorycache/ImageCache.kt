package com.example.petmatcher.memorycache

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.LruCache
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.network.petlist.Pet
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


const val CACHE_SIZE = 25
/**
 * In memory cache of pet images
 */
@Singleton
class ImageCache @Inject constructor() {

    private val cache = LruCache<String, Drawable>(CACHE_SIZE)


    suspend fun cacheImages(context: Context?, pets: List<Pet>) {
        withContext(IO) {
            context?.let {
                pets.forEach { pet ->
                    val photos = pet.media.photos
                    if (photos != null) {
                        Glide.with(context)
                            .load(photos.photoList[3].url)
                            .into(object: CustomTarget<Drawable>(){
                                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                    cache.put(pet.id.value, resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }
                            })
                    }

                }
            }
        }

    }

    fun getImage(key: String): Drawable? {
        return cache.get(key)
    }
}