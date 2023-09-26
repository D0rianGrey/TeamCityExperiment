import jetbrains.buildServer.configs.kotlin.BuildStep
import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.PublishMode
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

// Setting for Build
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

//        script {
//            name = "Set Allure Report URL"
//            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
//            scriptContent = """
//                    echo "##teamcity[setParameter name='env.ALLURE_REPORT_URL' value='http://localhost:8111/buildConfiguration/TeamCityExperiment_Build/%teamcity.build.id%?buildTab=report_project1_Test_Results']"
//                """.trimIndent()
//        }

//        script {
//            name = "Send Allure report to Microsoft Teams"
//            executionMode = BuildStep.ExecutionMode.ALWAYS
//            scriptContent = """
//                    WEBHOOK_URL="https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
//                    ALLURE_REPORT_URL="%env.ALLURE_REPORT_URL%"
//                    curl -H "Content-Type: application/json" -d "{\"text\": \"Allure Report is available at $ALLURE_REPORT_URL\"}" $WEBHOOK_URL
//                """.trimIndent()
//        }
//        script {
//            name = "Send Allure report to Microsoft Teams"
//            executionMode = BuildStep.ExecutionMode.ALWAYS
//            scriptContent = """
//                    WEBHOOK_URL="https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
//                    curl -H "Content-Type: application/json" -d "{\"text\": \"Allure Report is available at"}"
//                """.trimIndent()
//        }

        script {
            name = "Send 'Hello' to teams"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = """
                WEBHOOK_URL="https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
                curl -H "Content-Type: application/json" -d "{\"text\": \"Hello\"}" $WEBHOOK_URL
                            """.trimIndent()
        }
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
