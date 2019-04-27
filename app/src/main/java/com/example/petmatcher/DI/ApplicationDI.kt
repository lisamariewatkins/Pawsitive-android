package com.example.petmatcher.DI

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.example.network.RetrofitFactoryV2
import com.example.network.animals.AnimalService
import com.example.network.animals.AnimalServiceImpl
import com.example.network.organizations.OrganizationService
import com.example.network.organizations.OrganizationServiceImpl
import com.example.petmatcher.BaseApplication
import com.example.database.AppDatabase
import com.example.database.FavoriteDao
import com.example.database.OrganizationDao
import com.example.petmatcher.imageutil.ImageLoader
import com.example.petmatcher.util.Logger
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * [AndroidInjectionModule] provides bindings for all base Android classes
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ApplicationModule::class,
    ActivityModule::class,
    WorkerBindingModule::class]
)
interface ApplicationComponent {
    /**
     * Defines the builder class that Dagger generates for this component. You can enforce here what dagger needs to create
     * the root of the graph, in this case we only need an [Application] instance.
     */
    @Component.Builder
    interface Builder {
        /**
         * Binds the instance of an application to the root component of the graph.
         */
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }

    fun inject(app: BaseApplication)
}

/**
 * Application level dependencies. Notice that [ViewModelModule] is injected here, so we are dealing with one instance
 * of the factory for the scope of the application.
 */
@Module(includes = [
    ViewModelModule::class
])
object ApplicationModule {
    @Singleton
    @JvmStatic
    @Provides
    fun providesContext(application: Application): Context {
        return application
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesRetrofitV2Instance(context: Context): RetrofitFactoryV2 {
        return RetrofitFactoryV2(context)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesAnimalService(retrofitFactoryV2: RetrofitFactoryV2): AnimalService {
        return AnimalServiceImpl(retrofitFactoryV2)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesOrganizationService(retrofitFactoryV2: RetrofitFactoryV2): OrganizationService {
        return OrganizationServiceImpl(retrofitFactoryV2)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideDb(application: Application): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, "petFinder.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao {
        return db.favoriteDao()
    }

    @Singleton
    @JvmStatic
    @Provides
    fun provideOrganizationDao(db: AppDatabase): OrganizationDao {
        return db.organizationDao()
    }
}
