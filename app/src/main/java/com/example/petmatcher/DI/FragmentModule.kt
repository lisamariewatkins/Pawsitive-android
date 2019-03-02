package com.example.petmatcher.DI

import com.example.petmatcher.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/*
*  Any injectable fragments must be provided here
*/
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesHomeFragment(): HomeFragment
}
