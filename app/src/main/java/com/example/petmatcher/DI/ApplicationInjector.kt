package com.example.petmatcher.DI

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.petmatcher.BaseApplication
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/*
* Inject application with activity lifecycle callbacks
*/
object ApplicationInjector {
    fun init(app: BaseApplication) {
        DaggerApplicationComponent.builder().application(app)
            .build().inject(app)

        app.registerActivityLifecycleCallbacks(object
            : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    handleActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {
                    // not used
                }

                override fun onActivityResumed(activity: Activity) {
                    // not used
                }

                override fun onActivityPaused(activity: Activity) {
                    // not used
                }

                override fun onActivityStopped(activity: Activity) {
                    // not used
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
                    // not used
                }

                override fun onActivityDestroyed(activity: Activity) {
                    // not used
                }
            })
    }

    /*
    * Lifecycle callback to inject any new fragment on the stack
    */
    private fun handleActivity(activity: Activity) {
        if (activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                .registerFragmentLifecycleCallbacks(
                    object: FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(
                            fm: FragmentManager,
                            f: Fragment,
                            savedInstanceState: Bundle?
                        ) {
                            if (f is Injectable) {
                                AndroidSupportInjection.inject(f)
                            }
                        }
                    }, true
                )
        }
    }
}