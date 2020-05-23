package com.riyase.livelog.views.files

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riyase.livelog.LiveLogInterface
import com.riyase.livelog.R
import com.riyase.livelog.views.files.FilesAdapter.Listener
import java.io.File

class FilesView : ConstraintLayout {

    val mainInterface : LiveLogInterface
    lateinit var recyclerView : RecyclerView
    val adapter : FilesAdapter
    var currentDir : File

    constructor(context : Context, mainInterface: LiveLogInterface) : super(context) {
        this.mainInterface = mainInterface
        adapter = FilesAdapter(adapterListener)
        initView(context)
        currentDir = FileUtil.getAppDir(context)
        listFiles(currentDir)
    }

    fun initView(context : Context) {
        inflate(context, R.layout.view_files, this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(getContext())
        recyclerView.adapter = adapter
        val ibUp : View = findViewById(R.id.ibUp)
        ibUp.setOnClickListener { goUp() }
        val ibSdStorage : View = findViewById(R.id.ibSdStorage)
        ibSdStorage.setOnClickListener { listStorageFiles(FileUtil.getStorageDir(context)) }
        val ibPhoneStorage : View = findViewById(R.id.ibPhoneStorage)
        ibPhoneStorage.setOnClickListener { listFiles(FileUtil.getAppDir(context)) }
        val ibNewDir : View = findViewById(R.id.ibNewDir)
        ibNewDir.setOnClickListener { newFile(true) }
        val ibNewFile : View = findViewById(R.id.ibNewFile)
        ibNewFile.setOnClickListener { newFile(false) }
        val bSave : Button = findViewById(R.id.bSave)
        bSave.setOnClickListener{ saveFile() }
        val bCancelNewFile : Button = findViewById(R.id.bCancel)
        bCancelNewFile.setOnClickListener{ closeNewFileLayout() }
    }


    fun listStorageFiles(dir: File) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            listFiles(dir)
        } else {
            Toast.makeText(context, "No READ_EXTERNAL_STORAGE permission", Toast.LENGTH_SHORT).show()
        }
    }

    fun listFiles(dir : File) {
        currentDir = dir
        val files = currentDir.listFiles()
        if (files != null) {
            adapter.setFiles(files)
        } else {
            adapter.setFiles(emptyArray())
        }
    }

    fun goUp() {
        val parent = currentDir.parentFile
        if (parent != null) {
            listFiles(parent)
        }
    }

    fun newFile(isDir : Boolean) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            mainInterface.onKeyboardMode(true)
            findViewById<View>(R.id.loNewFile).visibility = View.VISIBLE
            findViewById<View>(R.id.recyclerView).visibility = View.INVISIBLE
            findViewById<View>(R.id.etContent).visibility = if (isDir) {
                View.GONE
            } else{
                View.VISIBLE
            }
        } else {
            Toast.makeText(context, "No WRITE_EXTERNAL_STORAGE permission", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveFile() {
        val etContent : EditText = findViewById(R.id.etContent)
        val etName : EditText = findViewById(R.id.etName)
        val fileName = etName.text.toString().trim()
        if (etContent.visibility == View.VISIBLE) {
            val fileContent = etContent.text.toString().trim()
            if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(fileContent)) {
                FileUtil.createFile(currentDir, etName.text.toString(), etContent.text.toString())
            }
        } else {
            if (!TextUtils.isEmpty(fileName)) {
                FileUtil.createDir(currentDir, etName.text.toString())
            }
        }
        closeNewFileLayout()
        listFiles(currentDir)
    }

    fun closeNewFileLayout() {
        val etContent : EditText = findViewById(R.id.etContent)
        val etName : EditText = findViewById(R.id.etName)
        etContent.setText("")
        etName.setText("")
        findViewById<View>(R.id.loNewFile).visibility = View.INVISIBLE
        findViewById<View>(R.id.recyclerView).visibility = View.VISIBLE
        mainInterface.onKeyboardMode(false)
    }

    private val adapterListener = object : Listener {
        override fun onFileClicked(file : File) {
            TODO("Not yet implemented")
        }

        override fun onDirClicked(file : File) {
            listFiles(file)
        }

    }

}