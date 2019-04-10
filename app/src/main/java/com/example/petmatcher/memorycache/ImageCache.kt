package com.example.petmatcher.memorycache

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.LruCache
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.network.animals.Animal
import com.example.network.petlist.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


const val CACHE_SIZE = 25
/**
 * In memory cache of pet images
 */
@Singleton
class ImageCache @Inject constructor(val context: Context) {

    private val cache = LruCache<Int, Drawable>(CACHE_SIZE)

    suspend fun cacheImages(pets: List<Animal>) {
        withContext(Dispatchers.Default) {
            pets.forEach { animal ->
                val photos = animal.photos
                if (!photos.isEmpty()) {
                    Glide.with(context)
                        .load(photos[0].large)
                        .into(object: CustomTarget<Drawable>(){
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                cache.put(animal.id, resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }
                        })
                }
            }
        }

    }

    fun getImage(key: Int): Drawable? {
        return cache.get(key)
    }
}