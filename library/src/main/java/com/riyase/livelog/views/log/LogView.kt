package com.riyase.livelog.views.log

import android.content.Context
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riyase.livelog.R

class LogView : ConstraintLayout {

    lateinit var recyclerView : RecyclerView
    val adapter = LogAdapter()

    constructor(context : Context) : super(context) {
        initView(context)
    }
    /*constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {

    }

    constructor(context: Context, attributes: AttributeSet, style : Int) : super(context, attributes, style) {

    }*/

    fun initView(context : Context) {
        inflate(context, R.layout.view_logs, this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(getContext())
        recyclerView.adapter = adapter
        val ibMultiLine : ImageButton = rootView.findViewById(R.id.ibMultiLine)
        ibMultiLine.setOnClickListener {
            val isMulti = adapter.toggleMultiLine()
            if (isMulti) {
                ibMultiLine.setImageResource(R.drawable.ic_singleline)
            } else {
                ibMultiLine.setImageResource(R.drawable.ic_multiline)
            }
        }
    }

    fun addLog(tag : String, log : String) {
        adapter.addLog("D/$tag: $log")
    }

}