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
            name = "Send 'Hello' to Microsoft Teams"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            val BRANCH_NAME = "%teamcity.build.branch%"
            val ALLURE_REPORT_URL =
                "http://localhost:8111/buildConfiguration/TeamCityExperiment_Build/%teamcity.build.id%?buildTab=report_project1_Test_Results"
            val WEBHOOK_URL =
                "https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
            scriptContent = """
        curl -H 'Content-Type: application/json' -d '{"text": "$BRANCH_NAME [View Allure Report]($ALLURE_REPORT_URL)"}' $WEBHOOK_URL
    """.trimIndent()
        }

        script {
            name = "Send Adaptive Card to Microsoft Teams"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            val WEBHOOK_URL =
                "https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
            val PAYLOAD = """
                {
    "@context": "http://schema.org/extensions",
    "@type": "MessageCard",
    "themeColor": "0076D7",
    "title": "Card Title",
    "text": "This is a sample Connector Card for Microsoft 365 Groups.",
    "sections": [
        {
            "startGroup": true,
            "title": "Section Title",
            "text": "Section text",
            "activityTitle": "Activity title",
            "activitySubtitle": "Activity subtitle",
            "activityImage": "https://example.com/image.jpg",
            "facts": [
                {
                    "name": "Fact 1",
                    "value": "Value 1"
                },
                {
                    "name": "Fact 2",
                    "value": "Value 2"
                }
            ]
        }
    ],
    "potentialAction": [
        {
            "@type": "OpenUri",
            "name": "Go to Website",
            "targets": [
                {
                    "os": "default",
                    "uri": "https://example.com"
                }
            ]
        },
        {
            "@type": "ActionCard",
            "name": "Comment",
            "inputs": [
                {
                    "@type": "TextInput",
                    "id": "comment",
                    "isMultiline": false,
                    "title": "Add a comment"
                }
            ],
            "actions": [
                {
                    "@type": "HttpPOST",
                    "name": "Save",
                    "target": "https://example.com/save-comment"
                }
            ]
        }
    ]
}
            """.trimIndent()
            scriptContent = """
        curl -H 'Content-Type: application/json' -d "$PAYLOAD" $WEBHOOK_URL
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
