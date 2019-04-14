package com.example.petmatcher.DI

import com.example.petmatcher.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Any injectable activity must be provided here.
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesMainActivity(): MainActivity
}