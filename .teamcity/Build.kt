import scripts.AllureReportScripts.applyAllureReportScripts
import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.PublishMode
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.vcs

// Setting for Build
object Build : BuildType({
    name = "Test"

    publishArtifacts = PublishMode.ALWAYS

    params {
        param("env", "cloud_chrome")
        param("url", "https://www.google.com.ua/")
        param(
            "webhook_url",
            "https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
        )
        param(
            "allure_report_url",
            "http://localhost:8111/buildConfiguration/TeamCityExperiment_Build/%teamcity.build.id%?buildTab=report_project1_Test_Results"
        )
        param("prometheusData", "allure-report/export/prometheusData.txt")
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

        applyAllureReportScripts();
    }

    artifactRules = """
        +:allure-report => allure-report
        +:allure-report/history => allure-report/history
    """

    dependencies {
        artifacts(Build) {
            id = "ARTIFACT_DEPENDENCY"
            buildRule = lastSuccessful()
            artifactRules = """
                allure-report/history/* => allure-results/history
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
