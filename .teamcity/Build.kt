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
            name = "Send 'Hello' to Microsoft Teams"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            val WEBHOOK_URL =
                "https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
            val payload = """
                {
            "type": "message",
            "attachments": [
                {
                    "contentType": "application/vnd.microsoft.card.adaptive",
                    "contentUrl": null,
                    "content": {
                        "schema": "http://adaptivecards.io/schemas/adaptive-card.json",
                        "type": "AdaptiveCard",
                        "version": "1.2",
                        "body": [
                            {
                                "type": "TextBlock",
                                "text": "BRANCH_NAME",
                                "wrap": true
                            },
                            {
                                "type": "TextBlock",
                                "text": "[View Allure Report](ALLURE_REPORT_URL)",
                                "wrap": true
                            }
                        ]
                    }
                }
            ]
        }
            """.trimIndent()
            scriptContent = """
        curl -H 'Content-Type: application/json' -d '$payload' $WEBHOOK_URL
    """.trimIndent()
        }


//        script {
//            name = "Send 'Hello' to Microsoft Teams"
//            executionMode = BuildStep.ExecutionMode.ALWAYS
//            val WEBHOOK_URL =
//                "https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
//            scriptContent = """
//        curl -H 'Content-Type: application/json' -d '{"text": "Hello"}' \$WEBHOOK_URL
//    """.trimIndent()
//        }

//        script {
//            name = "Send Adaptive Card to Microsoft Teams"
//            executionMode = BuildStep.ExecutionMode.ALWAYS
//            val WEBHOOK_URL =
//                "https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
//            val PAYLOAD = """
//                {
//            "type": "message",
//            "attachments": [
//                {
//                    "contentType": "application/vnd.microsoft.card.adaptive",
//                    "contentUrl": null,
//                    "content": {
//                        "schema": "http://adaptivecards.io/schemas/adaptive-card.json",
//                        "type": "AdaptiveCard",
//                        "version": "1.2",
//                        "body": [
//                            {
//                                "type": "TextBlock",
//                                "text": "For Samples and Templates, see [https://adaptivecards.io/samples](https://adaptivecards.io/samples)"
//                            }
//                        ]
//                    }
//                }
//            ]
//        }
//            """.trimIndent()
//            scriptContent = """
//        curl -H 'Content-Type: application/json' -d "$PAYLOAD" $WEBHOOK_URL
//    """.trimIndent()
//        }
//        script {
//            name = "Send using Python"
//            executionMode = BuildStep.ExecutionMode.ALWAYS
//            scriptContent = """
//        python - <<END
//        import requests
//        import json
//
//        WEBHOOK_URL = 'https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca'
//        payload = {
//            "type": "message",
//            "attachments": [
//                {
//                    "contentType": "application/vnd.microsoft.card.adaptive",
//                    "contentUrl": None,
//                    "content": {
//                        "$schema": "http://adaptivecards.io/schemas/adaptive-card.json",
//                        "type": "AdaptiveCard",
//                        "version": "1.2",
//                        "body": [
//                            {
//                                "type": "TextBlock",
//                                "text": "For Samples and Templates, see [https://adaptivecards.io/samples](https://adaptivecards.io/samples)"
//                            }
//                        ]
//                    }
//                }
//            ]
//        }
//
//        headers = {'Content-Type': 'application/json'}
//        response = requests.post(WEBHOOK_URL, headers=headers, data=json.dumps(payload))
//        print(response.status_code)
//        print(response.content)
//        END
//    """.trimIndent()
//        }

//        script {
//            name = "Debug: Print all environment variables and parameters"
//            scriptContent = """
//        printenv
//        echo "TeamCity parameters:"
//        echo "teamcity.build.status = %teamcity.build.status%"
//        echo "teamcity.build.test.total = %teamcity.build.test.total%"
//    """.trimIndent()
//        }

//        script {
//            name = "Send Detailed Info to Microsoft Teams"
//            executionMode = BuildStep.ExecutionMode.ALWAYS
//            val ALLURE_REPORT_URL =
//                "http://localhost:8111/buildConfiguration/TeamCityExperiment_Build/%teamcity.build.id%?buildTab=report_project1_Test_Results"
//            val WEBHOOK_URL =
//                "https://vakerin.webhook.office.com/webhookb2/9c1222ef-4e94-4519-8587-4c6d274a897d@09e68569-5204-4f37-8857-099b0cdfc689/IncomingWebhook/e665721392a24e019db0c59371fe5bb2/a217d337-3a25-44ea-bf80-629df276aeca"
//            val BRANCH_NAME = DslContext.getParameter("teamcity.build.branch")
//            val STATUS = DslContext.getParameter("teamcity.build.status")
//            val TOTAL_TESTS = DslContext.getParameter("teamcity.build.test.total");
//            val PAYLOAD = """
//                {
//            "@type": "MessageCard",
//            "@context": "http://schema.org/extensions",
//            "summary": "Build Summary",
//            "sections": [{
//                "activityTitle": "Build Completed",
//                "activitySubtitle": "Branch: $BRANCH_NAME, Status: $STATUS, Total Tests: $TOTAL_TESTS",
//                "activityImage": "",
//                "facts": [
//                    { "name": "Branch", "value": "$BRANCH_NAME" },
//                    { "name": "Status", "value": "$STATUS" },
//                    { "name": "Total Tests Run", "value": "$TOTAL_TESTS" },
//                    { "name": "Allure Report", "value": "[View Report]($ALLURE_REPORT_URL)" }
//                ]
//            }]
//        }
//            """.trimIndent()
//
//            scriptContent = """
//
//        curl -H 'Content-Type: application/json' -d "$PAYLOAD" $WEBHOOK_URL
//    """.trimIndent()
//        }


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
