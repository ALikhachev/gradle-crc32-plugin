package com.alikhachev.jetbrains.crc32

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.zip.ZipFile

class TestCrc32Task {
    @TempDir
    lateinit var testProjectDir: File
    private lateinit var settingsFile: File
    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        settingsFile = File(testProjectDir, "settings.gradle.kts")
        buildFile = File(testProjectDir, "build.gradle.kts")
    }

    @Test
    fun testCrc32Task() {
        settingsFile.writeText("""
            rootProject.name = "test-project"
        """.trimIndent())

        buildFile.writeText("""
            group = "com.example"
            version = "1.0"
            
            plugins {
                java
                id("com.alikhachev.jetbrains.crc32") version ("1.0-SNAPSHOT")
            }
        """.trimIndent())
        val outputFile = File(testProjectDir, "build/libs/test-project-1.0-with-crc.jar")
        assertFalse(outputFile.exists())
        val result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(testProjectDir)
                .withArguments("crc32")
                .build()

        assertEquals(result.task(":crc32")?.outcome, TaskOutcome.SUCCESS)
        assertTrue(outputFile.exists())
        ZipFile(outputFile).use {
            assertEquals(it.comment, "CRC32 sum: 0") // no classes at all
        }
    }
}