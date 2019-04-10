package com.example.petmatcher.DI

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.network.RetrofitFactory
import com.example.network.RetrofitFactoryV2
import com.example.network.animals.AnimalService
import com.example.network.animals.AnimalServiceImpl
import com.example.network.petlist.PetManager
import com.example.network.petlist.PetManagerImpl
import com.example.network.shelter.ShelterManager
import com.example.network.shelter.ShelterManagerImpl
import com.example.petmatcher.BaseApplication
import com.example.petmatcher.data.AppDatabase
import com.example.petmatcher.data.FavoriteDao
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
    fun providesRetrofitInstance(context: Context): RetrofitFactory {
        return RetrofitFactory(context)
    }

    @Singleton
    @Provides
    fun providesRetrofitV2Instance(): RetrofitFactoryV2 {
        return RetrofitFactoryV2()
    }

    @Singleton
    @Provides
    fun providesAnimalService(retrofitFactoryV2: RetrofitFactoryV2): AnimalService {
        return AnimalServiceImpl(retrofitFactoryV2)
    }

    @Singleton
    @Provides
    fun providesPetManager(retrofitFactory: RetrofitFactory): PetManager {
        return PetManagerImpl(retrofitFactory)
    }

    @Singleton
    @Provides
    fun providesShelterManager(retrofitFactory: RetrofitFactory): ShelterManager {
        return ShelterManagerImpl(retrofitFactory)
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
    fun provideUserDao(db: AppDatabase): FavoriteDao {
        return db.favoriteDao()
    }
}
