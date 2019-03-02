package com.example.petmatcher.DI

import android.app.Application
import com.example.network.petlist.PetManager
import com.example.network.petlist.PetManagerImpl
import com.example.petmatcher.BaseApplication
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
    @Provides
    @Singleton
    fun providesPetManager(): PetManager {
        return PetManagerImpl()
    }
}
