package com.alikhachev.jetbrains.crc32

import java.util.zip.ZipEntry

interface ZipEntryVisitor {
    fun visitEntry(entry: ZipEntry)
}

class Crc32SumCounter : ZipEntryVisitor {
    var crc32sum = 0L

    override fun visitEntry(entry: ZipEntry) {
        if (!entry.name.endsWith(".class")) return
        crc32sum += entry.crc
        val className = entry.name.replace("/", ".").substring(0, entry.name.length - 6)
        println("$className CRC32: ${entry.crc.toString(16)}")
    }
}