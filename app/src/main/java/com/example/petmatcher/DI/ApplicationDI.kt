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
import com.example.petmatcher.data.AppDatabase
import com.example.petmatcher.data.FavoriteDao
import com.example.petmatcher.data.OrganizationDao
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ApplicationModule::class,
    ActivityModule::class]
)
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }

    fun inject(app: BaseApplication)
}

@Module(includes = [
    ViewModelModule::class
])
class ApplicationModule {
    @Singleton
    @Provides
    fun providesContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun providesRetrofitV2Instance(context: Context): RetrofitFactoryV2 {
        return RetrofitFactoryV2(context)
    }

    @Singleton
    @Provides
    fun providesAnimalService(retrofitFactoryV2: RetrofitFactoryV2): AnimalService {
        return AnimalServiceImpl(retrofitFactoryV2)
    }

    @Singleton
    @Provides
    fun providesOrganizationService(retrofitFactoryV2: RetrofitFactoryV2): OrganizationService {
        return OrganizationServiceImpl(retrofitFactoryV2)
    }

    @Singleton
    @Provides
    fun providesConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideDb(application: Application): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, "petFinder.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao {
        return db.favoriteDao()
    }

    @Singleton
    @Provides
    fun provideOrganizationDao(db: AppDatabase): OrganizationDao {
        return db.organizationDao()
    }
}
