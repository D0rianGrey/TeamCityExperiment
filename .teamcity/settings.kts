import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2023.05"

project {

    buildType(Build)
//
//    features {
//        feature {
//            id = "1"
//            type = "ReportTab"
//            param("startPage", "allure-report/index.html")
//            param("title", "Allure Report")
//        }
//    }


//
//    features {
//        buildReportTab {
//            id = "PROJECT_EXT_52"
//            title = "Test Results"
////            startPage = "allure-report.zip!/allure-report/index.html"
//            startPage = "./allure-report/index.html"
//        }
    //        feature {
//            id = "1"
//            type = "ReportTab"
//            param("startPage", "allure-report/index.html")
//            param("title", "Allure Report")
//        }
//    }

    features {
        feature {
            type = "allureReport"
            param("allureRunnerMode", "runner")
            param("allureReportGenerate", "true")
            param("allureResultsPattern", "allure-results/**")
        }

        buildReportTab {
            id = "PROJECT_EXT_52"
            title = "Test Results"
//            startPage = "allure-report.zip!/allure-report/index.html"
            startPage = "./allure-report/index.html"
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

//    artifactRules = """
//        +:allure-report => allure-report.zip
//    """

    artifactRules = """
    +:allure-report => .
"""

//    features {
//        feature {
//            id = "1"
//            type = "ReportTab"
//            param("startPage", "allure-report/index.html")
//            param("title", "Allure Report")
//        }
//    }

    triggers {
        vcs { }
    }

    features {
        perfmon { }
    }
})
