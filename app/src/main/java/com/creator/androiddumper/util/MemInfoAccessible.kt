package com.creator.androiddumper.util

import android.os.Environment
import android.text.TextUtils
import com.jaredrummler.android.shell.Shell
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.BufferedWriter
import java.io.File
import java.io.FileFilter
import java.io.FileWriter
import java.util.*
import java.util.regex.Pattern

interface MemInfoAccessible {

    companion object {

        private const val COMMAND_DUMP_MEMINFO = "dumpsys meminfo"

        private const val COMMAND_DUMP_MEMINFO_FORMAT = "dumpsys meminfo %s"

        val OUTPUT_FILE_DIRECTORY = Environment.getExternalStorageDirectory().absolutePath + "/MemInfo/"

        const val TARGET_TOTAL_PACKAGE = "total"

        private const val REGEX_SAVED_FILE_FORMAT = "^mem\\|[a-zA-Z0-9.$:@]+\\|[0-9]+.txt$"

        private val sPatternSavedFile = Pattern.compile(REGEX_SAVED_FILE_FORMAT)!!

        private const val REGEX_SAVED_FILE_FORMAT_OF_PACKAGE = "^mem_%s+\\|[0-9]+.txt\$"
    }

    fun queryMemInfo(activity: RxAppCompatActivity, @Nullable targetPkgName: String?, @NonNull callback: Consumer<String>) {
        val cmd = if (TextUtils.isEmpty(targetPkgName)) COMMAND_DUMP_MEMINFO
        else String.format(Locale.getDefault(), COMMAND_DUMP_MEMINFO_FORMAT, targetPkgName)
        executeCommand(activity, cmd, callback)
    }

    fun executeCommand(activity: RxAppCompatActivity, cmd: String, callback: Consumer<String>) {
        Observable.just(cmd)
                .bindUntilEvent(activity, ActivityEvent.DESTROY)
                .subscribeOn(Schedulers.io())
                .map { Shell.SH.run(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .map { if (it.isSuccessful) it.getStdout() else it.getStderr() }
                .subscribe(callback)
    }

    fun outputMemInfo(activity: RxAppCompatActivity, content: String,
                      targetPkgName: String, callback: Consumer<String>) {
        Observable.just(content)
                .bindUntilEvent(activity, ActivityEvent.DESTROY)
                .subscribeOn(Schedulers.io())
                .map { writeToFile(it, targetPkgName) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback)
    }

    private fun writeToFile(content: String, targetPkgName: String): String {
        val outputDir = File(OUTPUT_FILE_DIRECTORY)
        if (outputDir.isFile && !outputDir.delete())
            throw IllegalStateException("Cannot delete file ${outputDir.absolutePath}")
        if (!outputDir.exists() && !outputDir.mkdirs())
            throw IllegalStateException("Cannot create directory ${outputDir.absolutePath}")
        val outputName = "${OUTPUT_FILE_DIRECTORY}mem|$targetPkgName|${System.currentTimeMillis()}.txt"
        val outputFile = File(outputName)
        if ((outputFile.exists() || outputFile.isDirectory) && !outputFile.delete())
            throw IllegalStateException("Cannot delete file ${outputFile.absolutePath}")
        if (!outputFile.createNewFile()) throw IllegalStateException("Cannot create file ${outputFile.absolutePath}")
        val writer = FileWriter(outputFile)
        val bufferWriter = BufferedWriter(writer)
        bufferWriter.write(content)
        bufferWriter.close()
        writer.close()
        return outputFile.absolutePath
    }

    fun loadSavedFiles(pkgName: String? = null): Array<InfoFile> {
        val outputDir = File(OUTPUT_FILE_DIRECTORY)
        if (!outputDir.exists() || outputDir.isFile) return emptyArray()
        val files: Array<File> = if (pkgName == null)
            outputDir.listFiles(FileFilter { it.isFile && sPatternSavedFile.matcher(it.name).matches() })
        else {
            val pattern = Pattern.compile(String.format(REGEX_SAVED_FILE_FORMAT_OF_PACKAGE, pkgName))
            outputDir.listFiles(FileFilter { it.isFile && pattern.matcher(it.name).matches() })
        }
        return Array(files.size) {
            val currentFile = files[it]
            val fileName = currentFile.name.substring(0, currentFile.name.length - 4)
            val filePkgName = fileName.split("|")[1]
            InfoFile(currentFile.absolutePath, filePkgName, currentFile.lastModified())
        }
    }
}