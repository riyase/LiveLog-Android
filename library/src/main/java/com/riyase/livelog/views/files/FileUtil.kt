package com.riyase.livelog.views.files

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import com.riyase.livelog.BuildConfig
import java.io.File

class FileUtil {

    companion object {
        fun getAppDir(context: Context) : File {
            val packageManager = context.packageManager
            val packageName = context.packageName
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return File(packageInfo.applicationInfo.dataDir)
        }

        fun getStorageDir(context: Context) : File {
            return Environment.getExternalStorageDirectory()
        }

        fun openFile(context: Context, file: File) {
            val contentUri = FileProvider.getUriForFile(
                context,
                "",//BuildConfig.FILE_PROVIDER_AUTHORITY,
                File(file.absolutePath));
            val intent = Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, getMimeType(context, file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        fun getMimeType(context: Context, file: File) : String {
            return ""
        }

        fun createDir(dir: File, name : String) {
            val newDir = File(dir, name)
            newDir.mkdir()
        }

        fun createFile(dir : File, name : String, content : String) {
            val file = File(dir, name)
            file.createNewFile()
            file.writeText(content)
        }
    }

}