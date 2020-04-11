package com.example.petmatcher.DI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petmatcher.favorites.FavoritesViewModel
import com.example.petmatcher.home.HomeViewModel
import com.example.petmatcher.petdetails.DetailsViewModel
import com.example.petmatcher.organizations.OrganizationSearchViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * Since we provide fragments through subcomponents, and dependency injection is performed every time the fragment is recreated, we can't simply
 * provide the [ViewModelProvider] every time the fragment is recreated because the subcomponent ends up being discarded and recreated every time.
 * This would betray the scope of the [ViewModel], it wouldn't be able to survive fragment destruction.
 *
 * A way to get around this is to instead inject [ViewModelFactory]. The factory contains all required instances for [ViewModel] creation.
 */
@Singleton
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

/**
 * Multibinding so that we can look up a ViewModel instance by key. Without multi-bindings, we would have to duplicate
 * the factory code above for every [ViewModel] we wish to create. This approach is more scalable.
 *
 * @Target specifies annotation target types for ViewModelKey. @Retention specifies that this code is available via
 * reflection at runtime.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    internal abstract fun bindFavoritesModel(viewModel: FavoritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    internal abstract fun bindDetailsViewModel(viewModel: DetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrganizationSearchViewModel::class)
    internal abstract fun bindShelterSearchViewModel(viewModel: OrganizationSearchViewModel): ViewModel
}