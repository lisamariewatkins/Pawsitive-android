package com.example.petmatcher.DI

import com.example.petmatcher.favorites.FavoritesFragment
import com.example.petmatcher.home.HomeFragment
import com.example.petmatcher.petdetails.DetailsFragment
import com.example.petmatcher.search.OrganizationSearchFragment
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
    abstract fun contributesInfoFragment(): OrganizationSearchFragment

    @ContributesAndroidInjector
    abstract fun contributesDetailFragment(): DetailsFragment
}
