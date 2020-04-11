package com.example.petmatcher.DI

import com.example.petmatcher.favorites.FavoritesFragment
import com.example.petmatcher.home.HomeFragment
import com.example.petmatcher.petdetails.DetailsFragment
import com.example.petmatcher.organizations.OrganizationSearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Any fragment that needs to be injected should be provided here.
 *
 * This module is references by MainActivity's AndroidInjector. The providers for these fragments are initialized when MainActivity's
 * component (i.e. subcomponent) is initialized.
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
