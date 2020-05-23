package com.riyase.livelogexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.riyase.livelog.LiveLog
import com.riyase.livelogexample.databinding.ActivityMainBinding
import java.lang.RuntimeException


class MainActivity : AppCompatActivity() {

    private val PERM_REQ = 1
    lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main);
        setupViews()
        askPerm()
    }

    var counter = 0;
    private fun setupViews() {
        binding.bStartLiveLog.setOnClickListener(View.OnClickListener {
            if (!Settings.canDrawOverlays(this@MainActivity)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.setData(Uri.parse("package:" + getPackageName()))
                startActivityForResult(intent, 101)
            } else {
                LiveLog.start()
                LiveLog.d("Tag", "started")
            }
        })

        binding.bStopLiveLog.setOnClickListener(View.OnClickListener {
            LiveLog.stop()
        })

        binding.bLog.setOnClickListener(View.OnClickListener {
            for (i in 0..10) {
                LiveLog.d("Tag", "counter : ${counter++}")
            }
            //throw RuntimeException("Oh my God")
        })
    }

    fun askPerm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // go to settings
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERM_REQ);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // go to settings
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_REQ);
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERM_REQ -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }
    /*override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<String>, grantResults : Array<Int>) {
        when (requestCode) {
            PERM_REQ -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }*/
}
