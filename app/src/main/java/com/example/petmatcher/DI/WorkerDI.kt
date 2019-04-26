package com.example.petmatcher.DI

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.petmatcher.search.ChildWorkerFactory
import com.example.petmatcher.search.OrganizationWorker
import dagger.*
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out CoroutineWorker>)

@Module
interface WorkerBindingModule {
    @Binds
    @IntoMap
    @WorkerKey(OrganizationWorker::class)
    fun bindHelloWorldWorker(factory: OrganizationWorker.Factory): ChildWorkerFactory
}

class SampleWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out CoroutineWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val foundEntry =
            workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = foundEntry?.value
            ?: throw IllegalArgumentException("unknown worker class name: $workerClassName")
        return factoryProvider.get().create(appContext, workerParameters)
    }
}