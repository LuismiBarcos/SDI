package org.sdi.injector

import java.io.File
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.jar.JarFile

class JarHandler {
    private var usingWorkspace = false
    private val workspace = File(Files.createDirectory(Paths.get("${System.getProperty("user.dir")}${File.separator}temp${UUID.randomUUID()}")).toUri())

    fun removeTempWorkspace() {
        if(usingWorkspace) {
            deleteDirectory(workspace)
        }
    }

    private fun deleteDirectory(directory: File) {
        // if the file is directory or not
        if (directory.isDirectory) {
            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    deleteDirectory(file)
                }
            }
        }
        directory.delete()
    }

    fun unzipJar(clazz: Class<*>, packageName: String): File {
        usingWorkspace = true

        val jarFile = JarFile(URI(clazz.protectionDomain.codeSource.location.file).path)

        Files.copy(
            Paths.get(jarFile.name),
            Paths.get("${workspace.path}${File.separator}${jarFile.name.split("/").last()}")
        )

        val unzipCommand = "jar -xvf ${jarFile.name.split("/").last()}".split("\\s+".toRegex())
        execute(workspace, unzipCommand)

        return File("${workspace.path}${File.separator}$packageName")
    }

    private fun execute(directory: File, command: List<String>) {
        val executionProcess = ProcessBuilder()
            .command(command)
            .directory(directory)
            .start()
        val normalExecutionMessages = Thread { showCommandMessages(executionProcess.inputStream) }
        val errorExecutionMessages = Thread { showCommandMessages(executionProcess.errorStream) }
        normalExecutionMessages.start()
        errorExecutionMessages.start()
        normalExecutionMessages.join()
        errorExecutionMessages.join()
        val processExit = executionProcess.waitFor()
        if(processExit != 0) {
            throw Exception()
        }
    }

    private fun showCommandMessages(stream: InputStream) {
        stream.reader(Charsets.UTF_8).use { }
    }
}