package com.cheatshqip.appupdate.adapter.output

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity

class CurrentActivityProvider : Application.ActivityLifecycleCallbacks {
    var currentActivity: ComponentActivity? = null
        private set

    override fun onActivityResumed(activity: Activity) {
        if (activity is ComponentActivity) currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        if (currentActivity === activity) currentActivity = null
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity === activity) currentActivity = null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
}
