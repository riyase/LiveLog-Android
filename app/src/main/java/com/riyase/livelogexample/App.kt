package com.riyase.livelogexample

import android.app.Application
import com.riyase.livelog.LiveLog
import kotlin.system.exitProcess

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LiveLog.init(this)
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            LiveLog.e("Fatal", e.toString())
            exitProcess(1);
        }
    }
}