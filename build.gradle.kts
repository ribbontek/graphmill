import org.apache.tools.ant.taskdefs.condition.Os
import org.apache.tools.ant.taskdefs.condition.Os.FAMILY_WINDOWS
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("pl.allegro.tech.build.axion-release") version "1.14.2"
}

group = "com.ribbontek"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation(kotlin("test"))
    testImplementation("com.github.javafaker:javafaker:1.0.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks {
    register<Copy>("copyGitHooks") {
        description = "Copies the git hooks from git-hooks to the .git folder."
        group = "GIT_HOOKS"
        from("$rootDir/git-hooks/") {
            include("**/*.sh")
            rename("(.*).sh", "$1")
        }
        into("$rootDir/.git/hooks")
    }

    register<Exec>("installGitHooks") {
        description = "Installs the git hooks from the git-hooks folder"
        group = "GIT_HOOKS"
        workingDir(rootDir)
        commandLine("chmod")
        args("-R", "+x", ".git/hooks/")
        dependsOn(named("copyGitHooks"))
        onlyIf {
            !Os.isFamily(FAMILY_WINDOWS)
        }
        doLast {
            logger.info("Git hooks installed successfully.")
        }
    }

    register<Delete>("deleteGitHooks") {
        description = "Delete the git hooks."
        group = "GIT_HOOKS"
        delete(fileTree(".git/hooks/"))
    }

    register("versionFile") {
        doLast {
            mkdir("${project.buildDir}/version")
            file("${project.buildDir}/version/version").writeText(scmVersion.version)
            project.version = scmVersion.version
        }
    }

    afterEvaluate {
        tasks["clean"].dependsOn(tasks.named("installGitHooks"))
        tasks["build"].dependsOn(tasks.named("versionFile"))
    }
}

scmVersion {
    tag {
        // if no tags exists, this sets the starting position
        initialVersion { _, _ -> "1.0.0" }
    }
    // Use minor, not patch by default. e.g. 1.0.0 -> 1.1.0
    versionIncrementer("incrementMinor")
    // Adds branch names to snapshots. e.g. 1.0.0-feature-SW-000-exion-release-plugin-SNAPSHOT
    branchVersionCreator.putAll(
        mapOf(
            "feature/.*" to "versionWithBranch",
            "hotfix/.*" to "versionWithBranch"
        )
    )
}
