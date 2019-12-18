package com.ifanr.tangzhi

import android.app.Application
import android.os.Environment
import java.io.File
import javax.inject.Inject

interface AppConfig {

    // DCIM 里的 app 文件夹
    val imageFolderInDCIM: File
}

class AppConfigImpl @Inject constructor(
    private val app: Application
): AppConfig {

    companion object {
        private const val FOLDER_IN_DCIM = "tangzhi"
    }

    override val imageFolderInDCIM: File
        get() {
            val folder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                FOLDER_IN_DCIM)
            folder.mkdirs()
            return folder
        }
}