import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2023.05"

project {
    buildType(Build)
//    features {
//        buildReportTab {
//            id = "PROJECT_EXT_52"
//            title = "Test Results"
//            startPage = "allure-report.zip!/allure-report/index.html"
//        }
//    }
    features {
        feature {
            type = "allureReport"
            param("allureRunnerMode", "runner")
            param("allureReportGenerate", "true")
            param("allureResultsPattern", "allure-results/**")
        }
    }
}

object Build : BuildType({
    name = "Test"

    publishArtifacts = PublishMode.ALWAYS

    params {
        param("env", "cloud_chrome")
        param("url", "https://www.google.com.ua/")
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

        script {
            scriptContent = """
                #!/bin/bash
                allure generate ./allure-results --clean -o ./allure-report
            """
        }
    }

    artifactRules = """
        +:allure-report => allure-report.zip
    """

    triggers {
        vcs { }
    }

    features {
        perfmon { }
    }
})
