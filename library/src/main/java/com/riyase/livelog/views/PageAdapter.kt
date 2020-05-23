package com.riyase.livelog.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.riyase.livelog.LiveLogInterface
import com.riyase.livelog.views.files.FilesView
import com.riyase.livelog.views.log.LogView

class PageAdapter : PagerAdapter {

    private val logView : LogView
    private val filesView : FilesView
    private val mainInterface : LiveLogInterface

    constructor(context : Context, mainInterface : LiveLogInterface) {
        this.mainInterface = mainInterface
        this.logView = LogView(context)
        this.filesView = FilesView(context, mainInterface)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return if (position == 0) {
            container.addView(logView)
            logView
        } else {
            container.addView(filesView)
            filesView
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, view : Any) {
        container.removeView(view as View)
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Hello"
    }

    fun addLog(tag : String, log : String) {
        logView.addLog(tag, log)
    }
}