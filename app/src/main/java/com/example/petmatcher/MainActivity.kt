package com.example.petmatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.petmatcher.data.PetRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author Lisa Watkins
 *
 * Main activity that hosts navigation graph
 */
class MainActivity: DaggerAppCompatActivity(), HasSupportFragmentInjector, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var petRepository: PetRepository

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpBottomNavMenu(findNavController(R.id.nav_host_fragment))

        // eagerly fetch pet list
        launch {
            withContext(IO) {
                petRepository.getPets() // TODO("splash screen/re-examine if this is the right place for this")
            }
        }
    }

    private fun setUpBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.setupWithNavController(navController)
    }
}
