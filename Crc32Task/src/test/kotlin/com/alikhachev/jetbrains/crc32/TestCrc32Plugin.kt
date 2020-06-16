package com.alikhachev.jetbrains.crc32

import org.gradle.api.plugins.BasePlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.Assertions.assertEquals

class TestCrc32Plugin {
    @Test
    fun testAddsTask() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.alikhachev.jetbrains.crc32")
        val task = project.tasks.getByName("crc32")
        assertThat(project.plugins.getPlugin("com.alikhachev.jetbrains.crc32"), instanceOf(Crc32Plugin::class.java))
        assertThat(task, instanceOf(Crc32Task::class.java))
        assertEquals(project.tasks.withType(Crc32Task::class.java).size, 1)
        assertEquals(task.group, BasePlugin.BUILD_GROUP)
    }

    @Test
    fun testOnEmptyProject() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.alikhachev.jetbrains.crc32")
        val task = project.tasks.getByName("crc32") as Crc32Task
        assertEquals(task.inputJars.files.size, 0)
        assertEquals(task.outputJars.size, 0)
    }

    @Test
    fun testOnJavaProject() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.alikhachev.jetbrains.crc32")
        project.pluginManager.apply("java")
        val task = project.tasks.getByName("crc32") as Crc32Task
        assertEquals(task.inputJars.files.size, 1)
        assertEquals(task.outputJars.size, 1)
    }
}