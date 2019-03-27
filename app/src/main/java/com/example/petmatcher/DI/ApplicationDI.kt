package com.example.petmatcher.DI

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.network.petlist.PetManager
import com.example.network.petlist.PetManagerImpl
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
    fun providesPetManager(): PetManager {
        return PetManagerImpl()
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
