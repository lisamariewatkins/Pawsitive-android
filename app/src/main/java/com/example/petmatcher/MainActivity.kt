package com.example.petmatcher

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.petmatcher.networkutil.ConnectivityLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * @author Lisa Watkins
 *
 * Main activity that hosts navigation graphs for all tab bar entry points. The activity also contains a network status
 * bar that listens for connectivity events via [LiveData] to update the user on their connection status.
 */
class MainActivity: AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        setupConnectivityManager()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        /** order matters **/
        val navGraphIds = listOf(R.navigation.home, R.navigation.favorites, R.navigation.shelters)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(this, navController)
        })
        currentNavController = controller
    }

    /**
     * If we lose network connection, show the user a "hey, there's no network" message for 4 seconds and then hide it.
     */
    private fun setupConnectivityManager() {
        val connectionStatusBar = findViewById<TextView>(R.id.connection_status)
        val connectivityLiveData = ConnectivityLiveData(connectivityManager)

        connectivityLiveData.observe(this, Observer { network ->
            connectionStatusBar.apply {
                if (network) {
                    visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    postDelayed({
                        visibility = View.GONE
                    }, 4000)
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    /**
     * Overriding popBackStack is necessary in this case if the app is started from the deep link.
     */
    override fun onBackPressed() {
        if (currentNavController?.value?.popBackStack() != true) {
            super.onBackPressed()
        }
    }
}
