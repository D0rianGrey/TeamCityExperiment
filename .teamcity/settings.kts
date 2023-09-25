import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2023.05"

project {
    buildType(Build)
    features {
        buildReportTab {
            id = "PROJECT_EXT_52"
            title = "Test Results"
            startPage = "allure-report.zip!/allure-report/index.html"
        }
    }
}

object Build : BuildType({
    name = "Test"
    artifactRules = """
        +:allure-report => allure-report.zip
    """
    publishArtifacts = PublishMode.ALWAYS

    // Define common paths as parameters
    params {
//        param("allureResultsPath", "%teamcity.build.checkoutDir%/allure-results")
//        param("allureReportPath", "%teamcity.build.checkoutDir%/allure-report")
        param("env", "cloud_chrome")
        param("url", "https://www.google.com.ua/")
//        param("teamcity.artifacts.prepublishingCommands", "move /Y %allureReportPath% allure-report")
//        param("artifacts", "%allureReportPath% => allure-report.zip")
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

//        // Use the parameterized paths
//        exec {
//            name = "Generate Allure Report"
//            path = "allure"
//            arguments = "generate %allureResultsPath% -o %allureReportPath%"
//        }
//        step {
//            name = "Generate test report"
//            type = "allureReportGeneratorRunner"
//            param("allure.result.directory", "allure-results")
//            param("allure.report.path.prefix", "allure-report")
//            executionMode = BuildStep.ExecutionMode.ALWAYS
//        }

        script {
            scriptContent = """
                #!/bin/bash
                allure generate ./allure-results --clean -o ./allure-report
            """
        }
    }

    triggers {
        vcs { }
    }

    features {
        perfmon { }
    }
})
