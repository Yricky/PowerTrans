package com.yricky.powertrans

import android.app.Application
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class PTApp :Application(){
    companion object{
        val tpe:ThreadPoolExecutor by lazy {
            ThreadPoolExecutor(1,8,10L,TimeUnit.MILLISECONDS, LinkedBlockingQueue(12))
        }
        lateinit var inst:PTApp
    }
    override fun onCreate() {
        super.onCreate()
        inst = this
    }
}