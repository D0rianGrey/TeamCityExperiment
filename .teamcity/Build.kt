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
        param(
            "webhook_url",
            "https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
        )
        param(
            "allure_report_url",
            "http://localhost:8111/buildConfiguration/TeamCityExperiment_Build/%teamcity.build.id%?buildTab=report_project1_Test_Results"
        )
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

        script {
            name = "Get Test Metrics from Allure Report"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = """
        PASSED_COUNT=${'$'}(awk '/launch_status_passed/ {print $2}' allure-report/export/prometheusData.txt)
        FAILED_COUNT=${'$'}(awk '/launch_status_failed/ {print $2}' allure-report/export/prometheusData.txt)
        SKIPPED_COUNT=${'$'}(awk '/launch_status_skipped/ {print $2}' allure-report/export/prometheusData.txt)
        DURATION=${'$'}(awk '/launch_time_duration/ {print $2}' allure-report/export/prometheusData.txt)
        echo "##teamcity[setParameter name='env.PASSED_TESTS' value='${'$'}PASSED_COUNT']"
        echo "##teamcity[setParameter name='env.FAILED_TESTS' value='${'$'}FAILED_COUNT']"
        echo "##teamcity[setParameter name='env.SKIPPED_TESTS' value='${'$'}SKIPPED_COUNT']"
        echo "##teamcity[setParameter name='env.DURATION' value='${'$'}DURATION']"
    """.trimIndent()
        }

        script {
            name = "Send Adaptive Card to Microsoft Teams as Allure report"
            executionMode = BuildStep.ExecutionMode.ALWAYS
//            val PAYLOAD = """
//                {
//  "@context": "http://schema.org/extensions",
//  "@type": "MessageCard",
//  "themeColor": "0076D7",
//  "title": "Allure Report",
//  "text": "This is a simplified report after the job is completed",
//  "sections": [
//    {
//      "startGroup": true,
//      "title": "**Information about build:**",
//      "facts": [
//        {
//          "name": "Branch name",
//          "value": "%teamcity.build.branch%"
//        },
//        {
//          "name": "Build Number",
//          "value": "%build.number%"
//        },
//        {
//          "name": "Agent name",
//          "value": "%teamcity.agent.name%"
//        },
//        {
//        "name": "Triggered by user name",
//        "value": "%teamcity.build.triggeredBy.username%"
//        }
//      ]
//    },
//    {
//      "startGroup": true,
//      "title": "**Information about tests:**",
//      "facts": [
//        {
//        "name": "Passed",
//        "value": "%env.PASSED_TESTS%"
//        },
//        {
//        "name": "Failed",
//        "value": "%env.FAILED_TESTS%"
//        },
//        {
//        "name": "Skipped",
//        "value": "%env.SKIPPED_TESTS%"
//        },
//        {
//        "name": "Duration",
//        "value": "%env.DURATION%"
//        }
//      ]
//    }
//  ],
//  "potentialAction": [
//    {
//      "@type": "OpenUri",
//      "name": "Open detailed report",
//      "targets": [
//        {
//          "os": "default",
//          "uri": "%allure_report_url%"
//        }
//      ]
//    }
//  ]
//}
//            """.trimIndent()
            scriptContent = """
        PAYLOAD=${'$'}(.teamcity/teamsCard/payload.json)
        curl -H 'Content-Type: application/json' -d '${'$'}PAYLOAD' %webhook_url%
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
