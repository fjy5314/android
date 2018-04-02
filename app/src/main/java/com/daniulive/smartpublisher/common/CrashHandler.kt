package com.daniulive.smartpublisher.common

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*


/**
 * Created by flny on 2018/3/28.
 */
object CrashHandler : Thread.UncaughtExceptionHandler {

    fun init(ctx: Context) {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 核心方法，当程序crash 会回调此方法， Throwable中存放这错误日志
     */
    override fun uncaughtException(arg0: Thread, arg1: Throwable) {

        val logPath: String
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            logPath = ("/sdcard/JScom/"
                    + File.separator
                    + File.separator
                    + "log")

            val file = File(logPath)
            if (!file.exists()) {
                file.mkdirs()
            }
            try {
                val fw = FileWriter(logPath + File.separator
                        + "errorlog.log", true)
                fw.write(Date().toString() + "\n")
                // 错误信息
                // 这里还可以加上当前的系统版本，机型型号 等等信息
                val stackTrace = arg1.stackTrace
                fw.write(arg1.message + "\n")
                for (i in stackTrace.indices) {
                    fw.write("file:" + stackTrace[i].fileName + " class:"
                            + stackTrace[i].className + " method:"
                            + stackTrace[i].methodName + " line:"
                            + stackTrace[i].lineNumber + "\n")
                }
                fw.write("\n")
                fw.close()
                // 上传错误信息到服务器
                // uploadToServer();
            } catch (e: IOException) {
                Log.e("crash handler", "load file failed...", e.cause)
            }

        }
        arg1.printStackTrace()
        android.os.Process.killProcess(android.os.Process.myPid())
    }



}