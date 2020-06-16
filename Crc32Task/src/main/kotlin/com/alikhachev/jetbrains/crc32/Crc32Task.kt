package com.alikhachev.jetbrains.crc32

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import java.io.File

@CacheableTask
open class Crc32Task : DefaultTask() {
    @get:Incremental
    @get:InputFiles
    @get:Classpath
    @get:SkipWhenEmpty
    @get:PathSensitive(PathSensitivity.RELATIVE)
    lateinit var inputJars: FileCollection

    @get:OutputFiles
    lateinit var outputJars: Map<String, File>

    @TaskAction
    fun computeCrc32Sum(inputChanges: InputChanges) {
        if (inputChanges.isIncremental) {
            println("Incremental mode")
        } else {
            println("Non-incremental mode")
        }

        inputChanges.getFileChanges(inputJars).forEach { change ->
            val inputJar = change.file
            val outputJar = outputJars[inputJar.absolutePath]
            if (outputJar == null) {
                println("Output jar for $inputJar is not configured!")
                return;
            }
            fillCrc32Comment(inputJar, outputJar)
        }
    }
}

fun fillCrc32Comment(inputJar: File, outputJar: File) {
    val sumCounter = Crc32SumCounter()
    copyZip(inputJar, outputJar, sumCounter).use { outputStream ->
        val comment = "CRC32 sum: ${sumCounter.crc32sum.toString(16)}"
        println("${inputJar.name} $comment")
        outputStream.setComment(comment)
    }
}