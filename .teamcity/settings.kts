import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2023.05"

project {

    buildType(Build)

    features {
        feature {
            type = "allureReport"
            param("allureRunnerMode", "runner")
            param("allureReportGenerate", "true")
            param("allureResultsPattern", "allure-results/**")
        }

        buildReportTab {
            title = "Test Results"
            startPage = "allure-report/index.html" // Point to unzipped directory
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
            name = "Test with creation allure-results"
            goals = "clean test -Denv=%env% -Durl=%url%"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }

        maven {
            name = "Generate Allure Report from allure-results"
            goals = "allure:report"
        }
    }

    artifactRules = """
        +:allure-report => allure-report
        +:allure-report/history => allure-report/history
    """

    dependencies {
        artifacts(Build) {
            artifactRules = """
                allure-report/history => allure-results/history
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
