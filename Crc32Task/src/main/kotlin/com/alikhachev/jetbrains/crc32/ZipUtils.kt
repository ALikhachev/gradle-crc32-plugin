package com.alikhachev.jetbrains.crc32

import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream
import org.apache.commons.compress.archivers.zip.ZipFile
import java.io.File
import java.io.FileOutputStream

/**
 * Don't forget to close returned output stream!
 */
fun copyZip(input: File, output: File, visitor: ZipEntryVisitor): JarArchiveOutputStream {
    ZipFile(input).use { zipFile ->
        val outputStream = JarArchiveOutputStream(FileOutputStream(output))
        for (entry in zipFile.entries) {
            if (!entry.isDirectory) {
                visitor.visitEntry(entry)
            }
            outputStream.addRawArchiveEntry(entry, zipFile.getRawInputStream(entry))
        }
        return outputStream
    }
}