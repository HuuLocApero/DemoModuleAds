package com.demo.ui.services

import android.app.*
import android.content.Intent
import android.os.IBinder
import dagger.android.AndroidInjection

class ServiceManager : Service() {
    private var instance: ServiceManager? = null

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    fun getInstance(): ServiceManager? {
        return this.instance
    }

    private fun setInstance(instance: ServiceManager) {
        this.instance = instance
    }


    companion object {
        private var instance: ServiceManager? = null
        fun getInstance(): ServiceManager? {
            return instance
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (getInstance() == null) {
            setInstance(this)
        }
        return START_STICKY

    }
}


