package file.founder

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FileFounder(private val context: Context) {

    var isShouldRequestPermission = false
    private var isRequestedPermission = false
    var requestCode = 1


    fun findFiles(dir: File, pattern: String, file: (file: String) -> Unit) {
        try {
            if (isShouldRequestPermission && !isRequestedPermission) {
                requestPermission()
            }

            CoroutineScope(Dispatchers.IO).launch {
                val files = dir.listFiles()
                if (files != null) {
                    for (i in files.indices) {
                        if (files[i].isDirectory) {
                            findFiles(files[i], pattern, file)
                        } else {
                            if (files[i].name.endsWith(pattern)) {
                                file.invoke(files[i].absolutePath)
                            }
                        }
                    }
                }
            }
        } catch (e:Throwable) {
            e.printStackTrace()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            (context as Activity),
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            requestCode
        )
        isRequestedPermission = true
    }
}