package com.example.petmatcher.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

interface WithDefaultCoroutineScope: CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    fun cancelAllChildren() {
        coroutineContext.cancelChildren()
    }
}