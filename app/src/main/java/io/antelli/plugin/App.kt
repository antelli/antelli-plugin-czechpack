package io.antelli.plugin

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.idescout.sql.SqlScoutServer

/**
 * Handcrafted by Štěpán Šonský on 19.11.2017.
 */

class App : Application() {

    companion object {
        lateinit var instance : App
        fun get() : App{
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseApp.initializeApp(this)
        SqlScoutServer.create(this, getPackageName());
    }
}
