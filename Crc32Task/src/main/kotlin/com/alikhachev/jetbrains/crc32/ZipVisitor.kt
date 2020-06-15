package com.alikhachev.jetbrains.crc32

import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.math.min

interface ZipVisitor {
    fun visitFile(zipFile: ZipFile, entry: ZipEntry)
}

class Crc32SumCounter : ZipVisitor {
    var crc32sum = 0L

    override fun visitFile(zipFile: ZipFile, entry: ZipEntry) {
        if (!entry.name.endsWith(".class")) return
        val crc32 = CRC32()
        val buf = ByteArray(min(entry.size, 4096L).toInt())
        zipFile.getInputStream(entry).use {
            do {
                val read = it.read(buf)
                if (read == -1) break
                crc32.update(buf, 0, read)
            } while (true)
        }

        crc32sum += crc32.value
        val className = entry.name.replace("/", ".").substring(0, entry.name.length - 6)
        println("$className CRC32: ${crc32.value.toString(16)}")
    }
}