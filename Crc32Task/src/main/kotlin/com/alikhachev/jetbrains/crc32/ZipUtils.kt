package com.alikhachev.jetbrains.crc32

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * Don't forget to close returned output stream!
 * @return
 */
fun copyZip(input: File, output: File, visitor: ZipVisitor): ZipOutputStream {
    ZipFile(input).use { zipFile ->
        val zipEntries = zipFile.entries()
        val zos = ZipOutputStream(FileOutputStream(output))
        for (entry in zipEntries) {
            zos.putNextEntry(entry)
            if (!entry.isDirectory) {
                visitor.visitFile(zipFile, entry)
                zipFile.getInputStream(entry).use {
                    it.copyTo(zos)
                }
            }
            zos.closeEntry()
        }
        return zos
    }
}