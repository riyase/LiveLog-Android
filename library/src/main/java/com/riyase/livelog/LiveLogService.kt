package com.riyase.livelog

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.riyase.livelog.views.PageAdapter


class LiveLogService : Service() {

    private lateinit var mLayout: LinearLayout;
    private lateinit var mManager: WindowManager;
    private var minimized = false
    private lateinit var adapter: PageAdapter
    private val screenSize: Pair<Int, Int> by lazy { getDisplayMetrics(mManager) }
    private val lpMinimize: WindowManager.LayoutParams by lazy { getWindowLayoutParam(true, wfNormal) }
    private val lpMaximize: WindowManager.LayoutParams by lazy { getWindowLayoutParam(false, wfNormal) }
    private val lpKeyboard: WindowManager.LayoutParams by lazy { getWindowLayoutParam(false, wfKeyboard) }
    private val wfNormal : Int = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    private val wfKeyboard : Int = 0
    lateinit var lpCurrent: WindowManager.LayoutParams

    private val myBinder = MyLocalBinder()
    private lateinit var listener: Listener;

    interface Listener {
        fun onStopClicked();
    }

    inner class MyLocalBinder : Binder() {
        fun getService(): LiveLogService {
            return this@LiveLogService
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener;
    }

    override fun onCreate() {
        super.onCreate()
        setupScreen()
    }

    private fun setupScreen() {
        mManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mLayout = buildView()
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        mLayout.setBackgroundColor(Color.rgb(255, 255, 255))
        mLayout.layoutParams = params

        mManager.addView(mLayout, lpMaximize)
        mLayout.setOnTouchListener { v, e ->
            if (e.action == MotionEvent.ACTION_MOVE && minimized) {
                lpCurrent.y = e.rawY.toInt() - lpCurrent.height
                mManager.updateViewLayout(mLayout, lpCurrent)
            }
            false
        }

    }

    private fun getDisplayMetrics(manager: WindowManager): Pair<Int, Int> {
        val displayMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(displayMetrics)
        return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    private fun getWindowLayoutParam(minimize: Boolean, flags : Int): WindowManager.LayoutParams {
        val height = if (minimize) baseContext.resources.getDimensionPixelSize(R.dimen.minimized_height) else screenSize.second
        val lp = WindowManager.LayoutParams(
            screenSize.first, height,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            flags,
            PixelFormat.TRANSLUCENT
        )
        lp.x = 0
        lp.y = 100
        lp.gravity = Gravity.TOP
        return lp
    }

    private fun buildView(): LinearLayout {
        val rootView: View = LayoutInflater.from(this).inflate(R.layout.layout_logs, null, false)
        val viewPager: ViewPager = rootView.findViewById(R.id.pager)
        adapter = PageAdapter(this, mainInterface)
        viewPager.adapter = adapter
        val ibMinimize: ImageButton = rootView.findViewById(R.id.ibMinimize)
        ibMinimize.setOnClickListener {
            lpCurrent = if (minimized) {
                lpMaximize
            } else {
                lpMinimize
            }
            mManager.updateViewLayout(mLayout, lpCurrent)
            minimized = !minimized
            if (minimized) {
                ibMinimize.setImageResource(R.drawable.ic_maximize)
            } else {
                ibMinimize.setImageResource(R.drawable.ic_minimize)
            }
        }
        val buttonLogs: View = rootView.findViewById(R.id.ibLogs)
        buttonLogs.setOnClickListener { viewPager.currentItem = 0 }
        val buttonFiles: View = rootView.findViewById(R.id.ibFiles)
        buttonFiles.setOnClickListener { viewPager.currentItem = 1 }
        val buttonClose: View = rootView.findViewById(R.id.ibClose)
        buttonClose.setOnClickListener { listener.onStopClicked() }
        val buttonAppSet: View = rootView.findViewById(R.id.ibAppSet)
        buttonAppSet.setOnClickListener { openAppSettings() }
        return rootView as LinearLayout
    }

    private val mainInterface = object : LiveLogInterface {
        override fun onKeyboardMode(active: Boolean) {
            if (active) {
                mManager.updateViewLayout(mLayout, lpKeyboard)
            } else{
                mManager.updateViewLayout(mLayout, lpMaximize)
            }
        }
    }

    fun i(tag: String, msg: String) {
    }

    fun v(tag: String, msg: String) {
    }

    fun d(tag: String, msg: String) {
        adapter.addLog(tag, msg)
    }

    fun w(tag: String, msg: String) {
    }

    fun e(tag: String, msg: String) {
        adapter.addLog(tag, msg)
    }

    fun wtf(tag: String, msg: String) {
    }

    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
    }

    fun openAppSettings() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.setData(Uri.parse("package:" + getPackageName()))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        if (!minimized) {
            lpCurrent = lpMinimize
            mManager.updateViewLayout(mLayout, lpCurrent)
            minimized = true
        }
    }

    override fun onDestroy() {
        mManager.removeView(mLayout)
        super.onDestroy()
    }

}

interface LiveLogInterface {
    fun onKeyboardMode(active: Boolean)
}