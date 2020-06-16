package com.alikhachev.jetbrains.crc32

import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.zip.ZipFile

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.FileOutputStream
import java.io.IOException

class TestCrc2Calculate {
    @Test
    fun testFile1(@TempDir tempDir: File) {
        testNormalFile(tempDir, "/Application-1.0-SNAPSHOT.jar", "9574c1b8bb")
    }

    @Test
    fun testFile2(@TempDir tempDir: File) {
        testNormalFile(tempDir, "/crc32-1.0-SNAPSHOT.jar", "3a22ff21d")
    }

    @Test
    fun testIncorrectFile(@TempDir tempDir: File) {
        val inputJar = File(tempDir, "input.jar")
        val outputJar = File(tempDir, "output.jar")
        prepareInputFile(inputJar, "/empty.txt")
        Assertions.assertThrows(IOException::class.java) {
            fillCrc32Comment(inputJar, outputJar)
        }
    }

    private fun testNormalFile(tempDir: File, jarPath: String, crc32Sum: String) {
        val inputJar = File(tempDir, "input.jar")
        val outputJar = File(tempDir, "output.jar")
        prepareInputFile(inputJar, jarPath)
        fillCrc32Comment(inputJar, outputJar)
        ZipFile(inputJar).use { inputFile ->
            ZipFile(outputJar).use { outputFile ->
                val inputEntries = inputFile.entries()
                for (entry in outputFile.entries()) {
                    assertTrue(IOUtils.contentEquals(outputFile.getInputStream(entry), inputFile.getInputStream(inputEntries.nextElement())))
                }
                assertThat(inputFile.comment, nullValue())
                assertEquals(outputFile.comment, "CRC32 sum: $crc32Sum")
            }
        }
    }

    private fun prepareInputFile(input: File, resourcePath: String) {
        TestCrc2Calculate::class.java.getResource(resourcePath).openStream().use {
            FileOutputStream(input).use { out ->
                it.copyTo(out)
            }
        }
    }
}