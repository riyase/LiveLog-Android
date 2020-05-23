package com.riyase.livelog.views.files

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riyase.livelog.R
import java.io.File

class FilesAdapter : RecyclerView.Adapter<FilesAdapter.Vh> {

    private val listener : Listener
    private var files = arrayOf<File>()

    interface Listener {
        fun onFileClicked(file : File)
        fun onDirClicked(file: File)
    }

    constructor(listener : Listener) {
        this.listener = listener
    }

    fun setFiles(files : Array<File>) {
        this.files = files
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return if (viewType == 0) {
            VhDirectory(LayoutInflater.from(parent.context).inflate(R.layout.list_item_files_dir, parent, false))
        } else{
            VhFile(LayoutInflater.from(parent.context).inflate(R.layout.list_item_files_file, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (files[position].isDirectory) {
            0
        } else{
            1
        }
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(files[position])
    }

    abstract class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(file : File)
    }

    inner class VhDirectory : Vh , View.OnClickListener {

        val tvName: TextView

        constructor(itemView: View) : super(itemView) {
            tvName = itemView.findViewById(R.id.tvName)
            tvName.setOnClickListener(this)
        }

        override fun bind(file: File) {
            tvName.setText(file.name)
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.tvName -> listener.onDirClicked(files[adapterPosition])
            }
        }
    }

    inner class VhFile : Vh , View.OnClickListener {

        val tvName: TextView

        constructor(itemView: View) : super(itemView) {
            tvName = itemView.findViewById(R.id.tvName)
            tvName.setOnClickListener(this)
        }

        override fun bind(file: File) {
            tvName.setText(file.name)
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.tvName -> listener.onFileClicked(files[adapterPosition])
            }
        }
    }
}