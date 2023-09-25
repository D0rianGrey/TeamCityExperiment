import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2023.05"

project {
    buildType(Build)
}

object Build : BuildType({
    name = "Test"

    publishArtifacts = PublishMode.ALWAYS

    params {
        param("env", "cloud_chrome")
        param("url", "https://www.google.com.ua/")
        param("testParam", "1")
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            name = "Test"
            goals = "clean test -Denv=%env% -Durl=%url%"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }

        // Step to generate Allure report
        exec {
            name = "Generate Allure Report"
            path = "allure"  // Make sure allure is available in PATH
            arguments =
                "generate %teamcity.build.checkoutDir%/allure-results -o %teamcity.build.checkoutDir%/allure-report"
        }
    }

    triggers {
        vcs { }
    }

    features {
        perfmon { }
    }

    // Publish Allure report as build artifact
    artifacts {
        artifactRules = """
            +:%teamcity.build.checkoutDir%/allure-report => allure-report.zip
        """.trimIndent()
    }
})
