package com.demo.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.demomoduleads.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


object FileUtils {

    fun deleteFile(file: File?): Boolean {
        return if (file?.exists() == true) {
            file.delete()
        } else {
            true
        }
    }

    fun shareFile(context: Context, file: File?) {
        if (file != null) {
            val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            ShareCompat.IntentBuilder(context)
                .setType("image/*")
                .setSubject(context.getString(R.string.share_photo))
                .addStream(fileUri)
                .setChooserTitle(context.getString(R.string.share_photo))
                .startChooser()
        }
    }

    fun writeFileBytes(context: Context, bytes: ByteArray, folderName: String): File? {
        return try {
            val newFile = newFile(context, folderName, "${UUID.randomUUID()}.png")

            if (writeFileByte(newFile, bytes)) {
                newFile
            } else {
                null
            }
        } catch (ex: Exception) {
            null
        }
    }

    fun writeFileByte(file: File, bytes: ByteArray): Boolean {
        return try {
            val stream = FileOutputStream(file.path)
            stream.write(bytes)
            stream.close()

            true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    private fun newFile(context: Context, folderName: String, fileName: String): File {
        return File(getFolder(context, folderName), fileName)
    }

    private fun getFolder(context: Context, folderName: String): File {
//        val folder = File("${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/${folderName}")
        val folder = File("${context.filesDir}/${folderName}")
        if (!folder.exists()) folder.mkdirs()
        return folder
    }

    fun getFileFromContentUri(context: Context, contentUri: Uri, uniqueName: Boolean): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri) ?: ""
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = ("temp_file_" + if (uniqueName) timeStamp else "") + ".$fileExtension"
        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()
        // Initialize streams
        var oStream: FileOutputStream? = null
        var inputStream: InputStream? = null

        try {
            oStream = FileOutputStream(tempFile)
            inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let { copy(inputStream, oStream) }
            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Close streams
            inputStream?.close()
            oStream?.close()
        }

        return tempFile
    }

    fun fileFromContentUri(context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? =
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri))
        else uri.path?.let { MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(it)).toString()) }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }
}