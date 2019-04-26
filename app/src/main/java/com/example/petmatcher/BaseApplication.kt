package com.example.petmatcher

import android.app.Activity
import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.example.petmatcher.DI.ApplicationInjector
import com.example.petmatcher.DI.SampleWorkerFactory
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BaseApplication: Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var workerFactory: SampleWorkerFactory

    override fun onCreate() {
        super.onCreate()
        ApplicationInjector.init(this)
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(workerFactory).build())
    }

    override fun activityInjector() = dispatchingAndroidInjector
}