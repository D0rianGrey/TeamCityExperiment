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

        script {
            name = "Command Line from UI"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = """
                    PASSED_COUNT=${'$'}(jq '.counters.passed' allure-report/export/prometheusData.txt)
                    echo "##teamcity[setParameter name='env.PASSED_TESTS' value='${'$'}PASSED_COUNT']"
                """.trimIndent()
        }

        script {
            name = "Send Adaptive Card to Microsoft Teams as Allure report"
            executionMode = BuildStep.ExecutionMode.ALWAYS

            //Basic info
            val ALLURE_REPORT_URL =
                "http://localhost:8111/buildConfiguration/TeamCityExperiment_Build/%teamcity.build.id%?buildTab=report_project1_Test_Results"

            // About build
            val BRANCH_NAME = "%teamcity.build.branch%"
            val BUILD_ID = "%teamcity.build.id%"
            val AGENT_NAME = "%teamcity.agent.name%"
            val TRIGGERED_BY_USER_NAME = "%teamcity.build.triggeredBy.username%"

            // About tests

            val WEBHOOK_URL =
                "https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
            val PAYLOAD = """
                {
  "@context": "http://schema.org/extensions",
  "@type": "MessageCard",
  "themeColor": "0076D7",
  "title": "Allure Report",
  "text": "This is a simplified report after the job is completed",
  "sections": [
    {
      "startGroup": true,
      "title": "**Information about build:**",
      "facts": [
        {
          "name": "Branch name",
          "value": "$BRANCH_NAME"
        },
        {
          "name": "Build ID",
          "value": "$BUILD_ID"
        },
        {
          "name": "Agent name",
          "value": "$AGENT_NAME"
        },
        {
        "name": "Triggered by user name",
        "value": "$TRIGGERED_BY_USER_NAME"
        }
      ]
    },
    {
      "startGroup": true,
      "title": "**Information about tests:**",
      "facts": [
        {
        "name": "Number of Passed Tests",
        "value": "Passed"
        }
      ]
    }
  ],
  "potentialAction": [
    {
      "@type": "OpenUri",
      "name": "Open detailed report",
      "targets": [
        {
          "os": "default",
          "uri": "$ALLURE_REPORT_URL"
        }
      ]
    }
  ]
}
            """.trimIndent()
            scriptContent = """
        curl -H 'Content-Type: application/json' -d '$PAYLOAD' $WEBHOOK_URL
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
