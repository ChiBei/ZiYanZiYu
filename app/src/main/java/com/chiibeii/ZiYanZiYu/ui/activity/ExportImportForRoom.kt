package com.chiibeii.ZiYanZiYu.ui.activity

import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.chiibeii.ZiYanZiYu.ui.activity.MyApplication.Companion.context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import android.os.Bundle

import android.app.Activity
import java.nio.channels.FileChannel


class ExportImportForRoom : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //creating a new folder for the database to be backuped to
        val direct = File(Environment.getExternalStorageDirectory().toString() + "/Exam Creator")
        if (!direct.exists() && direct.mkdir()) {
            //directory is created;
        }
        exportDB()
        importDB()
    }

    //importing database
    private fun importDB() {
        try {
            val sd = Environment.getExternalStorageDirectory()
            val data = Environment.getDataDirectory()
            if (sd.canWrite()) {
                val currentDBPath = ("//data//" + "com.chiibeii.ZiYanZiYu"
                        + "//databases//" + "blogItem_database")
                val backupDBPath = "/BackupFolder/blogItem_database"
                val backupDB = File(data, currentDBPath)
                val currentDB = File(sd, backupDBPath)
                val src: FileChannel = FileInputStream(currentDB).channel
                val dst: FileChannel = FileOutputStream(backupDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
                Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(baseContext, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    //exporting database
    private fun exportDB() {
        try {
            val sd = Environment.getExternalStorageDirectory()
            val data = Environment.getDataDirectory()
            if (sd.canWrite()) {
                val currentDBPath = ("//data//" + "com.chiibeii.ZiYanZiYu"
                        + "//databases//" + "blogItem_database")
                val backupDBPath = "/BackupFolder/blogItem_database"
                val currentDB = File(data, currentDBPath)
                val backupDB = File(sd, backupDBPath)
                val src: FileChannel = FileInputStream(currentDB).channel
                val dst: FileChannel = FileOutputStream(backupDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
                Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(baseContext, e.toString(), Toast.LENGTH_LONG).show()
        }
    }


    private fun exportDbFile() {

        try {

            //Existing DB Path
            val DB_PATH = "/data/com.chiibeii.ZiYanZiYu/databases/blogItem_database"
            val DATA_DIRECTORY = Environment.getDataDirectory()
            val INITIAL_DB_PATH = File(DATA_DIRECTORY, DB_PATH)

            //COPY DB PATH
            val EXTERNAL_DIRECTORY: File = Environment.getExternalStorageDirectory()
            val COPY_DB = "/mynewfolder/mydb.db"
            val COPY_DB_PATH = File(EXTERNAL_DIRECTORY, COPY_DB)

            File(COPY_DB_PATH.parent!!).mkdirs()
            val srcChannel = FileInputStream(INITIAL_DB_PATH).channel

            val dstChannel = FileOutputStream(COPY_DB_PATH).channel
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size())
            srcChannel.close()
            dstChannel.close()

        } catch (excep: Exception) {
            Toast.makeText(context, "ERROR IN COPY $excep", Toast.LENGTH_LONG).show()
            Log.e("FILECOPYERROR>>>>", excep.toString())
            excep.printStackTrace()
        }

    }


}




