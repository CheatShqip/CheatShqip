package com.cheatshqip.buildlogic

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.ByteArrayOutputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Base64
import java.util.Properties

private const val POLLING_INTERVAL_MS = 3_000L

class SonarReportPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("sonarReport", SonarReportTask::class.java) {
            rootDirectory.set(target.layout.projectDirectory)
            sonarScannerExecutable.set(resolveSonarScanner())
        }
    }

    private fun resolveSonarScanner(): String {
        val path = System.getenv("PATH") ?: return "sonar-scanner"
        return path.split(java.io.File.pathSeparator)
            .map { dir -> java.io.File(dir, "sonar-scanner") }
            .firstOrNull { file -> file.canExecute() }
            ?.absolutePath
            ?: "sonar-scanner"
    }
}

@Suppress("NewApi")
abstract class SonarReportTask : DefaultTask() {

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val rootDirectory: DirectoryProperty

    @get:Input
    abstract val sonarScannerExecutable: Property<String>

    @TaskAction
    fun run() {
        val rootDir = rootDirectory.get().asFile

        val localProps = Properties().apply {
            rootDir.resolve("local.properties").inputStream().use(::load)
        }
        val sonarProps = Properties().apply {
            rootDir.resolve("sonar-project.properties").inputStream().use(::load)
        }

        val config = SonarConfig(
            hostUrl = sonarProps.getProperty("sonar.host.url").trimEnd('/'),
            token = localProps.getProperty("sonar.token")?.takeIf { it.isNotBlank() }
                ?: System.getenv("SONAR_TOKEN")
                ?: throw GradleException("sonar.token not set in local.properties and SONAR_TOKEN env var is missing"),
            projectKey = sonarProps.getProperty("sonar.projectKey"),
            organization = sonarProps.getProperty("sonar.organization"),
        )

        val taskId = runScanner(rootDir, config.token)
        waitForCompletion(config, taskId)
        printIssues(config)
    }

    private fun runScanner(rootDir: java.io.File, token: String): String {
        logger.lifecycle("Running SonarQube analysis...")
        val output = ByteArrayOutputStream()
        val process = ProcessBuilder(sonarScannerExecutable.get())
            .directory(rootDir)
            .redirectErrorStream(true)
            .also { it.environment()["SONAR_TOKEN"] = token }
            .start()

        process.inputStream.bufferedReader().forEachLine { line ->
            logger.lifecycle(line)
            output.write((line + "\n").toByteArray())
        }
        if (process.waitFor() != 0) throw GradleException("sonar-scanner failed")

        return output.toString().lines()
            .firstOrNull { "api/ce/task?id=" in it }
            ?.substringAfter("id=")
            ?.trim()
            ?: throw GradleException("Could not extract task ID from sonar-scanner output")
    }

    private fun waitForCompletion(config: SonarConfig, taskId: String) {
        logger.lifecycle("\nWaiting for server to process analysis (task: $taskId)...")
        while (true) {
            @Suppress("UNCHECKED_CAST")
            val task = (httpGet(config, "api/ce/task?id=$taskId") as Map<String, Any>)["task"]
                as Map<String, Any>
            val status = task["status"] as String
            logger.lifecycle("  Status: $status")
            when (status) {
                "SUCCESS" -> return
                "FAILED", "CANCELLED" -> throw GradleException("Analysis $status")
                else -> Thread.sleep(POLLING_INTERVAL_MS)
            }
        }
    }

    private fun printIssues(config: SonarConfig) {
        logger.lifecycle("\nFetching issues...")
        val url = "api/issues/search" +
            "?projectKeys=${config.projectKey}&organization=${config.organization}" +
            "&ps=500&resolved=false"
        @Suppress("UNCHECKED_CAST")
        val issues = (httpGet(config, url) as Map<String, Any>)["issues"]
            as List<Map<String, Any>>

        logger.lifecycle("=== ${issues.size} open issue(s) ===\n")
        issues.forEach { issue ->
            val component = (issue["component"] as String).substringAfterLast(":")
            val line = issue["line"] ?: "?"
            val severity = issue["severity"] ?: "UNKNOWN"
            val message = issue["message"] ?: ""
            val rule = issue["rule"] ?: ""
            logger.lifecycle("[$severity] $component:$line")
            logger.lifecycle("  Rule   : $rule")
            logger.lifecycle("  Message: $message\n")
        }
    }

    private fun httpGet(config: SonarConfig, path: String): Any {
        val auth = Base64.getEncoder().encodeToString("${config.token}:".toByteArray())
        val response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder()
                .uri(URI.create("${config.hostUrl}/$path"))
                .header("Authorization", "Basic $auth")
                .GET().build(),
            HttpResponse.BodyHandlers.ofString()
        )
        return JsonSlurper().parseText(response.body())
    }
}

private data class SonarConfig(
    val hostUrl: String,
    val token: String,
    val projectKey: String,
    val organization: String,
)