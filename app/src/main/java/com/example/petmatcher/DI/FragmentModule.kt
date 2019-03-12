package com.example.petmatcher.DI

import com.example.petmatcher.favorites.FavoritesFragment
import com.example.petmatcher.home.HomeFragment
import com.example.petmatcher.info.InfoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/*
*  Any injectable fragments must be provided here
*/
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributesFavoritesFragment(): FavoritesFragment

    @ContributesAndroidInjector
    abstract fun contributesInfoFragment(): InfoFragment
}
