import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
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
            name = "Test with creation allure-results"
            goals = "clean test -Denv=%env% -Durl=%url%"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }

        maven {
            name = "Generate Allure Report from allure-results"
            goals = "allure:serve"
        }
//
//        script {
//            name = "Install allure command line"
//            scriptContent = """
//                #!/bin/bash
//                wget https://github.com/allure-framework/allure2/releases/download/2.13.8/allure-2.13.8.tgz
//                tar -zxvf allure-2.13.8.tgz -C /opt/
//                export PATH=$PATH:/opt/allure-2.13.8/bin
//            """
//        }

//        script {
//            scriptContent = """
//                #!/bin/bash
//                allure generate ./allure-results --clean -o ./allure-report
//            """
//        }
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
