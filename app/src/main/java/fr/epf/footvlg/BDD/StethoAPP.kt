package fr.epf.footvlg.BDD

import android.app.Application
import com.facebook.stetho.Stetho

class StethoAPP: Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}