package com.creator.androiddumper.util

class InfoFile(val name: String, val absolutePath: String, val packageName: String,
               val lastModified: Long, val length: Long) : Comparable<InfoFile> {

    override fun compareTo(other: InfoFile): Int {
        return when {
            packageName == MemInfoAccessible.TARGET_TOTAL_PACKAGE &&
                    other.packageName != MemInfoAccessible.TARGET_TOTAL_PACKAGE
                    || packageName > other.packageName
                    || lastModified > other.lastModified
            -> 1
            packageName != MemInfoAccessible.TARGET_TOTAL_PACKAGE &&
                    other.packageName == MemInfoAccessible.TARGET_TOTAL_PACKAGE
                    || packageName < other.packageName
                    || lastModified < other.lastModified
            -> -1
            else -> 0
        }
    }
}