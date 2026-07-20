package com.utaputranto.joyviekmp.core.platform

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.lang.ref.WeakReference

object PlatformContextProvider {
    @Volatile
    private var appContext: Context? = null

    private var currentActivityRef: WeakReference<Activity>? = null

    val context: Context
        get() =
            checkNotNull(appContext) {
                "PlatformContextProvider has not been initialized. " +
                    "Call PlatformContextProvider.init(context) from Application.onCreate()."
            }

    val currentActivity: Activity?
        get() = currentActivityRef?.get()

    fun init(application: Application) {
        appContext = application.applicationContext
        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?,
                ) {}

                override fun onActivityStarted(activity: Activity) {}

                override fun onActivityResumed(activity: Activity) {
                    currentActivityRef = WeakReference(activity)
                }

                override fun onActivityPaused(activity: Activity) {
                    if (currentActivityRef?.get() == activity) {
                        currentActivityRef = null
                    }
                }

                override fun onActivityStopped(activity: Activity) {}

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle,
                ) {}

                override fun onActivityDestroyed(activity: Activity) {
                    if (currentActivityRef?.get() == activity) {
                        currentActivityRef = null
                    }
                }
            },
        )
    }
}
