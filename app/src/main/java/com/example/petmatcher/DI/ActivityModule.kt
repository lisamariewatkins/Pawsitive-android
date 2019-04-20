package com.example.petmatcher.DI

import com.example.petmatcher.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Any injectable activity must be provided here.
 *
 * Dagger generates subcomponent factories for each activity listed here. The subcomponent factories build the component with any
 * modules specified in their annotations, which create another level of subcomponents.
 *
 * When [ApplicationComponent] is created, Dagger will create providers for a map of each activity listed below to their respective subcomponent factory.
 *
 * When we call AndroidInjector.inject(activity), Dagger will look up the activity's subcomponent builder in this map to inject all of the
 * activity's dependencies.
 */
@Module
abstract class ActivityModule {
    /**
     * Dagger generates a subcomponent factory for MainActivity. When the component is initialized, all of the providers for
     * [FragmentBuildersModule] dependencies will be created.
     */
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributesMainActivity(): MainActivity
}