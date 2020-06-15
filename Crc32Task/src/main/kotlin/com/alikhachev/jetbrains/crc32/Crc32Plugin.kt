package com.alikhachev.jetbrains.crc32

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.bundling.Jar
import java.io.File

class Crc32Plugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("crc32", Crc32Task::class.java) {
            val inputJars = mutableListOf<File>()
            val outputJars = mutableMapOf<String, File>()
            project.tasks.withType(Jar::class.java).forEach { jarTask ->
                it.dependsOn(jarTask.name)
                val jarFile = jarTask.outputs.files.singleFile
                inputJars.add(jarFile)
                outputJars[jarFile.absolutePath] = File(jarFile.parent, jarFile.name.replace(".jar", "-with-crc.jar"))
            }

            it.group = BasePlugin.BUILD_GROUP
            it.description = "Writes sum of all jar classes CRC32 into comment"
            it.inputJars = project.layout.files(inputJars)
            it.outputJars = outputJars
        }
    }
}