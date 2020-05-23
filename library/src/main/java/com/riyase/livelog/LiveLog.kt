package com.riyase.livelog
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

class LiveLog {

    var liveLogEnabled = false;
    var fileLogEnabled = false;
    companion object {
        /*fun liveLogEnabled(enbaled : Boolean) {
            liveLogEnabled = enbaled;
        }

        fun fileLogEnabled(enbaled: Boolean) {
            fileLogEnabled = enbaled;
        }*/

        lateinit var context: Context

        fun i(tag : String, msg : String) {
            Log.i(tag, msg);
            if (isBound) myService.i(tag, msg)
        }

        fun v(tag : String, msg : String) {
            Log.v(tag, msg);
            if (isBound) myService.v(tag, msg)
        }

        fun d(tag : String, msg : String) {
            Log.d(tag, msg);
            if (isBound) myService.d(tag, msg)
        }

        fun w(tag : String, msg : String) {
            Log.w(tag, msg);
            if (isBound) myService.w(tag, msg)
        }

        fun e(tag : String, msg : String) {
            Log.e(tag, msg);
            if (isBound) myService.e(tag, msg)
        }

        fun wtf(tag : String, msg : String) {
            Log.wtf(tag, msg);
            if (isBound) myService.wtf(tag, msg)
        }

        fun init(app : Application) {
            context = app.applicationContext
        }

        fun start() {
            val intent = Intent(context, LiveLogService::class.java)
            //context.startService(intent)
            context.bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        }

        fun stop() {
            if (isBound) context.unbindService(myConnection)
        }

        private lateinit var myService: LiveLogService
        private var isBound = false
        private val myConnection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                val binder = service as LiveLogService.MyLocalBinder
                myService = binder.getService()
                myService.setListener(listener)
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                isBound = false
            }
        }

        private val listener = object : LiveLogService.Listener {
            override fun onStopClicked() {
                stop()
            }
        }
    }
}